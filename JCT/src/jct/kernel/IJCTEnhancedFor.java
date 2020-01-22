/**
 * @author Mathieu Lemoine
 * @created 2009-01-08 (木)
 *
 * Licensed under 3-clause BSD License:
 * Copyright © 2009, Mathieu Lemoine
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Mathieu Lemoine nor the
 *    names of contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY Mathieu Lemoine ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Mathieu Lemoine BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jct.kernel;

/**
 * This class represents an enhanced for loop
 * 
 * @author Mathieu Lemoine
 */
public interface IJCTEnhancedFor extends IJCTStatement,
		IJCTElementContainer<IJCTElement> {
	/**
	 * Modifies the variable of this enhanced for
	 * 
	 * @param variable
	 *            the new variable
	 */
	public void setVariable(final IJCTVariable variable);

	/**
	 * Returns the variable of this enhanced for
	 * <em>Included in the enclosed elements.</em>
	 */
	public IJCTVariable getVariable();

	/**
	 * Modifies the iterable of this enhanced for
	 * 
	 * @param iterable
	 *            the new iterable
	 */
	public void setIterable(final IJCTExpression iterable);

	/**
	 * Returns the iterable of this enhanced for
	 * <em>Included in the enclosed elements.</em>
	 */
	public IJCTExpression getIterable();

	/**
	 * Modifies the body of this enhanced for
	 * 
	 * @param body
	 *            the new body
	 */
	public void setBody(final IJCTStatement body);

	/**
	 * Returns the body of this enhanced for
	 * <em>Included in the enclosed elements.</em>
	 */
	public IJCTStatement getBody();

}