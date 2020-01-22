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

package com.noelios.restlet.container;

import org.restlet.Container;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.Scorer;
import org.restlet.VirtualHost;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 * Router that collects calls from all server connectors and dispatches them to the appropriate
 * host routers for dispatching to the user applications.
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ServerRouter extends Router
{
	/** The parent container. */
	private Container container;

	/**
	 * Constructor.
	 * @param container The parent container.
	 */
	public ServerRouter(Container container)
	{
		super(container.getContext());
		this.container = container;
	}

	/** Starts the Restlet. */
	public void start() throws Exception
	{
		// Attach all virtual hosts
		for (VirtualHost host : getContainer().getHosts())
		{
			getScorers().add(new HostScorer(this, host));
		}

		// Also attach the local host if it exists
		if (getContainer().getDefaultHost() != null)
		{
			getScorers().add(new HostScorer(this, getContainer().getDefaultHost()));
		}

		// If no host matches, display and error page with a precise message
		Restlet noHostMatched = new Restlet(getContainer().getContext())
		{
			public void handle(Request request, Response response)
			{
				response.setStatus(Status.CLIENT_ERROR_NOT_FOUND,
						"No virtual host could handle the request");
			}
		};
		setDefaultScorer(new Scorer(this, noHostMatched));

		// Start the router
		super.start();
	}

	/**
	 * Returns the parent container.
	 * @return The parent container.
	 */
	private Container getContainer()
	{
		return container;
	}
}
