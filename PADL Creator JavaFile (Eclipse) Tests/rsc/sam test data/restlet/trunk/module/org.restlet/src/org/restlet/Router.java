/*
 * Copyright 2005-2006 Noelios Consulting.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet;

import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.util.ScorerList;

/**
 * Chainer routing calls to one of the attached scorers. Each scorer represents a potential route that can 
 * compute an affinity score for each call depending on various criteria. The attach() method allow the 
 * creation of scorers based on URI patterns matching the beginning of a the relative resource part.<br/>
 * <br/>
 * In addition, several routing modes are supported, implementing various algorithms like:
 * <ul>
 * <li>Best match (default)</li>
 * <li>First match</li>
 * <li>Last match</li>
 * <li>Random match</li>
 * <li>Round robin</li>
 * <li>Custom</li>
 * </ul>
 * <br/>
 * Note that for scorers using URI patterns will update the resource base reference during the routing if 
 * they are selected. If you are using hierarchical paths, remember to directly attach the child routers to 
 * their parent router instead of the top level Restlet container. Also, remember to manually handle the 
 * path separator characters in your path patterns otherwise the delegation will not work as expected.<br/>
 * <br/>
 * Finally, you can modify the scorers list while handling incoming calls as the delegation code is ensured 
 * to be thread-safe.
 * @see <a href="http://www.restlet.org/tutorial#part11">Tutorial: Routers and hierarchical URIs</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public class Router extends Chainer
{
	/**
	 * Each call will be routed to the scorer with the best score, if the required score is reached.
	 */
	public static final int BEST = 1;

	/**
	 * Each call is routed to the first scorer if the required score is reached. If the required score 
	 * is not reached, then the scorer is skipped and the next one is considered. 
	 */
	public static final int FIRST = 2;

	/**
	 * Each call will be routed to the last scorer if the required score is reached. If the required score 
	 * is not reached, then the scorer is skipped and the previous one is considered. 
	 */
	public static final int LAST = 3;

	/**
	 * Each call is be routed to the next scorer target if the required score is reached. The next scorer is 
	 * relative to the previous call routed (round robin mode). If the required score is not reached, then the
	 * scorer is skipped and the next one is considered. If the last scorer is reached, the first scorer will 
	 * be considered.  
	 */
	public static final int NEXT = 4;

	/**
	 * Each call will be randomly routed to one of the scorers that reached the required score. If the random 
	 * scorer selected is not a match then the immediate next scorer is evaluated until one matching scorer is 
	 * found. If we get back to the inital random scorer selected with no match, then we return null.
	 */
	public static final int RANDOM = 5;

	/**
	 * Each call will be routed according to a custom mode.
	 */
	public static final int CUSTOM = 6;

	/** The modifiable list of scorers. */
	private ScorerList scorers;

	/** The default scorer tested if no other one was available. */
	private Scorer defaultScorer;

	/** The routing mode. */
	private int routingMode;

	/** The minimum score required to have a match. */
	private float requiredScore;

	/** The maximum number of attempts if no attachment could be matched on the first attempt. */
	private int maxAttempts;

	/** The delay (in milliseconds) before a new attempt. */
	private long retryDelay;

	/**
	 * Constructor.
	 */
	public Router()
	{
		this(null);
	}

	/**
	 * Constructor.
	 * @param context The context.
	 */
	public Router(Context context)
	{
		super(context);
		this.scorers = null;
		this.defaultScorer = null;
		this.routingMode = BEST;
		this.requiredScore = 0.5F;
		this.maxAttempts = 1;
		this.retryDelay = 500L;
	}

	/**
	 * Attaches a target to this router based on a given URI pattern. A new scorer will be added routing
	 * to the target when calls with a URI matching the pattern will be received.
	 * @param uriPattern The URI pattern that must match the relative part of the resource URI. 
	 * @param target The target Restlet to attach.
	 */
	public void attach(String uriPattern, Restlet target)
	{
		getScorers().add(uriPattern, target);
	}

	/**
	 * Detaches the target from this router. All scorers routing to this target Restlet are removed
	 * from the list of scorers.
	 * @param target The target Restlet to detach.
	 */
	public void detach(Restlet target)
	{
		getScorers().removeAll(target);
	}

	/**
	 * Returns the matched scorer according to a custom algorithm. To use in combination of the RouterMode.CUSTOM 
	 * enumeration. The default implementation (to be overriden), returns null. 
	 * @param request The request to handle.
	 * @param response The response to update.
	 * @return The matched scorer if available or null.
	 */
	protected Scorer getCustom(Request request, Response response)
	{
		return null;
	}

	/**
	 * Returns the default scorer to test if no other one was available after retying the maximum number
	 * of attemps.
	 * @return The default scorer tested if no other one was available.
	 */
	public Scorer getDefaultScorer()
	{
		return this.defaultScorer;
	}

	/**
	 * Returns the maximum number of attempts if no attachment could be matched on the first attempt.
	 * This is useful when the attachment scoring is dynamic and therefore could change on a retry.
	 * The default value is set to 1. 
	 * @return The maximum number of attempts if no attachment could be matched on the first attempt.
	 */
	public int getMaxAttempts()
	{
		return this.maxAttempts;
	}

	/**
	 * Returns the next Restlet if available.
	 * @param request The request to handle.
	 * @param response The response to update.
	 * @return The next Restlet if available or null.
	 */
	public Restlet getNext(Request request, Response response)
	{
		Scorer result = null;

		for (int i = 0; (result == null) && (i < getMaxAttempts()); i++)
		{
			if (i > 0)
			{
				// Before attempting another time, let's
				// sleep during the "retryDelay" set.
				try
				{
					Thread.sleep(getRetryDelay());
				}
				catch (InterruptedException e)
				{
				}
			}

			if (this.scorers != null)
			{
				// Select the routing mode
				switch (getRoutingMode())
				{
					case BEST:
						result = getScorers().getBest(request, response, getRequiredScore());
					break;

					case FIRST:
						result = getScorers().getFirst(request, response, getRequiredScore());
					break;

					case LAST:
						result = getScorers().getLast(request, response, getRequiredScore());
					break;

					case NEXT:
						result = getScorers().getNext(request, response, getRequiredScore());
					break;

					case RANDOM:
						result = getScorers().getRandom(request, response, getRequiredScore());
					break;

					case CUSTOM:
						result = getCustom(request, response);
					break;
				}
			}
		}

		if (result == null)
		{
			// If nothing matched in the scorers list, check the default scorer
			if ((getDefaultScorer() != null)
					&& (getDefaultScorer().score(request, response) >= getRequiredScore()))
			{
				result = getDefaultScorer();
			}
			else
			{
				// No route could be found
				response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			}
		}

		return result;
	}

	/**
	 * Returns the minimum score required to have a match.
	 * @return The minimum score required to have a match.
	 */
	public float getRequiredScore()
	{
		return this.requiredScore;
	}

	/**
	 * Returns the delay (in seconds) before a new attempt. The default value is 500 ms.
	 * @return The delay (in seconds) before a new attempt.
	 */
	public long getRetryDelay()
	{
		return this.retryDelay;
	}

	/**
	 * Returns the routing mode.
	 * @return The routing mode.
	 */
	public int getRoutingMode()
	{
		return this.routingMode;
	}

	/**
	 * Returns the modifiable list of scorers.
	 * @return The modifiable list of scorers.
	 */
	public ScorerList getScorers()
	{
		if (this.scorers == null) this.scorers = new ScorerList(this);
		return this.scorers;
	}

	/**
	 * Sets the default scorer tested if no other one was available.
	 * @param defaultScorer The default scorer tested if no other one was available.
	 */
	public void setDefaultScorer(Scorer defaultScorer)
	{
		this.defaultScorer = defaultScorer;
	}

	/**
	 * Sets the maximum number of attempts if no attachment could be matched on the first attempt.
	 * This is useful when the attachment scoring is dynamic and therefore could change on a retry.
	 * @param maxAttempts The maximum number of attempts. 
	 */
	public void setMaxAttempts(int maxAttempts)
	{
		this.maxAttempts = maxAttempts;
	}

	/**
	 * Sets the score required to have a match.
	 * @param score The score required to have a match.
	 */
	public void setRequiredScore(float score)
	{
		this.requiredScore = score;
	}

	/**
	 * Sets the delay (in seconds) before a new attempt.
	 * @param retryDelay The delay (in seconds) before a new attempt.
	 */
	public void setRetryDelay(long retryDelay)
	{
		this.retryDelay = retryDelay;
	}

	/**
	 * Sets the routing mode.
	 * @param routingMode The routing mode.
	 */
	public void setRoutingMode(int routingMode)
	{
		this.routingMode = routingMode;
	}

}
