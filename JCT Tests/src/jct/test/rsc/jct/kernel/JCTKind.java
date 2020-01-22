/**
 * @author Mathieu Lemoine
 * @created 2008-08-17 (日)
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

/**
 * Enum containing all the kinds of elements in a Javac AST
 *
 * @author Mathieu Lemoine
 */
public enum JCTKind 
{
    FIELD,
    PARAMETER,
    VARIABLE,
    ERRONEOUS_SELECTOR,
    SIMPLE_SELECTOR,
    SIMPLE_IDENTIFIER,
    ROOT_NODE,
    PRIMITIVE_TYPE,
    ARRAY_TYPE,
    CLASS_TYPE,
    INTERSECTION_TYPE,
    PACKAGE,
    COMPILATION_UNIT,
    COMMENT,
    IMPORT,
    CLASS,
    METHOD,
    METHOD_INVOCATION,
    BLOCK,
    ASSERT,
    BREAK,
    CONTINUE,
    LABEL,
    DO_WHILE,
    WHILE,
    FOR,
    ENHANCED_FOR,
    IF,
    RETURN,
    SWITCH,
    CASE,
    SYNCHRONIZED,
    THROW,
    TRY,
    CATCH,
    ARRAY_ACCESS,
    EXPRESSION_STATEMENT,
    ASSIGNMENT,
    AND_ASSIGNMENT,
    DIVIDE_ASSIGNMENT,
    LEFT_SHIFT_ASSIGNMENT,
    MINUS_ASSIGNMENT,
    MULTIPLY_ASSIGNMENT,
    OR_ASSIGNMENT,
    PLUS_ASSIGNMENT,
    REMAINDER_ASSIGNMENT,
    RIGHT_SHIFT_ASSIGNMENT,
    UNSIGNED_RIGHT_SHIFT_ASSIGNMENT,
    XOR_ASSIGNMENT,
    BITWISE_COMPLEMENT,
    UNARY_MINUS,
    UNARY_PLUS,
    PREFIX_DECREMENT,
    PREFIX_INCREMENT,
    POSTFIX_DECREMENT,
    POSTFIX_INCREMENT,
    LOGICAL_COMPLEMENT,
    EQUAL_TO,
    NOT_EQUAL_TO,
    CONDITIONAL_AND,
    CONDITIONAL_OR,
    LESS_THAN,
    LESS_THAN_OR_EQUAL,
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL,
    AND,
    DIVIDE,
    LEFT_SHIFT,
    MINUS,
    MULTIPLY,
    OR,
    PLUS,
    REMAINDER,
    RIGHT_SHIFT,
    UNSIGNED_RIGHT_SHIFT,
    XOR,
    CONDITIONAL_OPERATOR,
    INSTANCE_OF,
    CAST,
    NEW_ARRAY,
    NEW_CLASS,
    BOOLEAN_LITERAL,
    DOUBLE_LITERAL,
    FLOAT_LITERAL,
    INTEGER_LITERAL,
    LONG_LITERAL,
    CHARACTER_LITERAL,
    STRING_LITERAL,
    NULL_LITERAL,
    PARENTHESIS,
    EMPTY_STATEMENT,
    MEMBER_SELECTOR,
    ERRONEOUS_EXPRESSION;

}
