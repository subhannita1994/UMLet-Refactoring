/**
 * @author Mathieu Lemoine
 * @created 2008-08-18 (月)
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

package jct.test.rsc.jct.kernel;


import java.util.List;


/**
 * Interface representing an erroneous expression
 *
 * @author Mathieu Lemoine
 */
public interface IJCTErroneousExpression extends IJCTExpression, IJCTElementContainer<IJCTExpression>
{
    /**
     * Adds a "expression" at the index (or move it there)
     */
    public void addExpression(final int anIndex, final IJCTExpression aExpression);
    
    /**
     * Adds a "expression" at the end of the list (or move it there)
     */
    public void addExpression(final IJCTExpression aExpression);
    
    /**
     * Removes this expression from the list
     */
    public void removeExpression(final IJCTExpression aExpression);
    
    /**
     * Remove the expression at the index
     */
    public void removeExpression(final int anIndex);
    
    /**
     * Returns the list of expressions of this erroneous expression
     * <em>Part of the enclosed elements.</em>
     */
    public List<IJCTExpression> getExpressions();
    

}
