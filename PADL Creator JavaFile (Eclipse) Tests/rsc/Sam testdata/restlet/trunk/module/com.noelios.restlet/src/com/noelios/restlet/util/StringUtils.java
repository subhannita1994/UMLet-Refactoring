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

package com.noelios.restlet.util;

/**
 * String manipulation utilities.
 * @author Jerome Louvel (contact@noelios.com)
 */
public class StringUtils
{
	/**
	 * Strips a delimiter character from both ends of the source string.
	 * @param source The source string to strip.
	 * @param delimiter The character to remove.
	 * @return The stripped string.
	 */
	public static String strip(String source, char delimiter)
	{
		return strip(source, delimiter, true, true);
	}

	/**
	 * Strips a delimiter character from a source string.
	 * @param source The source string to strip.
	 * @param delimiter The character to remove.
	 * @param start Indicates if start of source should be stripped.
	 * @param end Indicates if end of source should be stripped.
	 * @return The stripped source string.
	 */
	public static String strip(String source, char delimiter, boolean start, boolean end)
	{
		int beginIndex = 0;
		int endIndex = source.length();
		boolean stripping = true;

		// Strip beginning
		while (stripping && (beginIndex < endIndex))
		{
			if (source.charAt(beginIndex) == delimiter)
			{
				beginIndex++;
			}
			else
			{
				stripping = false;
			}
		}

		// Strip end
		stripping = true;
		while (stripping && (beginIndex < endIndex - 1))
		{
			if (source.charAt(endIndex - 1) == delimiter)
			{
				endIndex--;
			}
			else
			{
				stripping = false;
			}
		}

		return source.substring(beginIndex, endIndex);
	}

}
