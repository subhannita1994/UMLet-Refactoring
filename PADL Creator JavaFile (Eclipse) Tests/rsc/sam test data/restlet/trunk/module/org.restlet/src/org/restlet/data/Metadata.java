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

package org.restlet.data;

/**
 * Description of data contained in a resource representation. "A representation consists of data,
 * metadata describing the data, and, on occasion, metadata to describe the metadata (usually for the purpose
 * of verifying message integrity). Metadata is in the form of name-value pairs, where the name corresponds to
 * a standard that defines the value's structure and semantics. Response messages may include both
 * representation metadata and resource metadata: information about the resource that is not specific to the
 * supplied representation." Roy T. Fielding
 * @see <a href="http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm#sec_5_2_1_2">Source
 * dissertation</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public class Metadata
{
	/** The metadata name like "text/html" or "compress" or "iso-8851-1". */
	private String name;

	/** The description of this metadata. */
	private String description;

	/**
	 * Constructor.
	 * @param name The unique name.
	 */
	public Metadata(String name)
	{
		this(name, null);
	}

	/**
	 * Constructor.
	 * @param name The unique name.
	 * @param description The description.
	 */
	public Metadata(String name, String description)
	{
		this.name = name;
		this.description = description;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object object)
	{
		return (object instanceof Metadata)
				&& ((Metadata) object).getName().equals(getName());
	}

	/**
	 * Returns the description.
	 * @return The description.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Returns the name (ex: "text/html" or "compress" or "iso-8851-1").
	 * @return The name (ex: "text/html" or "compress" or "iso-8851-1").
	 */
	public String getName()
	{
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode()
	{
		return (getName() == null) ? 0 : getName().hashCode();
	}

	/**
	 * Returns the metadata name.
	 * @return The metadata name.
	 */
	@Override
	public String toString()
	{
		return getName();
	}
}
