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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reference to a Uniform Resource Identifier (URI). Contrary to the java.net.URI 
 * class, this interface represents mutable references. It strictly conforms to the RFC 3986
 * specifying URIs and follow its naming conventions.<br/>
 * <pre>
 * 	URI reference        = absolute-reference | relative-reference
 * 
 * 	absolute-reference   = scheme ":" scheme-specific-part [ "#" fragment ]
 * 	scheme-specific-part = ( hierarchical-part [ "?" query ] ) | opaque-part
 * 	hierarchical-part    = ( "//" authority path-abempty ) | path-absolute | path-rootless | path-empty
 * 	authority            = [ user-info "@" ] host-domain [ ":" host-port ]
 * 
 * 	relative-reference   = relative-part [ "?" query ] [ "#" fragment ]
 * 	relative-part        = ( "//" authority path-abempty ) | path-absolute | path-noscheme | path-empty
 * 
 * 	path-abempty         = begins with "/" or is empty
 * 	path-absolute        = begins with "/" but not "//"
 * 	path-noscheme        = begins with a non-colon segment
 * 	path-rootless        = begins with a segment
 * 	path-empty           = zero characters
 * </pre>
 * Note that this class doesn't encode or decode the reserved characters. It assumes that the URIs or 
 * the URI parts passed in are properly encoded using the standard URI encoding mechanism. You can use
 * the static "encode()" and "decode()" methods for this purpose.  
 * @author Jerome Louvel (contact@noelios.com)
 * @see <a href="http://www.faqs.org/rfcs/rfc3986.html">RFC 3986</a>
 */
public class Reference
{
	/**
	 * Dencodes a given string using the standard URI encoding mechanism. 
	 * @param toDecode The string to decode.
	 * @return The decoded string.
	 */
	public static String decode(String toDecode)
	{
		String result = null;

		try
		{
			result = URLDecoder.decode(toDecode, "UTF-8");
		}
		catch (UnsupportedEncodingException uee)
		{
			Logger.getLogger(Reference.class.getCanonicalName()).log(Level.WARNING,
					"Unable to decode the string with the UTF-8 character set.", uee);
		}

		return result;
	}

	/**
	 * Encodes a given string using the standard URI encoding mechanism. 
	 * @param toEncode The string to encode.
	 * @return The encoded string.
	 */
	public static String encode(String toEncode)
	{
		String result = null;

		try
		{
			result = URLEncoder.encode(toEncode, "UTF-8");
		}
		catch (UnsupportedEncodingException uee)
		{
			Logger.getLogger(Reference.class.getCanonicalName()).log(Level.WARNING,
					"Unable to encode the string with the UTF-8 character set.", uee);
		}

		return result;
	}

	/**
	 * Creates a reference string from its parts.
	 * @param scheme The scheme ("http", "https" or "ftp").
	 * @param hostName The host name or IP address.
	 * @param hostPort The host port (default ports are correctly ignored).
	 * @param path The path component for hierarchical identifiers.
	 * @param query The optional query component for hierarchical identifiers.
	 * @param fragment The optionale fragment identifier.
	 */
	public static String toString(String scheme, String hostName, Integer hostPort,
			String path, String query, String fragment)
	{
		String host = hostName;

		// Append the host port number
		if (hostPort != null)
		{
			int defaultPort = Protocol.valueOf(scheme).getDefaultPort();
			if (hostPort != defaultPort)
			{
				host = hostName + ':' + hostPort;
			}
		}

		return toString(scheme, host, path, query, fragment);
	}

	/**
	 * Creates a relative reference string from its parts.
	 * @param relativePart The relative part component.
	 * @param query The optional query component for hierarchical identifiers.
	 * @param fragment The optionale fragment identifier.
	 */
	public static String toString(String relativePart, String query, String fragment)
	{
		StringBuilder sb = new StringBuilder();

		// Append the path
		if (relativePart != null)
		{
			sb.append(relativePart);
		}

		// Append the query string 
		if (query != null)
		{
			sb.append('?').append(query);
		}

		// Append the fragment identifier
		if (fragment != null)
		{
			sb.append('#').append(fragment);
		}

		// Actually construct the reference
		return sb.toString();
	}

	/**
	 * Creates a reference string from its parts.
	 * @param scheme The scheme ("http", "https" or "ftp").
	 * @param host The host name or IP address plus the optional port number.
	 * @param path The path component for hierarchical identifiers.
	 * @param query The optional query component for hierarchical identifiers.
	 * @param fragment The optionale fragment identifier.
	 */
	public static String toString(String scheme, String host, String path, String query,
			String fragment)
	{
		StringBuilder sb = new StringBuilder();

		if (scheme != null)
		{
			// Append the scheme and host name
			sb.append(scheme.toLowerCase()).append("://").append(host);
		}

		// Append the path
		if (path != null)
		{
			sb.append(path);
		}

		// Append the query string 
		if (query != null)
		{
			sb.append('?').append(query);
		}

		// Append the fragment identifier
		if (fragment != null)
		{
			sb.append('#').append(fragment);
		}

		// Actually construct the reference
		return sb.toString();
	}

	/** The base reference for relative references. */
	private Reference baseRef;

	/** The internal reference. */
	private String internalRef;

	/** The fragment separator index. */
	private int fragmentIndex;

	/** The query separator index. */
	private int queryIndex;

	/** The scheme separator index. */
	private int schemeIndex;

