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

package com.noelios.restlet.ext.httpclient;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.data.Request;

import com.noelios.restlet.http.HttpClientCall;

/**
 * HTTP client connector using the HttpMethodCall and Apache HTTP Client project. Here is the list of 
 * parameters that are supported:
 * <table>
 * 	<tr>
 * 		<th>Parameter name</th>
 * 		<th>Value type</th>
 * 		<th>Default value</th>
 * 		<th>Description</th>
 * 	</tr>
 * 	<tr>
 * 		<td>followRedirects</td>
 * 		<td>boolean</td>
 * 		<td>false</td>
 * 		<td>If true, the protocol will automatically follow redirects. If false, the protocol will not automatically follow redirects.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>maxConnectionsPerHost</td>
 * 		<td>int</td>
 * 		<td>2 (uses HttpClient's default)</td>
 * 		<td>The maximum number of connections that will be created for any particular host.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>maxTotalConnections</td>
 * 		<td>int</td>
 * 		<td>20 (uses HttpClient's default)</td>
 * 		<td>The maximum number of active connections.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>connectionManagerTimeout</td>
 * 		<td>int</td>
 * 		<td>0</td>
 * 		<td>The timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>stopIdleTimeout</td>
 * 		<td>int</td>
 * 		<td>1000</td>
 * 		<td>The minimum idle time, in milliseconds, for connections to be closed when stopping the connector.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>readTimeout</td>
 * 		<td>int</td>
 * 		<td>0</td>
 * 		<td>Sets the read timeout to a specified timeout, in milliseconds. A timeout of zero is interpreted as an infinite timeout.</td>
 * 	</tr>
 * </table>
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/guide/net/index.html">Networking Features</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public class HttpClientHelper extends com.noelios.restlet.http.HttpClientHelper
{
	private HttpClient httpClient;

	/**
	 * Constructor.
	 * @param client The client to help.
	 */
	public HttpClientHelper(Client client)
	{
		super(client);
		this.httpClient = null;
		getSupportedProtocols().add(Protocol.HTTP);
		getSupportedProtocols().add(Protocol.HTTPS);
	}

	public HttpClient getHttpClient()
	{
		return this.httpClient;
	}

	@Override
	public void start() throws Exception
	{
		super.start();

		// Create the multi-threaded connection manager and configure it
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(
				getMaxConnectionsPerHost());
		connectionManager.getParams().setMaxTotalConnections(getMaxTotalConnections());

		// Create the internal client connector
		this.httpClient = new HttpClient(connectionManager);
		getHttpClient().getParams().setAuthenticationPreemptive(false);
		getHttpClient().getParams().setConnectionManagerTimeout(
				getConnectionManagerTimeout());
		getHttpClient().getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		getHttpClient().getParams().setSoTimeout(getReadTimeout());
	}

	@Override
	public void stop() throws Exception
	{
		getHttpClient().getHttpConnectionManager().closeIdleConnections(
				getStopIdleTimeout());
	}

	/**
	 * Creates a low-level HTTP client call from a high-level uniform call.
	 * @param request The high-level request.
	 * @return A low-level HTTP client call.
	 */
	public HttpClientCall create(Request request)
	{
		HttpClientCall result = null;

		try
		{
			result = new HttpMethodCall(this, request.getMethod().toString(), request
					.getResourceRef().toString(), request.isEntityAvailable());
		}
		catch (IOException ioe)
		{
			getLogger().log(Level.WARNING, "Unable to create the HTTP client call", ioe);
		}

		return result;
	}

	/**
	 * Indicates if the protocol will automatically follow redirects. 
	 * @return True if the protocol will automatically follow redirects.
	 */
	public boolean isFollowRedirects()
	{
		return Boolean.parseBoolean(getParameters().getFirstValue("followRedirects",
				"false"));
	}

	/**
	 * Returns the maximum number of connections that will be created for any particular host.
	 * @return The maximum number of connections that will be created for any particular host.
	 */
	public int getMaxConnectionsPerHost()
	{
		return Integer
				.parseInt(getParameters().getFirstValue("maxConnectionsPerHost", "2"));
	}

	/**
	 * Returns the maximum number of active connections.
	 * @return The maximum number of active connections.
	 */
	public int getMaxTotalConnections()
	{
		return Integer.parseInt(getParameters().getFirstValue("maxTotalConnections", "20"));
	}

	/**
	 * Returns the timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.
	 * @return The timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.
	 */
	public int getConnectionManagerTimeout()
	{
		return Integer.parseInt(getParameters().getFirstValue("connectionManagerTimeout",
				"0"));
	}

	/**
	 * Returns the minimum idle time, in milliseconds, for connections to be closed when stopping the connector.
	 * @return The minimum idle time, in milliseconds, for connections to be closed when stopping the connector.
	 */
	public int getStopIdleTimeout()
	{
		return Integer.parseInt(getParameters().getFirstValue("stopIdleTimeout", "1000"));
	}

	/**
	 * Returns the read timeout value. A timeout of zero is interpreted as an infinite timeout.
	 * @return The read timeout value.
	 */
	public int getReadTimeout()
	{
		return Integer.parseInt(getParameters().getFirstValue("readTimeout", "0"));
	}

}
