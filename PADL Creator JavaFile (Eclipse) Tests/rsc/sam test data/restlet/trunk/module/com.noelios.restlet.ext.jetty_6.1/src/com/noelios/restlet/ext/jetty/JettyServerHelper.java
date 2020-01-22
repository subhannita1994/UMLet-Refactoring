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

package com.noelios.restlet.ext.jetty;

import java.io.IOException;

import javax.servlet.ServletException;

import org.mortbay.jetty.AbstractConnector;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Server;
import org.mortbay.thread.BoundedThreadPool;

/**
 * Abstract Jetty Web server connector. Here is the list of parameters that are supported:
 * <table>
 * 	<tr>
 * 		<th>Parameter name</th>
 * 		<th>Value type</th>
 * 		<th>Default value</th>
 * 		<th>Description</th>
 * 	</tr>
 * 	<tr>
 * 		<td>minThreads</td>
 * 		<td>int</td>
 * 		<td>1</td>
 * 		<td>Minumum threads waiting to service requests.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>maxThread</td>
 * 		<td>int</td>
 * 		<td>255</td>
 * 		<td>Maximum threads that will service requests.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>threadMaxIdleTimeMs</td>
 * 		<td>int</td>
 * 		<td>60000</td>
 * 		<td>Time for an idle thread to wait for a request or read.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>lowThreads</td>
 * 		<td>int</td>
 * 		<td>25</td>
 * 		<td>Threshold of remaining threads at which the server is considered as running low on resources.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>lowResourceMaxIdleTimeMs</td>
 * 		<td>int</td>
 * 		<td>2500</td>
 * 		<td>Time in ms that connections will persist if listener is low on resources.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>acceptorThreads</td>
 * 		<td>int</td>
 * 		<td>1</td>
 * 		<td>Number of acceptor threads to set.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>acceptQueueSize</td>
 * 		<td>int</td>
 * 		<td>0</td>
 * 		<td>Size of the accept queue.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>headerBufferSize</td>
 * 		<td>int</td>
 * 		<td>4*1024</td>
 * 		<td>Size of the buffer to be used for request and response headers.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>requestBufferSize</td>
 * 		<td>int</td>
 * 		<td>8*1024</td>
 * 		<td>Size of the content buffer for receiving requests.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>responseBufferSize</td>
 * 		<td>int</td>
 * 		<td>32*1024</td>
 * 		<td>Size of the content buffer for sending responses.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>ioMaxIdleTimeMs</td>
 * 		<td>int</td>
 * 		<td>30000</td>
 * 		<td>Maximum time to wait on an idle IO operation.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>soLingerTime</td>
 * 		<td>int</td>
 * 		<td>1000</td>
 * 		<td>SO linger time (see Jetty 6 documentation).</td>
 * 	</tr>
 * 	<tr>
 * 		<td>converter</td>
 * 		<td>String</td>
 * 		<td>com.noelios.restlet.http.HttpServerConverter</td>
 * 		<td>Class name of the converter of low-level HTTP calls into high level requests and responses.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>useForwardedForHeader</td>
 * 		<td>boolean</td>
 * 		<td>false</td>
 * 		<td>Lookup the "X-Forwarded-For" header supported by popular proxies and caches and uses it to populate 
 * the Request.getClientAddresses() method result. This information is only safe for intermediary components 
 * within your local network. Other addresses could easily be changed by setting a fake header and should not
 * be trusted for serious security checks.</td>
 * 	</tr>
 * </table>
 * @see <a href="http://jetty.mortbay.org/jetty6/">Jetty home page</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public abstract class JettyServerHelper extends com.noelios.restlet.http.HttpServerHelper
{
	/** The wrapped Jetty server. */
	private Server wrappedServer;

	/** The internal Jetty connector. */
	private AbstractConnector connector;

	/**
	 * Constructor.
	 * @param server The server to help.
	 */
	public JettyServerHelper(org.restlet.Server server)
	{
		super(server);
		this.connector = null;
		this.wrappedServer = new WrappedServer(this);

		// Configuring the thread pool
		BoundedThreadPool btp = new BoundedThreadPool();
		btp.setLowThreads(getLowThreads());
		btp.setMaxIdleTimeMs(getThreadMaxIdleTimeMs());
		btp.setMaxThreads(getMaxThreads());
		btp.setMinThreads(getMinThreads());
		getWrappedServer().setThreadPool(btp);
	}

	/** Starts the Connector. */
	public void start() throws Exception
	{
		if (this.connector == null)
		{
			this.connector = createConnector();
			configure(this.connector);
			getWrappedServer().addConnector(connector);
		}

		getWrappedServer().start();
	}

	/** Stops the Connector. */
	public void stop() throws Exception
	{
		getWrappedServer().stop();
	}

	/**
	 * Creates a new internal Jetty connector.
	 * @return A new internal Jetty connector.
	 */
	protected abstract AbstractConnector createConnector();

	/**
	 * Configures the internal Jetty connector.
	 * @param connector The internal Jetty connector.
	 */
	protected void configure(AbstractConnector connector)
	{
		if (getServer().getAddress() != null) connector.setHost(getServer().getAddress());
		connector.setPort(getServer().getPort());
		connector.setLowResourceMaxIdleTime(getLowResourceMaxIdleTimeMs());
		connector.setAcceptors(getAcceptorThreads());
		connector.setAcceptQueueSize(getAcceptQueueSize());
		connector.setHeaderBufferSize(getHeaderBufferSize());
		connector.setRequestBufferSize(getRequestBufferSize());
		connector.setResponseBufferSize(getResponseBufferSize());
		connector.setMaxIdleTime(getIoMaxIdleTimeMs());
		connector.setSoLingerTime(getSoLingerTime());
	}

	/**
	 * Jetty server wrapped by a parent Restlet HTTP server connector.
	 * @author Jerome Louvel (contact@noelios.com)
	 */
	private static class WrappedServer extends org.mortbay.jetty.Server
	{
		JettyServerHelper helper;

		/**
		 * Constructor.
		 * @param server The Jetty HTTP server.
		 */
		public WrappedServer(JettyServerHelper server)
		{
			this.helper = server;
		}

		/**
		 * Handler method converting a Jetty Connection into a Restlet Call.
		 * @param connection The connection to handle.
		 */
		public void handle(HttpConnection connection) throws IOException, ServletException
		{
			helper.handle(new JettyCall(helper.getLogger(), connection));
		}
	};

	/**
	 * Returns the minumum threads waiting to service requests.
	 * @return The minumum threads waiting to service requests.
	 */
	public int getMinThreads()
	{
		return Integer.parseInt(getParameters().getFirstValue("minThreads", "1"));
	}

	/**
	 * Returns the maximum threads that will service requests.
	 * @return The maximum threads that will service requests.
	 */
	public int getMaxThreads()
	{
		return Integer.parseInt(getParameters().getFirstValue("maxThreads", "255"));
	}

	/**
	 * Returns the time for an idle thread to wait for a request or read.
	 * @return The time for an idle thread to wait for a request or read.
	 */
	public int getThreadMaxIdleTimeMs()
	{
		return Integer.parseInt(getParameters().getFirstValue("threadMaxIdleTimeMs",
				"60000"));
	}

	/**
	 * Returns the threshold of remaining threads at which the server is considered as running low on resources.
	 * @return The threshold of remaining threads at which the server is considered as running low on resources.
	 */
	public int getLowThreads()
	{
		return Integer.parseInt(getParameters().getFirstValue("lowThreads", "25"));
	}

	/**
	 * Returns the time in ms that connections will persist if listener is low on resources.
	 * @return The time in ms that connections will persist if listener is low on resources.
	 */
	public int getLowResourceMaxIdleTimeMs()
	{
		return Integer.parseInt(getParameters().getFirstValue("lowResourceMaxIdleTimeMs",
				"2500"));
	}

	/**
	 * Returns the number of acceptor threads to set. 
	 * @return The number of acceptor threads to set.
	 */
	public int getAcceptorThreads()
	{
		return Integer.parseInt(getParameters().getFirstValue("acceptorThreads", "1"));
	}

	/**
	 * Returns the size of the accept queue.
	 * @return The size of the accept queue.
	 */
	public int getAcceptQueueSize()
	{
		return Integer.parseInt(getParameters().getFirstValue("acceptQueueSize", "0"));
	}

	/**
	 * Returns the size of the buffer to be used for request and response headers.
	 * @return The size of the buffer to be used for request and response headers.
	 */
	public int getHeaderBufferSize()
	{
		return Integer.parseInt(getParameters().getFirstValue("headerBufferSize",
				Integer.toString(4 * 1024)));
	}

	/**
	 * Returns the size of the content buffer for receiving requests.
	 * @return The size of the content buffer for receiving requests.
	 */
	public int getRequestBufferSize()
	{
		return Integer.parseInt(getParameters().getFirstValue("requestBufferSize",
				Integer.toString(8 * 1024)));
	}

	/**
	 * Returns the size of the content buffer for sending responses.
	 * @return The size of the content buffer for sending responses.
	 */
	public int getResponseBufferSize()
	{
		return Integer.parseInt(getParameters().getFirstValue("responseBufferSize",
				Integer.toString(32 * 1024)));
	}

	/**
	 * Returns the maximum time to wait on an idle IO operation.
	 * @return The maximum time to wait on an idle IO operation.
	 */
	public int getIoMaxIdleTimeMs()
	{
		return Integer.parseInt(getParameters().getFirstValue("ioMaxIdleTimeMs", "30000"));
	}

	/**
	 * Returns the SO linger time (see Jetty 6 documentation).
	 * @return The SO linger time (see Jetty 6 documentation).
	 */
	public int getSoLingerTime()
	{
		return Integer.parseInt(getParameters().getFirstValue("soLingerTime", "1000"));
	}

	/**
	 * Sets the wrapped Jetty server.
	 * @param wrappedServer The wrapped Jetty server.
	 */
	protected void setWrappedServer(Server wrappedServer)
	{
		this.wrappedServer = wrappedServer;
	}

	/**
	 * Returns the wrapped Jetty server.
	 * @return The wrapped Jetty server.
	 */
	protected Server getWrappedServer()
	{
		return this.wrappedServer;
	}

}
