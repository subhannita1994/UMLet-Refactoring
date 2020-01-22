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

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Protocol;

/**
 * Restlet enabling communication between Components. "A connector is an abstract mechanism that mediates 
 * communication, coordination, or cooperation among components. Connectors enable communication between 
 * components by transferring data elements from one interface to another without changing the data." 
 * Roy T. Fielding</br>
 * <br/>
 * "Encapsulate the activities of accessing resources and transferring resource representations. The 
 * connectors present an abstract interface for component communication, enhancing simplicity by providing 
 * a clean separation of concerns and hiding the underlying implementation of resources and communication 
 * mechanisms" Roy T. Fielding
 * @see <a href="http://www.ics.uci.edu/~fielding/pubs/dissertation/software_arch.htm#sec_1_2_2">Source
 * dissertation</a>
 * @see <a href="http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm#sec_5_2_2">Source
 * dissertation</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public abstract class Connector extends Restlet
{
	/** The list of protocols simultaneously supported. */
	private List<Protocol> protocols;

	/**
	 * Constructor.
	 * @param context The context.
	 */
	public Connector(Context context)
	{
		this(context, null);
	}

	/**
	 * Constructor.
	 * @param context The context.
	 * @param protocols The supported protocols.
	 */
	public Connector(Context context, List<Protocol> protocols)
	{
		super(context);
		this.protocols = protocols;
	}

	/**
	 * Returns the protocols simultaneously supported.
	 * @return The protocols simultaneously supported.
	 */
	public List<Protocol> getProtocols()
	{
		if (this.protocols == null) this.protocols = new ArrayList<Protocol>();
		return this.protocols;
	}

}
