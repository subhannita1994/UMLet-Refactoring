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

package com.noelios.restlet;

import java.util.logging.Logger;

import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.ParameterList;
import org.restlet.data.Request;
import org.restlet.data.Response;

/**
 * Server connector helper.  
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ServerHelper extends ConnectorHelper
{
	/** The server to help. */
	private Server server;

	/**
	 * Constructor.
	 * @param server The client to help.
	 */
	public ServerHelper(Server server)
	{
		this.server = server;
	}

	/**
	 * Returns the server to help.
	 * @return The server to help.
	 */
	public Server getServer()
	{
		return this.server;
	}

	/**
	 * Returns the server parameters.
	 * @return The server parameters.
	 */
	public ParameterList getParameters()
	{
		ParameterList result = (getServer() != null) ? getServer().getContext()
				.getParameters() : null;
		if (result == null) result = new ParameterList();
		return result;
	}

	/**
	 * Returns the server logger.
	 * @return The server logger.
	 */
	public Logger getLogger()
	{
		return getServer().getLogger();
	}

	/**
	 * Returns the server context.
	 * @return The server context.
	 */
	public Context getContext()
	{
		return getServer().getContext();
	}

	/**
	 * Handles a call.
	 * @param request The request to handle.
	 * @param response The response to update.
	 */
	public void handle(Request request, Response response)
	{
		getServer().handle(request, response);
	}

}
