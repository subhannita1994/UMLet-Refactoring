/*******************************************************************************
 * Copyright (c) 2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/
//
// Generated by JTB 1.2.2
//

package util.parser.java.v14.nodes;

/**
 * Grammar production:
 * f0 -> "for"
 * f1 -> "("
 * f2 -> [ ForInit() ]
 * f3 -> ";"
 * f4 -> [ Expression() ]
 * f5 -> ";"
 * f6 -> [ ForUpdate() ]
 * f7 -> ")"
 * f8 -> Statement()
 */
public class ForStatement implements Node {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public NodeToken f0;
   public NodeToken f1;
   public NodeOptional f2;
   public NodeToken f3;
   public NodeOptional f4;
   public NodeToken f5;
   public NodeOptional f6;
   public NodeToken f7;
   public Statement f8;

   public ForStatement(NodeToken n0, NodeToken n1, NodeOptional n2, NodeToken n3, NodeOptional n4, NodeToken n5, NodeOptional n6, NodeToken n7, Statement n8) {
      this.f0 = n0;
      this.f1 = n1;
      this.f2 = n2;
      this.f3 = n3;
      this.f4 = n4;
      this.f5 = n5;
      this.f6 = n6;
      this.f7 = n7;
      this.f8 = n8;
   }

   public ForStatement(NodeOptional n0, NodeOptional n1, NodeOptional n2, Statement n3) {
      this.f0 = new NodeToken("for");
      this.f1 = new NodeToken("(");
      this.f2 = n0;
      this.f3 = new NodeToken(";");
      this.f4 = n1;
      this.f5 = new NodeToken(";");
      this.f6 = n2;
      this.f7 = new NodeToken(")");
      this.f8 = n3;
   }

   public void accept(util.parser.java.v14.visitors.Visitor v) {
      v.visit(this);
   }
   public Object accept(util.parser.java.v14.visitors.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

