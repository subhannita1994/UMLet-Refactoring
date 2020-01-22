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

package org.restlet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.data.CharacterSet;
import org.restlet.data.Encoding;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.data.Tag;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.Result;

/**
 * Representation wrapper. Useful for application developer who need to enrich the representation
 * with application related properties and behavior.
 * @see <a href="http://c2.com/cgi/wiki?DecoratorPattern">The decorator (aka wrapper) pattern</a>
 * @author Jerome Louvel (contact@noelios.com)
 */
public class WrapperRepresentation extends Representation
{
	/** The wrapped representation. */
	private Representation wrappedRepresentation;

	/**
	 * Constructor.
	 * @param wrappedRepresentation The wrapped representation.
	 */
	public WrapperRepresentation(Representation wrappedRepresentation)
	{
		this.wrappedRepresentation = wrappedRepresentation;
	}

	/**
	 * Indicates if it is allowed to delete the resource. The default value is false. 
	 * @return True if the method is allowed.
	 */
	public boolean allowDelete()
	{
		return getWrappedRepresentation().allowDelete();
	}

	// ----------------
	// Resource methods
	// ----------------

	/**
	 * Indicates if it is allowed to get the variants. The default value is true. 
	 * @return True if the method is allowed.
	 */
	public boolean allowGet()
	{
		return getWrappedRepresentation().allowGet();
	}

	/**
	 * Indicates if it is allowed to post to the resource. The default value is false. 
	 * @return True if the method is allowed.
	 */
	public boolean allowPost()
	{
		return getWrappedRepresentation().allowPost();
	}

	/**
	 * Indicates if it is allowed to put to the resource. The default value is false. 
	 * @return True if the method is allowed.
	 */
	public boolean allowPut()
	{
		return getWrappedRepresentation().allowPut();
	}

	/**
	 * Asks the resource to delete itself and all its representations.
	 * @return The result information. 
	 */
	public Result delete()
	{
		return getWrappedRepresentation().delete();
	}

	/**
	 * Returns the list of methods allowed on the requested resource.
	 * @return The list of allowed methods.
	 */
	public List<Method> getAllowedMethods()
	{
		return getWrappedRepresentation().getAllowedMethods();
	}

	/**
	 * Returns a channel with the representation's content.<br/>
	 * If it is supported by a file, a read-only instance of FileChannel is returned.<br/>
	 * This method is ensured to return a fresh channel for each invocation unless it 
	 * is a transient representation, in which case null is returned.
	 * @return A channel with the representation's content.
	 * @throws IOException
	 */
	public ReadableByteChannel getChannel() throws IOException
	{
		return getWrappedRepresentation().getChannel();
	}

	/**
	 * Returns the character set or null if not applicable.
	 * @return The character set or null if not applicable.
	 */
	public CharacterSet getCharacterSet()
	{
		return getWrappedRepresentation().getCharacterSet();
	}

	/**
	 * Returns the encoding or null if identity encoding applies.
	 * @return The encoding or null if identity encoding applies.
	 */
	public Encoding getEncoding()
	{
		return getWrappedRepresentation().getEncoding();
	}

	/**
	 * Returns the future date when this representation expire. If this information is not known, returns null.
	 * @return The expiration date.
	 */
	public Date getExpirationDate()
	{
		return getWrappedRepresentation().getExpirationDate();
	}

	/**
	 * Returns the official identifier.
	 * @return The official identifier.
	 */
	public Reference getIdentifier()
	{
		return getWrappedRepresentation().getIdentifier();
	}

	/**
	 * Returns the list of all the identifiers for the resource. The list is composed of the official identifier
	 * followed by all the alias identifiers.
	 * @return The list of all the identifiers for the resource.
	 */
	public ReferenceList getIdentifiers()
	{
		return getWrappedRepresentation().getIdentifiers();
	}

	/**
	 * Returns the language or null if not applicable.
	 * @return The language or null if not applicable.
	 */
	public Language getLanguage()
	{
		return getWrappedRepresentation().getLanguage();
	}

