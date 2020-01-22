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

package com.noelios.restlet.http;

import java.util.Date;

import org.restlet.Application;
import org.restlet.data.ParameterList;
import org.restlet.data.Request;
import org.restlet.service.ConnectorService;
import org.restlet.util.DateUtils;

/**
 * Low-level call for the HTTP connectors.
 * @author Jerome Louvel (contact@noelios.com)
 */
public class HttpCall
{
	/** Indicates if the call is confidential. */
	private boolean confidential;

	/** The client IP address. */
	private String clientAddress;

	/** The method. */
	private String method;

	/** The reason phrase. */
	private String reasonPhrase;

	/** The request headers. */
	private ParameterList requestHeaders;

	/** The request URI. */
	private String requestUri;

	/** The response headers. */
	private ParameterList responseHeaders;

	/** The server IP address. */
	private String serverAddress;

	/** The server domain. */
	private String serverDomain;

	/** The server port. */
	private Integer serverPort;

	/** The status code. */
	private int statusCode;

	/**
	 * Constructor.
	 */
	public HttpCall()
	{
		this.confidential = false;
		this.clientAddress = null;
		this.method = null;
		this.reasonPhrase = "";
		this.requestHeaders = null;
		this.requestUri = null;
		this.responseHeaders = null;
		this.serverAddress = null;
		this.serverPort = null;
		this.statusCode = 200;
	}

	/**
	 * Returns the connector service associated to a request.
	 * @param request The request to lookup. 
	 * @return The connector service associated to a request.
	 */
	public ConnectorService getConnectorService(Request request)
	{
		ConnectorService result = null;
		Application application = (Application) request.getAttributes().get(
				Application.class.getCanonicalName());

		if (application != null)
		{
			result = application.getConnectorService();
		}
		else
		{
			result = new ConnectorService();
		}

		return result;
	}

	/**
	 * Formats a date as a header string.
	 * @param date The date to format.
	 * @param cookie Indicates if the date should be in the cookie format.
	 * @return The formatted date.
	 */
	public String formatDate(Date date, boolean cookie)
	{
		if (cookie)
		{
			return DateUtils.format(date, DateUtils.FORMAT_RFC_1036.get(0));
		}
		else
		{
			return DateUtils.format(date, DateUtils.FORMAT_RFC_1123.get(0));
		}
	}

	/**
	 * Returns the request address.<br/>
	 * Corresponds to the IP address of the requesting client.
	 * @return The request address.
	 */
	public String getClientAddress()
	{
		return this.clientAddress;
	}

	/**
	 * Returns the request method. 
	 * @return The request method.
	 */
	public String getMethod()
	{
		return this.method;
	}

	/**
	 * Returns the reason phrase.
	 * @return The reason phrase.
	 */
	public String getReasonPhrase()
	{
		return this.reasonPhrase;
	}

	/**
	 * Returns the modifiable list of request headers.
	 * @return The modifiable list of request headers.
	 */
	public ParameterList getRequestHeaders()
	{
		if (this.requestHeaders == null) this.requestHeaders = new ParameterList();
		return this.requestHeaders;
	}

	/**
	 * Returns the URI on the request line (most like a relative reference, but not necessarily). 
	 * @return The URI on the request line.
	 */
	public String getRequestUri()
	{
		return this.requestUri;
	}

	/**
	 * Returns the modifiable list of server headers.
	 * @return The modifiable list of server headers.
	 */
	public ParameterList getResponseHeaders()
	{
		if (this.responseHeaders == null) this.responseHeaders = new ParameterList();
		return this.responseHeaders;
	}

	/**
	 * Returns the response address.<br/>
	 * Corresponds to the IP address of the responding server.
	 * @return The response address.
	 */
	public String getServerAddress()
	{
		return this.serverAddress;
	}

	/** 
	 * Returns the server domain.
	 * @return The server domain.
	 */
	public String getServerDomain()
	{
		return this.serverDomain;
	}

	/** 
	 * Returns the server port.
	 * @return The server port.
	 */
	public Integer getServerPort()
	{
		return this.serverPort;
	}

	/**
	 * Returns the status code.
	 * @return The status code.
	 */
	public int getStatusCode()
	{
		return this.statusCode;
	}

	/**
	 * Indicates if the confidentiality of the call is ensured (ex: via SSL).
	 * @return True if the confidentiality of the call is ensured (ex: via SSL).
	 */
	public boolean isConfidential()
	{
		return this.confidential;
	}

	/**
	 * Parses a date string.
	 * @param date The date string to parse.
	 * @param cookie Indicates if the date is in the cookie format.
	 * @return The parsed date.
	 */
	public Date parseDate(String date, boolean cookie)
	{
		if (cookie)
		{
			return DateUtils.parse(date, DateUtils.FORMAT_RFC_1036);
		}
		else
		{
			return DateUtils.parse(date, DateUtils.FORMAT_RFC_1123);
		}
	}

	/**
	 * Sets the request address. 
	 * @param requestAddress The request address. 
	 */
	protected void setClientAddress(String requestAddress)
	{
		this.clientAddress = requestAddress;
	}

	/**
	 * Indicates if the confidentiality of the call is ensured (ex: via SSL).
	 * @param confidential True if the confidentiality of the call is ensured (ex: via SSL).
	 */
	protected void setConfidential(boolean confidential)
	{
		this.confidential = confidential;
	}

	/**
	 * Sets the request method. 
	 * @param method The request method.
	 */
	protected void setMethod(String method)
	{
		this.method = method;
	}

	/**
	 * Sets the reason phrase.
	 * @param reasonPhrase The reason phrase.
	 */
	public void setReasonPhrase(String reasonPhrase)
	{
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Sets the full request URI. 
	 * @param requestUri The full request URI.
	 */
	protected void setRequestUri(String requestUri)
	{
		this.requestUri = requestUri;
	}

	/**
	 * Sets the response address.<br/>
	 * Corresponds to the IP address of the responding server.
	 * @param responseAddress The response address.
	 */
	public void setServerAddress(String responseAddress)
	{
		this.serverAddress = responseAddress;
	}

	/** 
	 * Sets the server domain name.
	 * @param serverDomain The server domain name.
	 */
	public void setServerDomain(String serverDomain)
	{
		this.serverDomain = serverDomain;
	}

	/** 
	 * Sets the server port.
	 * @param serverPort The server port.
	 */
	public void setServerPort(Integer serverPort)
	{
		this.serverPort = serverPort;
	}

	/**
	 * Sets the status code.
	 * @param code The status code.
	 */
	public void setStatusCode(int code)
	{
		this.statusCode = code;
	}
}
