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

package com.noelios.restlet.ext.simple;

import java.net.ServerSocket;

import org.restlet.Server;
import org.restlet.data.Protocol;

import simple.http.BufferedPipelineFactory;
import simple.http.PipelineHandlerFactory;
import simple.http.connect.ConnectionFactory;

/**
 * Simple HTTPS server connector.
 * @author Lars Heuer (heuer[at]semagia.com) <a href="http://semagia.com/">Semagia</a>
 * @author Jerome Louvel (contact@noelios.com) <a href="http://www.noelios.com">Noelios Consulting</a>
 */
public class HttpServerHelper extends SimpleServerHelper
{
	/**
	 * Constructor.
	 * @param server The server to help.
	 */
	public HttpServerHelper(Server server)
	{
		super(server);
		getSupportedProtocols().add(Protocol.HTTP);
	}

	/** Starts the Restlet. */
	public void start() throws Exception
	{
		setSocket(new ServerSocket(getServer().getPort()));
		setConfidential(false);
		setHandler(PipelineHandlerFactory.getInstance(new SimpleProtocolHandler(this),
				getDefaultThreads(), getMaxWaitTimeMs()));
		setConnection(ConnectionFactory.getConnection(getHandler(),
				new BufferedPipelineFactory()));
		getConnection().connect(getSocket());
		super.start();
	}

}