	/**
	 * Returns the logger to use.
	 * @return The logger to use.
	 */
	public Logger getLogger()
	{
		return getWrappedRepresentation().getLogger();
	}

	/**
	 * Returns the media type.
	 * @return The media type.
	 */
	public MediaType getMediaType()
	{
		return getWrappedRepresentation().getMediaType();
	}

	/**
	 * Returns the last date when this representation was modified. If this information is not known, returns
	 * null.
	 * @return The modification date.
	 */
	public Date getModificationDate()
	{
		return getWrappedRepresentation().getModificationDate();
	}

	/**
	 * Returns the represented resource if available.
	 * @return The represented resource if available.
	 */
	public Resource getResource()
	{
		return getWrappedRepresentation().getResource();
	}

	/**
	 * Returns the size in bytes if known, UNKNOWN_SIZE (-1) otherwise.
	 * @return The size in bytes if known, UNKNOWN_SIZE (-1) otherwise.
	 */
	public long getSize()
	{
		return getWrappedRepresentation().getSize();
	}

	// ----------------------
	// Representation methods
	// ----------------------

	/**
	 * Returns a stream with the representation's content.
	 * This method is ensured to return a fresh stream for each invocation unless it 
	 * is a transient representation, in which case null is returned.
	 * @return A stream with the representation's content.
	 * @throws IOException
	 */
	public InputStream getStream() throws IOException
	{
		return getWrappedRepresentation().getStream();
	}

	/**
	 * Returns the tag.
	 * @return The tag.
	 */
	public Tag getTag()
	{
		return getWrappedRepresentation().getTag();
	}

	/**
	 * Converts the representation to a string value. Be careful when using this method as the conversion of 
	 * large content to a string fully stored in memory can result in OutOfMemoryErrors being thrown.
	 * @return The representation as a string value.
	 */
	public String getValue() throws IOException
	{
		return getWrappedRepresentation().getValue();
	}

	/**
	 * Returns the list of variants. Each variant is described by metadata and can provide several instances 
	 * of the variant's representation.
	 * @return The list of variants.
	 */
	public List<Representation> getVariants()
	{
		return (getWrappedRepresentation() == null) ? null : getWrappedRepresentation().getVariants();
	}

	/**
	 * Returns the wrapped representation.
	 * @return The wrapped representation.
	 */
	public Representation getWrappedRepresentation()
	{
		return this.wrappedRepresentation;
	}

	/**
	 * Indicates if some fresh content is available, without having to actually call one of the content
	 * manipulation method like getStream() that would actually consume it. This is especially useful for
	 * transient representation whose content can only be accessed once. 
	 * @return True if some fresh content is available.
	 */
	public boolean isAvailable()
	{
		return getWrappedRepresentation().isAvailable();
	}

	/**
	 * Indicates if the representation's content is transient, which means that it can 
	 * be obtained only once. This is often the case with representations transmitted
	 * via network sockets for example. In such case, if you need to read the content 
	 * several times, you need to cache it first, for example into memory or into a file.   
	 * @return True if the representation's content is transient.
	 */
	public boolean isTransient()
	{
		return getWrappedRepresentation().isTransient();
	}

	/**
	 * Posts a variant representation in the resource.
	 * @param entity The posted entity. 
	 * @return The result information.
	 */
	public Result post(Representation entity)
	{
		return getWrappedRepresentation().post(entity);
	}

	/**
	 * Puts a variant representation in the resource.
	 * @param variant A new or updated variant representation. 
	 * @return The result information.
	 */
	public Result put(Representation variant)
	{
		return getWrappedRepresentation().put(variant);
	}

	/**
	 * Indicates if some fresh content is available.
	 * @param isAvailable True if some fresh content is available.
	 */
	public void setAvailable(boolean isAvailable)
	{
		getWrappedRepresentation().setAvailable(isAvailable);
	}

	/**
	 * Sets the character set or null if not applicable.
	 * @param characterSet The character set or null if not applicable.
	 */
	public void setCharacterSet(CharacterSet characterSet)
	{
		getWrappedRepresentation().setCharacterSet(characterSet);
	}