	/**
	 * Empty constructor.
	 */
	public Reference()
	{
		this.baseRef = null;
		this.internalRef = null;
		updateIndexes();
	}

	/**
	 * Clone constructor.
	 * @param ref The reference to clone.
	 */
	public Reference(Reference ref)
	{
		this(ref.baseRef, ref.internalRef);
	}

	/**
	 * Constructor from an URI reference (most likely relative).
	 * @param baseRef The base reference. 
	 * @param uriReference The URI reference, either absolute or relative.
	 */
	public Reference(Reference baseRef, String uriReference)
	{
		this.baseRef = baseRef;
		this.internalRef = uriReference;
		updateIndexes();
	}

	/**
	 * Constructor of relative reference from its parts.
	 * @param baseRef The base reference. 
	 * @param relativePart The relative part component (most of the time it is the path component).
	 * @param query The optional query component for hierarchical identifiers.
	 * @param fragment The optionale fragment identifier.
	 */
	public Reference(Reference baseRef, String relativePart, String query, String fragment)
	{
		this(baseRef, toString(relativePart, query, fragment));
	}

	/**
	 * Constructor from an URI reference.
	 * @param uriReference The URI reference, either absolute or relative.
	 */
	public Reference(String uriReference)
	{
		this((Reference) null, uriReference);
	}

	/**
	 * Constructor from an identifier and a fragment.
	 * @param identifier The resource identifier.
	 * @param fragment The fragment identifier.
	 */
	public Reference(String identifier, String fragment)
	{
		this((fragment == null) ? identifier : identifier + '#' + fragment);
	}

