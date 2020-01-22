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

import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.util.Helper;

/**
 * Base connector helper.  
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ConnectorHelper implements Helper
{
	/** The protocols simultaneously supported. */
	private List<Protocol> supportedProtocols;

	/**
	 * Constructor.
	 */
	public ConnectorHelper()
	{
		this.supportedProtocols = null;
	}

	/**
	 * Returns the protocols simultaneously supported.
	 * @return The protocols simultaneously supported.
	 */
	public List<Protocol> getSupportedProtocols()
	{
		if (this.supportedProtocols == null)
			this.supportedProtocols = new ArrayList<Protocol>();
		return this.supportedProtocols;
	}

	/**
	 * Creates a new context.
	 * @return The new context.
	 */
	public Context createContext()
	{
		return null;
	}

	/**
	 * Handles a call.
	 * @param request The request to handle.
	 * @param response The response to update.
	 */
	public void handle(Request request, Response response)
	{
	}

	/** Start hook. */
	public void start() throws Exception
	{
	}

	/** Stop callback. */
	public void stop() throws Exception
	{
	}

}
