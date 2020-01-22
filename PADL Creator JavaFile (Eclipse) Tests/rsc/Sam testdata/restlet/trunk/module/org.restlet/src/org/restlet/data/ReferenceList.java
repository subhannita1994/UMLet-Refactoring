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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.util.WrapperList;

/**
 * List of URI references.
 * @author Jerome Louvel (contact@noelios.com)
 */
public class ReferenceList extends WrapperList<Reference>
{
	/** The list reference. */
	private Reference listRef;

	/**
	 * Constructor.
	 */
	public ReferenceList()
	{
		super();
	}

	/**
	 * Constructor.
	 * @param initialCapacity The initial list capacity.
	 */
	public ReferenceList(int initialCapacity)
	{
		super(new ArrayList<Reference>(initialCapacity));
	}

	/**
	 * Constructor.
	 * @param delegate The delegate list.
	 */
	public ReferenceList(List<Reference> delegate)
	{
		super(delegate);
	}

	/**
	 * Constructor from a "text/uri-list" representation.
	 * @param uriList The "text/uri-list" representation to parse.
	 * @throws IOException
	 */
	public ReferenceList(Representation uriList) throws IOException
	{
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new InputStreamReader(uriList.getStream()));

			String line = br.readLine();

			// Check if the list reference is specified as the first comment
			if ((line != null) && line.startsWith("#"))
			{
				setListRef(new Reference(line.substring(1).trim()));
				line = br.readLine();
			}

			while (line != null)
			{
				if (!line.startsWith("#"))
				{
					add(new Reference(line.trim()));
				}

				line = br.readLine();
			}
		}
		finally
		{
			if (br != null)
			{
				br.close();
			}
		}
	}

	/**
	 * Creates then adds a reference at the end of the list.
	 * @param uri The uri of the reference to add.
	 * @return True (as per the general contract of the Collection.add method).
	 */
	public boolean add(String uri)
	{
		return add(new Reference(uri));
	}

	/**
	 * Returns the list reference.
	 * @return The list reference.
	 */
	public Reference getListRef()
	{
		return this.listRef;
	}

	/**
	 * Returns a representation of the list in the "text/uri-list" format.
	 * @return A representation of the list in the "text/uri-list" format.
	 */
	public Representation getRepresentation()
	{
		StringBuilder sb = new StringBuilder();

		if (getListRef() != null)
		{
			sb.append("# ").append(getListRef().toString()).append("\r\n");
		}

		for (Reference ref : this)
		{
			sb.append(ref.toString()).append("\r\n");
		}

		return new StringRepresentation(sb.toString(), MediaType.TEXT_URI_LIST);
	}

	/**
	 * Sets the list reference.
	 * @param listRef The list reference.
	 */
	public void setListRef(Reference listRef)
	{
		this.listRef = listRef;
	}

	/**
	 * Sets the list reference.
	 * @param listUri The list reference as a URI.
	 */
	public void setListRef(String listUri)
	{
		setListRef(new Reference(listUri));
	}

	/**
	 * Returns a view of the portion of this list between the specified fromIndex,
	 * inclusive, and toIndex, exclusive.
	 * @param fromIndex The start position.
	 * @param toIndex The end position (exclusive).
	 * @return The sub-list.
	 */
	@Override
	public ReferenceList subList(int fromIndex, int toIndex)
	{
		return new ReferenceList(getDelegate().subList(fromIndex, toIndex));
	}

}
