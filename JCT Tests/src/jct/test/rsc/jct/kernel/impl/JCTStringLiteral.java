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

package jct.test.rsc.jct.kernel.impl;


import java.io.Writer;
import java.io.IOException;

import jct.test.rsc.jct.kernel.Constants;
import jct.test.rsc.jct.kernel.IJCTClassType;
import jct.test.rsc.jct.kernel.IJCTRootNode;
import jct.test.rsc.jct.kernel.IJCTStringLiteral;
import jct.test.rsc.jct.kernel.IJCTType;
import jct.test.rsc.jct.kernel.IJCTVisitor;
import jct.test.rsc.jct.kernel.JCTKind;


/**
 * This class represents a string literal
 * 
 * Default implementation for {@link jct.test.rsc.jct.kernel.IJCTStringLiteral}
 *
 * @author Mathieu Lemoine
 */
class JCTStringLiteral extends JCTSourceCodePart implements IJCTStringLiteral
{
    /**
     * value of this string literal
     */
    private String value;
    
    
    JCTStringLiteral(final IJCTRootNode aRootNode, final String value)
    {
        super(aRootNode);
        this.value = value;
    }
    
    @Override
	public Writer getSourceCode(final Writer aWriter) throws IOException
    {
        return aWriter.append("\"" + this.value.replace("\\", "\\").replace("\"", "\\\"") + "\"");
    }
    
    @Override
	public IJCTType getTypeResult()
    {
        return this.getRootNode().getType(Constants.CLASS_BINARYNAME_STRING, IJCTClassType.class);
    }
    
    /**
     * Modifies the value of this string literal
     *
     * @param value the new value
     */
    @Override
	public void setValue(final String value)
    {
        this.value = value;
    }
    
    /**
     * Returns the value of this string literal
     */
    @Override
	public String getValue()
    {
        return this.value;
    }
    
    /**
     * Returns the kind of this constituent (JCTKind.STRING_LITERAL)
     */
    @Override
	public JCTKind getKind()
    {
        return JCTKind.STRING_LITERAL;
    }
    
    /**
     * Calls the appropriate visit* method on the visitor
     */
    @Override
	public <R, P> R accept(final IJCTVisitor<R, P> visitor, final P aP)
    {
        return visitor.visitStringLiteral(this, aP);
    }
    

}