	/**
	 * Sets the encoding or null if identity encoding applies.
	 * @param encoding The encoding or null if identity encoding applies.
	 */
	public void setEncoding(Encoding encoding)
	{
		getWrappedRepresentation().setEncoding(encoding);
	}

	/**
	 * Sets the future date when this representation expire. If this information is not known, pass null.
	 * @param expirationDate The expiration date.
	 */
	public void setExpirationDate(Date expirationDate)
	{
		getWrappedRepresentation().setExpirationDate(expirationDate);
	}

	/**
	 * Sets the official identifier.
	 * @param identifier The official identifier.
	 */
	public void setIdentifier(Reference identifier)
	{
		getWrappedRepresentation().setIdentifier(identifier);
	}

	/**
	 * Sets the official identifier from a URI string.
	 * @param identifierUri The official identifier to parse.
	 */
	public void setIdentifier(String identifierUri)
	{
		getWrappedRepresentation().setIdentifier(identifierUri);
	}

	/**
	 * Sets a new list of all the identifiers for the resource.  
	 * @param identifiers The new list of identifiers. 
	 */
	public void setIdentifiers(ReferenceList identifiers)
	{
		getWrappedRepresentation().setIdentifiers(identifiers);
	}

	/**
	 * Sets the language or null if not applicable.
	 * @param language The language or null if not applicable.
	 */
	public void setLanguage(Language language)
	{
		getWrappedRepresentation().setLanguage(language);
	}

	/**
	 * Sets the logger to use.
	 * @param logger The logger to use.
	 */
	public void setLogger(Logger logger)
	{
		getWrappedRepresentation().setLogger(logger);
	}

	/**
	 * Sets the media type.
	 * @param mediaType The media type.
	 */
	public void setMediaType(MediaType mediaType)
	{
		getWrappedRepresentation().setMediaType(mediaType);
	}

	/**
	 * Sets the last date when this representation was modified. If this information is not known, pass null.
	 * @param modificationDate The modification date.
	 */
	public void setModificationDate(Date modificationDate)
	{
		getWrappedRepresentation().setModificationDate(modificationDate);
	}

	/**
	 * Sets the represented resource.
	 * @param resource The represented resource.
	 */
	public void setResource(Resource resource)
	{
		getWrappedRepresentation().setResource(resource);
	}

	/**
	 * Sets the expected size in bytes if known, -1 otherwise.
	 * @param expectedSize The expected size in bytes if known, -1 otherwise.
	 */
	public void setSize(long expectedSize)
	{
		getWrappedRepresentation().setSize(expectedSize);
	}

	/**
	 * Sets the tag.
	 * @param tag The tag.
	 */
	public void setTag(Tag tag)
	{
		getWrappedRepresentation().setTag(tag);
	}

	/**
	 * Indicates if the representation's content is transient.
	 * @param isTransient True if the representation's content is transient.
	 */
	public void setTransient(boolean isTransient)
	{
		getWrappedRepresentation().setTransient(isTransient);
	}

	/**
	 * Sets a new list of variants. 
	 * @param variants The new list of variants.
	 */
	public void setVariants(List<Representation> variants)
	{
		getWrappedRepresentation().setVariants(variants);
	}

	/**
	 * Writes the representation to a byte stream.
	 * This method is ensured to write the full content for each invocation unless it 
	 * is a transient representation, in which case an exception is thrown.
	 * @param outputStream The output stream.
	 * @throws IOException
	 */
	public void write(OutputStream outputStream) throws IOException
	{
		getWrappedRepresentation().write(outputStream);
	}

	/**
	 * Writes the representation to a byte channel.
	 * This method is ensured to write the full content for each invocation unless it 
	 * is a transient representation, in which case an exception is thrown.
	 * @param writableChannel A writable byte channel.
	 * @throws IOException
	 */
	public void write(WritableByteChannel writableChannel) throws IOException
	{
		getWrappedRepresentation().write(writableChannel);
	}
}