	/**
	 * Constructor of absolute reference from its parts.
	 * @param scheme The scheme ("http", "https" or "ftp").
	 * @param hostName The host name or IP address.
	 * @param hostPort The host port (default ports are correctly ignored).
	 * @param path The path component for hierarchical identifiers.
	 * @param query The optional query component for hierarchical identifiers.
	 * @param fragment The optionale fragment identifier.
	 */
	public Reference(String scheme, String hostName, int hostPort, String path,
			String query, String fragment)
	{
		this(toString(scheme, hostName, hostPort, path, query, fragment));
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param object The object to compare to.
	 * @return True if this object is the same as the obj argument. 
	 */
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Reference)
		{
			Reference ref = (Reference) object;
			if (this.internalRef == null)
			{
				return ref.internalRef == null;
			}
			else
			{
				return this.internalRef.equals(ref.internalRef);
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Returns the authority component for hierarchical identifiers.
	 * Includes the user info, host name and the host port number.
	 * @return The authority component for hierarchical identifiers.
	 */
	public String getAuthority()
	{
		String part = isRelative() ? getRelativePart() : getSchemeSpecificPart();

		if (part.startsWith("//"))
		{
			int index = part.indexOf('/', 2);

			if (index != -1)
			{
				return part.substring(2, index);
			}
			else
			{
				index = part.indexOf('?');
				if (index != -1)
				{
					return part.substring(2, index);
				}
				else
				{
					return part.substring(2);
				}
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the base reference for relative references.
	 * @return The base reference for relative references.
	 */
	public Reference getBaseRef()
	{
		return this.baseRef;
	}

	/**
	 * Returns the fragment identifier.
	 * @return The fragment identifier.
	 */
	public String getFragment()
	{
		if (fragmentIndex != -1)
		{
			return this.internalRef.substring(fragmentIndex + 1);
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns the hierarchical part which is equivalent to the scheme specific part less the query component.
	 * @return The hierarchical part .
	 */
	public String getHierarchicalPart()
	{
		if (schemeIndex != -1)
		{
			// Scheme found
			if (queryIndex != -1)
			{
				// Query found
				return this.internalRef.substring(schemeIndex + 1, queryIndex);
			}
			else
			{
				// No query found
				if (fragmentIndex != -1)
				{
					// Fragment found
					return this.internalRef.substring(schemeIndex + 1, fragmentIndex);
				}
				else
				{
					// No fragment found
					return this.internalRef.substring(schemeIndex + 1);
				}
			}
		}
		else
		{
			// No scheme found
			if (queryIndex != -1)
			{
				// Query found
				return this.internalRef.substring(0, queryIndex);
			}
			else
			{
				if (fragmentIndex != -1)
				{
					// Fragment found
					return this.internalRef.substring(0, fragmentIndex);
				}
				else
				{
					// No fragment found
					return this.internalRef;
				}
			}
		}
	}

	/**
	 * Returns the host domain name component for server based hierarchical identifiers. It can also be replaced
	 * by an IP address when no domain name was registered.
	 * @return The host domain name component for server based hierarchical identifiers.
	 */
	public String getHostDomain()
	{
		String result = null;
		String authority = getAuthority();

		if (authority != null)
		{
			int index1 = authority.indexOf('@');
			int index2 = authority.indexOf(':');

			if (index1 != -1)
			{
				// User info found
				if (index2 != -1)
				{
					// Port found
					result = authority.substring(index1 + 1, index2);
				}
				else
				{
					// No port found
					result = authority.substring(index1 + 1);
				}
			}
			else
			{
				// No user info found
				if (index2 != -1)
				{
					// Port found
					result = authority.substring(0, index2);
				}
				else
				{
					// No port found
					result = authority;
				}
			}
		}

		return result;
	}

	/**
	 * Returns the host identifier.
	 * Includes the scheme, the host name and the host port number.
	 * @return The host identifier.
	 */
	public String getHostIdentifier()
	{
		StringBuilder result = new StringBuilder();
		result.append(getScheme()).append("://").append(getAuthority());
		return result.toString();
	}

	/**
	 * Returns the optional port number for server based hierarchical identifiers.
	 * @return The optional port number for server based hierarchical identifiers.
	 */
	public Integer getHostPort()
	{
		Integer result = null;
		String authority = getAuthority();

		if (authority != null)
		{
			int index = authority.indexOf(':');

			if (index != -1)
			{
				result = Integer.valueOf(authority.substring(index + 1));
			}
		}

		return result;
	}

	/**
	 * Returns the absolute resource identifier, without the fragment.
	 * @return The absolute resource identifier, without the fragment.
	 */
	public String getIdentifier()
	{
		if (fragmentIndex != -1)
		{
			// Fragment found
			return this.internalRef.substring(0, fragmentIndex);
		}
		else
		{
			// No fragment found
			return this.internalRef;
		}
	}

	/**
	 * Returns the last segment of a hierarchical path.<br/>
	 * For example the "/a/b/c" and "/a/b/c/" paths have the same segments: "a", "b", "c.
	 * @return The last segment of a hierarchical path.
	 */
	public String getLastSegment()
	{
		String result = null;
		int lastSlash = getPath().lastIndexOf('/');

		if (lastSlash != -1)
		{
			result = getPath().substring(lastSlash + 1);
		}

		return result;
	}

	/**
	 * Returns the parent reference of a hierarchical reference. The last slash of the path will be considered
	 * as the end of the parent path.
	 * @return The parent reference of a hierarchical reference.
	 */
	public Reference getParentRef()
	{
		Reference result = null;

		if (isHierarchical())
		{
			String parentRef = null;
			String path = getPath();
			if (!path.equals("/") && !path.equals(""))
			{
				if (path.endsWith("/"))
				{
					path = path.substring(0, path.length() - 1);
				}

				parentRef = getHostIdentifier()
						+ path.substring(0, path.lastIndexOf('/') + 1);
			}
			else
			{
				parentRef = this.internalRef;
			}

			result = new Reference(parentRef);
		}

		return result;
	}

	/**
	 * Returns the path component for hierarchical identifiers.
	 * @return The path component for hierarchical identifiers.
	 */
	public String getPath()
	{
		String result = null;
		String part = isRelative() ? getRelativePart() : getSchemeSpecificPart();

		if (part != null)
		{
			if (part.startsWith("//"))
			{
				// Authority found
				int index1 = part.indexOf('/', 2);

				if (index1 != -1)
				{
					// Path found
					int index2 = part.indexOf('?');
					if (index2 != -1)
					{
						// Query found
						result = part.substring(index1, index2);
					}
					else
					{
						// No query found
						result = part.substring(index1);
					}
				}
				else
				{
					// Path must be empty in this case
				}
			}
			else
			{
				// No authority found
				int index = part.indexOf('?');
				if (index != -1)
				{
					// Query found
					result = part.substring(0, index);
				}
				else
				{
					// No query found
					result = part;
				}
			}
		}

		return result;
	}

	/**
	 * Returns the optional query component for hierarchical identifiers.
	 * @return The optional query component for hierarchical identifiers.
	 */
	public String getQuery()
	{
		if (queryIndex != -1)
		{
			// Query found
			if (fragmentIndex != -1)
			{
				// Fragment found
				return this.internalRef.substring(queryIndex + 1, fragmentIndex);
			}
			else
			{
				// No fragment found
				return this.internalRef.substring(queryIndex + 1);
			}
		}
		else
		{
			// No query found
			return null;
		}
	}

	/**
	 * Returns the optional query component as a form submission.
	 * @return The optional query component as a form submission.
	 * @throws IOException 
	 */
	public Form getQueryAsForm()
	{
		return new Form(getQuery());
	}

	/**
	 * Returns the relative part for relative references only.
	 * @return The relative part for relative references only.
	 */
	public String getRelativePart()
	{
		String result = null;

		if (schemeIndex == -1)
		{
			// This is a relative reference, no scheme found
			if (queryIndex != -1)
			{
				// Query found
				result = this.internalRef.substring(0, queryIndex);
			}
			else
			{
				if (fragmentIndex != -1)
				{
					// Fragment found
					result = this.internalRef.substring(0, fragmentIndex);
				}
				else
				{
					// No fragment found
					result = this.internalRef;
				}
			}
		}

		return result;
	}

	/**
	 * Returns the current reference relatively to a base reference.
	 * @param base The base reference to use.
	 * @return The current reference relatively to a base reference.
	 */
	public Reference getRelativeRef(Reference base)
	{
		Reference result = null;

		if (base == null)
		{
			result = this;
		}
		else if (!isAbsolute() || !isHierarchical())
		{
			throw new IllegalArgumentException(
					"The reference must have an absolute hierarchical path component");
		}
		else if (!base.isAbsolute() || !base.isHierarchical())
		{
			throw new IllegalArgumentException(
					"The base reference must have an absolute hierarchical path component");
		}
		else if (!getHostIdentifier().equals(base.getHostIdentifier()))
		{
			result = this;
		}
		else
		{
			String localPath = getPath();
			String basePath = base.getPath();
			String relativePath = null;

			if ((basePath == null) || (localPath == null))
			{
				relativePath = localPath;
			}
			else
			{
				// Find the junction point
				boolean diffFound = false;
				int lastSlashIndex = -1;
				int i = 0;
				char current;
				while (!diffFound && (i < localPath.length()) && (i < basePath.length()))
				{
					current = localPath.charAt(i);

					if (current != basePath.charAt(i))
					{
						diffFound = true;
					}
					else
					{
						if (current == '/') lastSlashIndex = i;
						i++;
					}
				}

				if (!diffFound)
				{
					if (localPath.length() == basePath.length())
					{
						// Both paths are strictely equivalent
						relativePath = ".";
					}
					else if (i == localPath.length())
					{
						// End of local path reached
						if (basePath.charAt(i) == '/')
						{
							if ((i + 1) == basePath.length())
							{
								// Both paths are strictely equivalent
								relativePath = ".";
							}
							else
							{
								// The local path is a direct parent of the base path
								// We need to add enough ".." in the relative path
								StringBuilder sb = new StringBuilder();
								sb.append("..");
								boolean canAdd = false;

								for (int j = i + 1; j < basePath.length(); j++)
								{
									if (basePath.charAt(j) == '/')
									{
										canAdd = true;
									}
									else if (canAdd)
									{
										sb.append("/..");
										canAdd = false;
									}
								}

								relativePath = sb.toString();
							}
						}
						else
						{
							// The base path has a segment that starts like the last local path segment 
							// But that is longer. Situation similar to a junction
							StringBuilder sb = new StringBuilder();
							boolean firstAdd = true;
							boolean canAdd = false;

							for (int j = i; j < basePath.length(); j++)
							{
								if (basePath.charAt(j) == '/')
								{
									canAdd = true;
								}
								else if (canAdd)
								{
									if (firstAdd)
									{
										firstAdd = false;
									}
									else
									{
										sb.append("/");
									}

									sb.append("..");
									canAdd = false;
								}
							}

							if (lastSlashIndex + 1 < localPath.length())
							{
								if (!firstAdd) sb.append('/');
								sb.append(localPath.substring(lastSlashIndex + 1));
							}

							relativePath = sb.toString();

							if (relativePath.equals("")) relativePath = ".";
						}
					}
					else if (i == basePath.length())
					{
						if (localPath.charAt(i) == '/')
						{
							if ((i + 1) == localPath.length())
							{
								// Both paths are strictely equivalent
								relativePath = ".";
							}
							else
							{
								// The local path is a direct child of the base path
								relativePath = localPath.substring(i + 1);
							}
						}
						else
						{
							if (lastSlashIndex == (i - 1))
							{
								// The local path is a direct subpath of the base path
								relativePath = localPath.substring(i);
							}
							else
							{
								relativePath = ".." + localPath.substring(lastSlashIndex);
							}
						}
					}
				}
				else
				{
					// We found a junction point,
					// we need to add enough ".." in the relative path
					// and append the rest of the local path
					// the local path is a direct subpath of the base path
					StringBuilder sb = new StringBuilder();
					boolean canAdd = false;
					boolean firstAdd = true;

					for (int j = i; j < basePath.length(); j++)
					{
						if (basePath.charAt(j) == '/')
						{
							canAdd = true;
						}
						else if (canAdd)
						{
							if (firstAdd)
							{
								firstAdd = false;
							}
							else
							{
								sb.append("/");
							}

							sb.append("..");
							canAdd = false;
						}
					}

					if (!firstAdd) sb.append('/');
					sb.append(localPath.substring(lastSlashIndex + 1));
					relativePath = sb.toString();
				}
			}

			// Builde the result reference
			result = new Reference();
			String query = getQuery();
			String fragment = getFragment();
			boolean modified = false;

			if ((query != null) && (!query.equals(base.getQuery())))
			{
				result.setQuery(query);
				modified = true;
			}

			if ((fragment != null) && (!fragment.equals(base.getFragment())))
			{
				result.setFragment(fragment);
				modified = true;
			}

			if (!modified || !relativePath.equals("."))
			{
				result.setPath(relativePath);
			}
		}

		return result;
	}

	/**
	 * Returns the scheme component.
	 * @return The scheme component.
	 */
	public String getScheme()
	{
		if (schemeIndex != -1)
		{
			// Scheme found
			return this.internalRef.substring(0, schemeIndex);
		}
		else
		{
			// No scheme found
			return null;
		}
	}

	/**
	 * Returns the protocol associated with the scheme component.
	 * @return The protocol associated with the scheme component.
	 */
	public Protocol getSchemeProtocol()
	{
		return Protocol.valueOf(getScheme());
	}

	/**
	 * Returns the scheme specific part.
	 * @return The scheme specific part.
	 */
	public String getSchemeSpecificPart()
	{
		String result = null;

		if (schemeIndex != -1)
		{
			// Scheme found
			if (fragmentIndex != -1)
			{
				// Fragment found
				result = this.internalRef.substring(schemeIndex + 1, fragmentIndex);
			}
			else
			{
				// No fragment found
				result = this.internalRef.substring(schemeIndex + 1);
			}
		}

		return result;
	}

	/**
	 * Returns the segments of a hierarchical path.<br/>
	 * A new list is created for each call.
	 * @return The segments of a hierarchical path.
	 */
	public List<String> getSegments()
	{
		List<String> result = new ArrayList<String>();
		;
		String path = getPath();
		int start = -2; // The index of the slash starting the segment  
		char current;

		if (path != null)
		{
			for (int i = 0; i < path.length(); i++)
			{
				current = path.charAt(i);

				if (current == '/')
				{
					if (start == -2)
					{
						// Beginning of an absolute path or sequence of two separators
						start = i;
					}
					else
					{
						// End of a segment
						result.add(path.substring(start + 1, i));
						start = i;
					}
				}
				else
				{
					if (start == -2)
					{
						// Starting a new segment for a relative path
						start = -1;
					}
					else
					{
						// Looking for the next character
					}
				}
			}

			if (start != -2)
			{
				// Add the last segment
				result.add(path.substring(start + 1));
			}
		}

		return result;
	}

	/**
	 * Returns the target reference. This method resolves relative references against the base reference
	 * then normalize them.
	 * @return The target reference. 
	 */
	public Reference getTargetRef()
	{
		Reference result = null;

		// Step 1 - Resolve relative reference against their base reference
		if (isRelative() && (baseRef != null))
		{
			// Relative URI detected
			String authority = getAuthority();
			String path = getPath();
			String query = getQuery();
			String fragment = getFragment();

			// Create an empty reference
			result = new Reference();
			result.setScheme(baseRef.getScheme());

			if (authority != null)
			{
				result.setAuthority(authority);
				result.setPath(path);
				result.setQuery(query);
			}
			else
			{
				result.setAuthority(baseRef.getAuthority());

				if ((path == null) || (path.equals("")))
				{
					result.setPath(baseRef.getPath());

					if (query != null)
					{
						result.setQuery(query);
					}
					else
					{
						result.setQuery(baseRef.getQuery());
					}
				}
				else
				{
					if (path.startsWith("/"))
					{
						result.setPath(path);
					}
					else
					{
						String basePath = baseRef.getPath();
						String mergedPath = null;

						if ((baseRef.getAuthority() != null)
								&& ((basePath == null) || (basePath.equals(""))))
						{
							mergedPath = "/" + path;
						}
						else
						{
							// Remove the last segment which may be empty if the path is ending with a slash
							int lastSlash = basePath.lastIndexOf('/');
							if (lastSlash == -1)
							{
								mergedPath = path;
							}
							else
							{
								mergedPath = basePath.substring(0, lastSlash + 1) + path;
							}
						}

						result.setPath(mergedPath);
					}

					result.setQuery(query);
				}
			}

			result.setFragment(fragment);
		}
		else
		{
			// Absolute URI detected
			result = new Reference(this);
		}

		// Step 2 - Normalize the target reference
		result.normalize();

		return result;
	}

	/**
	 * Returns the user info component for server based hierarchical identifiers.
	 * @return The user info component for server based hierarchical identifiers.
	 */
	public String getUserInfo()
	{
		String result = null;
		String authority = getAuthority();

		if (authority != null)
		{
			int index = authority.indexOf('@');

			if (index != -1)
			{
				result = authority.substring(0, index);
			}
		}

		return result;
	}

	/**
	 * Returns a hash code value for the object.
	 * @return A hash code value for the object.
	 */
	@Override
	public int hashCode()
	{
		return (this.internalRef == null) ? 0 : this.internalRef.hashCode();
	}

	/**
	 * Indicates if the reference is absolute.
	 * @return True if the reference is absolute.
	 */
	public boolean isAbsolute()
	{
		return (getScheme() != null);
	}

	/**
	 * Returns true if both reference are equivalent, meaning that they resolve to the same target reference.
	 * @param ref The reference to compare.
	 * @return True if both reference are equivalent.
	 */
	public boolean isEquivalentTo(Reference ref)
	{
		return getTargetRef().equals(ref.getTargetRef());
	}

	/**
	 * Indicates if the identifier is hierarchical.
	 * @return True if the identifier is hierarchical, false if it is opaque.
	 */
	public boolean isHierarchical()
	{
		return isRelative() || (getSchemeSpecificPart().charAt(0) == '/');
	}

	/**
	 * Indicates if the identifier is opaque.
	 * @return True if the identifier is opaque, false if it is hierarchical.
	 */
	public boolean isOpaque()
	{
		return isAbsolute() && (getSchemeSpecificPart().charAt(0) != '/');
	}

	/**
	 * Indicates if the reference is a parent of the hierarchical child reference.
	 * @param childRef The hierarchical reference.
	 * @return True if the reference is a parent of the hierarchical child reference.
	 */
	public boolean isParent(Reference childRef)
	{
		boolean result = false;

		if ((childRef != null) && (childRef.isHierarchical()))
		{
			result = childRef.toString(false, false).startsWith(toString(false, false));
		}

		return result;
	}

	/**
	 * Indicates if the reference is relative.
	 * @return True if the reference is relative.
	 */
	public boolean isRelative()
	{
		return (getScheme() == null);
	}

	/**
	 * Normalizes the reference. Useful before comparison between references or when building 
	 * a target reference from a base and a relative references.
	 * @return The current reference. 
	 */
	public Reference normalize()
	{
		// 1. The input buffer is initialized with the now-appended path components 
		//    and the output buffer is initialized to the empty string.
		StringBuilder output = new StringBuilder();
		StringBuilder input = new StringBuilder();
		String path = getPath();
		if (path != null) input.append(path);

		// 2. While the input buffer is not empty, loop as follows:
		while (input.length() > 0)
		{
			// A. If the input buffer begins with a prefix of "../" or "./",
			//    then remove that prefix from the input buffer; otherwise,
			if ((input.length() >= 3) && input.substring(0, 3).equals("../"))
			{
				input.delete(0, 3);
			}
			else if ((input.length() >= 2) && input.substring(0, 2).equals("./"))
			{
				input.delete(0, 2);
			}

			// B. if the input buffer begins with a prefix of "/./" or "/.",
			//    where "." is a complete path segment, then replace that
			//    prefix with "/" in the input buffer; otherwise,
			else if ((input.length() >= 3) && input.substring(0, 3).equals("/./"))
			{
				input.delete(0, 2);
			}
			else if ((input.length() == 2) && input.substring(0, 2).equals("/."))
			{
				input.delete(1, 2);
			}

			// C. if the input buffer begins with a prefix of "/../" or "/..",
			// where ".." is a complete path segment, then replace that
			// prefix with "/" in the input buffer and remove the last
			// segment and its preceding "/" (if any) from the output
			// buffer; otherwise,
			else if ((input.length() >= 4) && input.substring(0, 4).equals("/../"))
			{
				input.delete(0, 3);
				removeLastSegment(output);
			}
			else if ((input.length() == 3) && input.substring(0, 3).equals("/.."))
			{
				input.delete(1, 3);
				removeLastSegment(output);
			}

			// D.  if the input buffer consists only of "." or "..", then remove
			// that from the input buffer; otherwise,
			else if ((input.length() == 1) && input.substring(0, 1).equals("."))
			{
				input.delete(0, 1);
			}
			else if ((input.length() == 2) && input.substring(0, 2).equals(".."))
			{
				input.delete(0, 2);
			}

			// E.  move the first path segment in the input buffer to the end of
			// the output buffer, including the initial "/" character (if
			// any) and any subsequent characters up to, but not including,
			// the next "/" character or the end of the input buffer.   		
			else
			{
				int max = -1;
				for (int i = 1; (max == -1) && (i < input.length()); i++)
				{
					if (input.charAt(i) == '/') max = i;
				}

				if (max != -1)
				{
					// We found the next "/" character.
					output.append(input.substring(0, max));
					input.delete(0, max);
				}
				else
				{
					// End of input buffer reached
					output.append(input);
					input.delete(0, input.length());
				}
			}
		}

		// Finally, the output buffer is returned as the result
		setPath(output.toString());

		// Ensure that the scheme and host names are reset in lower case
		setScheme(getScheme());
		setHostDomain(getHostDomain());

		return this;
	}

	/**
	 * Removes the last segement from the output builder.
	 * @param output The output builder to update.
	 */
	private void removeLastSegment(StringBuilder output)
	{
		int min = -1;
		for (int i = output.length() - 1; (min == -1) && (i >= 0); i--)
		{
			if (output.charAt(i) == '/') min = i;
		}

		if (min != -1)
		{
			// We found the previous "/" character.
			output.delete(min, output.length());
		}
		else
		{
			// End of output buffer reached
			output.delete(0, output.length());
		}

	}

	/**
	 * Sets the authority component for hierarchical identifiers.
	 * @param authority The authority component for hierarchical identifiers.
	 */
	public void setAuthority(String authority)
	{
		String oldPart = isRelative() ? getRelativePart() : getSchemeSpecificPart();
		String newPart;
		String newAuthority = (authority == null) ? "" : "//" + authority;

		if (oldPart.startsWith("//"))
		{
			int index = oldPart.indexOf('/', 2);

			if (index != -1)
			{
				newPart = newAuthority + oldPart.substring(index);
			}
			else
			{
				index = oldPart.indexOf('?');
				if (index != -1)
				{
					newPart = newAuthority + oldPart.substring(index);
				}
				else
				{
					newPart = newAuthority;
				}
			}
		}
		else
		{
			newPart = newAuthority + oldPart;
		}

		if (isAbsolute())
		{
			setSchemeSpecificPart(newPart);
		}
		else
		{
			setRelativePart(newPart);
		}
	}

	/**
	 * Sets the base reference for relative references.
	 * @param baseRef The base reference for relative references.
	 */
	public void setBaseRef(Reference baseRef)
	{
		this.baseRef = baseRef;
	}

	/**
	 * Sets the fragment identifier.
	 * @param fragment The fragment identifier.
	 */
	public void setFragment(String fragment)
	{
		if ((fragment != null) && (fragment.indexOf('#') != -1))
		{
			throw new IllegalArgumentException("Illegal '#' character detected in parameter");
		}
		else
		{
			if (fragmentIndex != -1)
			{
				// Existing fragment
				if (fragment != null)
				{
					this.internalRef = this.internalRef.substring(0, fragmentIndex + 1)
							+ fragment;
				}
				else
				{
					this.internalRef = this.internalRef.substring(0, fragmentIndex);
				}
			}
			else
			{
				// No existing fragment
				if (fragment != null)
				{
					if (this.internalRef != null)
					{
						this.internalRef = this.internalRef + '#' + fragment;
					}
					else
					{
						this.internalRef = '#' + fragment;
					}
				}
				else
				{
					// Do nothing
				}
			}
		}

		updateIndexes();
	}

	/**
	 * Sets the host domain component for server based hierarchical identifiers.
	 * @param domain The host component for server based hierarchical identifiers.
	 */
	public void setHostDomain(String domain)
	{
		String authority = getAuthority();

		if (authority != null)
		{
			if (domain == null)
			{
				domain = "";
			}
			else
			{
				// URI specification indicates that host names should be produced in lower case
				domain = domain.toLowerCase();
			}

			int index1 = authority.indexOf('@');
			int index2 = authority.indexOf(':');

			if (index1 != -1)
			{
				// User info found
				if (index2 != -1)
				{
					// Port found
					setAuthority(authority.substring(0, index1 + 1) + domain
							+ authority.substring(index2));
				}
				else
				{
					// No port found
					setAuthority(authority.substring(0, index1 + 1) + domain);
				}
			}
			else
			{
				// No user info found
				if (index2 != -1)
				{
					// Port found
					setAuthority(domain + authority.substring(index2));
				}
				else
				{
					// No port found
					setAuthority(domain);
				}
			}
		}
	}

	/**
	 * Sets the optional port number for server based hierarchical identifiers.
	 * @param port The optional port number for server based hierarchical identifiers.
	 */
	public void setHostPort(Integer port)
	{
		String authority = getAuthority();

		if (authority != null)
		{
			int index = authority.indexOf(':');
			String newPort = (port == null) ? "" : ":" + port;

			if (index != -1)
			{
				setAuthority(authority.substring(0, index) + newPort);
			}
			else
			{
				setAuthority(authority + newPort);
			}
		}
		else
		{
			throw new IllegalArgumentException(
					"No authority defined, please define a host name first");
		}
	}

	/**
	 * Sets the absolute resource identifier.
	 * @param identifier The absolute resource identifier.
	 */
	public void setIdentifier(String identifier)
	{
		if (identifier == null) identifier = "";
		if (identifier.indexOf('#') == -1)
		{
			throw new IllegalArgumentException("Illegal '#' character detected in parameter");
		}
		else
		{
			if (fragmentIndex != -1)
			{
				// Fragment found
				this.internalRef = identifier + this.internalRef.substring(fragmentIndex);
			}
			else
			{
				// No fragment found
				this.internalRef = identifier;
			}

			updateIndexes();
		}
	}

	/**
	 * Sets the path component for hierarchical identifiers.
	 * @param path The path component for hierarchical identifiers.
	 */
	public void setPath(String path)
	{
		String oldPart = isRelative() ? getRelativePart() : getSchemeSpecificPart();
		String newPart = null;

		if (oldPart != null)
		{
			if (path == null) path = "";

			if (oldPart.startsWith("//"))
			{
				// Authority found
				int index1 = oldPart.indexOf('/', 2);

				if (index1 != -1)
				{
					// Path found
					int index2 = oldPart.indexOf('?');
					if (index2 != -1)
					{
						// Query found
						newPart = oldPart.substring(0, index1) + path
								+ oldPart.substring(index2);
					}
					else
					{
						// No query found
						newPart = oldPart.substring(0, index1) + path;
					}
				}
				else
				{
					// No path found
					int index2 = oldPart.indexOf('?');
					if (index2 != -1)
					{
						// Query found
						newPart = oldPart.substring(0, index2) + path
								+ oldPart.substring(index2);
					}
					else
					{
						// No query found
						newPart = oldPart + path;
					}
				}
			}
			else
			{
				// No authority found
				int index = oldPart.indexOf('?');
				if (index != -1)
				{
					// Query found
					newPart = path + oldPart.substring(index);
				}
				else
				{
					// No query found
					newPart = path;
				}
			}
		}
		else
		{
			newPart = path;
		}

		if (isAbsolute())
		{
			setSchemeSpecificPart(newPart);
		}
		else
		{
			setRelativePart(newPart);
		}
	}

	/**
	 * Sets the scheme component based on this protocol.
	 * @param protocol The protocol of the scheme component.
	 */
	public void setProtocol(Protocol protocol)
	{
		setScheme(protocol.getSchemeName());
	}

	/**
	 * Returns the query component for hierarchical identifiers.
	 * @param query The query component for hierarchical identifiers.
	 */
	public void setQuery(String query)
	{
		if (queryIndex != -1)
		{
			// Query found
			if (fragmentIndex != -1)
			{
				// Fragment found
				if (query != null)
				{
					this.internalRef = this.internalRef.substring(0, queryIndex + 1) + query
							+ this.internalRef.substring(fragmentIndex);
				}
				else
				{
					this.internalRef = this.internalRef.substring(0, queryIndex)
							+ this.internalRef.substring(fragmentIndex);
				}
			}
			else
			{
				// No fragment found
				if (query != null)
				{
					this.internalRef = this.internalRef.substring(0, queryIndex + 1) + query;
				}
				else
				{
					this.internalRef = this.internalRef.substring(0, queryIndex);
				}
			}
		}
		else
		{
			// No query found
			if (fragmentIndex != -1)
			{
				// Fragment found
				if (query != null)
				{
					this.internalRef = this.internalRef.substring(0, fragmentIndex) + '?'
							+ query + this.internalRef.substring(fragmentIndex);
				}
				else
				{
					// Do nothing;
				}
			}
			else
			{
				// No fragment found
				if (query != null)
				{
					if (this.internalRef != null)
					{
						this.internalRef = this.internalRef + '?' + query;
					}
					else
					{
						this.internalRef = '?' + query;
					}
				}
				else
				{
					// Do nothing;
				}
			}
		}

		updateIndexes();
	}

	/**
	 * Sets the relative part for relative references only.
	 * @param relativePart The relative part to set.
	 */
	public void setRelativePart(String relativePart)
	{
		if (relativePart == null) relativePart = "";
		if (schemeIndex == -1)
		{
			// This is a relative reference, no scheme found
			if (queryIndex != -1)
			{
				// Query found
				this.internalRef = relativePart + this.internalRef.substring(queryIndex);
			}
			else if (fragmentIndex != -1)
			{
				// Fragment found
				this.internalRef = relativePart + this.internalRef.substring(fragmentIndex);
			}
			else
			{
				// No fragment found
				this.internalRef = relativePart;
			}
		}

		updateIndexes();
	}

	/**
	 * Sets the scheme component.
	 * @param scheme The scheme component.
	 */
	public void setScheme(String scheme)
	{
		if (scheme != null)
		{
			// URI specification indicates that scheme names should be produced in lower case
			scheme = scheme.toLowerCase();
		}

		if (schemeIndex != -1)
		{
			// Scheme found
			if (scheme != null)
			{
				this.internalRef = scheme + this.internalRef.substring(schemeIndex);
			}
			else
			{
				this.internalRef = this.internalRef.substring(schemeIndex + 1);
			}
		}
		else
		{
			// No scheme found
			if (scheme != null)
			{
				if (this.internalRef == null)
				{
					this.internalRef = scheme + ':';
				}
				else
				{
					this.internalRef = scheme + ':' + this.internalRef;
				}
			}
		}

		updateIndexes();
	}

	/**
	 * Sets the scheme specific part.
	 * @param schemeSpecificPart The scheme specific part.
	 */
	public void setSchemeSpecificPart(String schemeSpecificPart)
	{
		if (schemeSpecificPart == null) schemeSpecificPart = "";
		if (schemeIndex != -1)
		{
			// Scheme found
			if (fragmentIndex != -1)
			{
				// Fragment found
				this.internalRef = this.internalRef.substring(0, schemeIndex + 1)
						+ schemeSpecificPart + this.internalRef.substring(fragmentIndex);
			}
			else
			{
				// No fragment found
				this.internalRef = this.internalRef.substring(0, schemeIndex + 1)
						+ schemeSpecificPart;
			}
		}
		else
		{
			// No scheme found
			if (fragmentIndex != -1)
			{
				// Fragment found
				this.internalRef = schemeSpecificPart
						+ this.internalRef.substring(fragmentIndex);
			}
			else
			{
				// No fragment found
				this.internalRef = schemeSpecificPart;
			}
		}

		updateIndexes();
	}

	/**
	 * Sets the segments of a hierarchical path.<br/>
	 * A new absolute path will replace any existing one.
	 * @param segments The segments of the hierarchical path.
	 */
	public void setSegments(List<String> segments)
	{
		StringBuilder sb = new StringBuilder();
		for (String segment : segments)
		{
			sb.append('/').append(segment);
		}
		setPath(sb.toString());
	}

	/**
	 * Sets the user info component for server based hierarchical identifiers.
	 * @param userInfo The user info component for server based hierarchical identifiers.
	 */
	public void setUserInfo(String userInfo)
	{
		String authority = getAuthority();

		if (authority != null)
		{
			int index = authority.indexOf('@');
			String newUserInfo = (userInfo == null) ? "" : userInfo + '@';

			if (index != -1)
			{
				setAuthority(newUserInfo + authority.substring(index + 1));
			}
			else
			{
				setAuthority(newUserInfo + authority);
			}
		}
		else
		{
			throw new IllegalArgumentException(
					"No authority defined, please define a host name first");
		}
	}

	/**
	 * Returns the reference as an URI string.
	 * @return The reference as an URI string.
	 */
	@Override
	public String toString()
	{
		return this.internalRef;
	}

	/**
	 * Returns the URI reference string.
	 * @param query Indicates if the query should be included;
	 * @param fragment Indicates if the fragment should be included;
	 * @return The URI reference string.
	 */
	public String toString(boolean query, boolean fragment)
	{
		if (query)
		{
			if (fragment)
			{
				return this.internalRef;
			}
			else
			{
				if (fragmentIndex != -1)
				{
					return this.internalRef.substring(0, fragmentIndex);
				}
				else
				{
					return this.internalRef;
				}
			}
		}
		else
		{
			if (fragment)
			{
				if (queryIndex != -1)
				{
					if (fragmentIndex != -1)
					{
						return this.internalRef.substring(0, queryIndex) + "#" + getFragment();
					}
					else
					{
						return this.internalRef.substring(0, queryIndex);
					}
				}
				else
				{
					return this.internalRef;
				}
			}
			else
			{
				if (queryIndex != -1)
				{
					return this.internalRef.substring(0, queryIndex);
				}
				else
				{
					return this.internalRef;
				}
			}
		}
	}

	/**
	 * Update internal indexes.
	 */
	private void updateIndexes()
	{
		if (internalRef != null)
		{
			int firstSlashIndex = this.internalRef.indexOf('/');
			this.schemeIndex = this.internalRef.indexOf(':');

			if ((firstSlashIndex != -1) && (this.schemeIndex > firstSlashIndex))
			{
				// We are in the rare case of a relative reference where one of the path segments 
				// contains a colon character. In this case, we ignore the colon as a valid scheme index.
				// Note that this colon can't be in the first segment as it is forbidden by the URI RFC.
				this.schemeIndex = -1;
			}

			this.queryIndex = this.internalRef.indexOf('?');
			this.fragmentIndex = this.internalRef.indexOf('#');
		}
		else
		{
			this.schemeIndex = -1;
			this.queryIndex = -1;
			this.fragmentIndex = -1;
		}
	}

}
