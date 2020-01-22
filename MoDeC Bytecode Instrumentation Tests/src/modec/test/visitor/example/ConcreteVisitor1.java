/*******************************************************************************
 * Copyright (c) 2001-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
package modec.test.visitor.example;

/**
 * @(#)Text4.java
 *
 *
 * @author 
 * @version 1.00 2007/3/16
 */


public class ConcreteVisitor1 implements Visitor {   
    
    public void visitConcreteElementA(ConcreteElementA ceA)
    {
    	System.out.println(ceA.getName());     		
    }
    
    public void visitConcreteElementB(ConcreteElementB ceB)
    {
    	System.out.println(ceB.getName());
    }   
}
