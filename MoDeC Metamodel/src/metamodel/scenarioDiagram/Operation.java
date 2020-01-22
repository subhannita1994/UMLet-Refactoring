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
package metamodel.scenarioDiagram;

import java.util.Iterator;
import java.util.List;

public class Operation extends metamodel.scenarioDiagram.Message {

	/**
	 * @uml.property  name="name"
	 */
	private String name = "";

	/**
	 * Getter of the property <tt>name</tt>
	 * @return  Returns the name.
	 * @uml.property  name="name"
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Setter of the property <tt>name</tt>
	 * @param name  The name to set.
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	// =============================

	//	/**
	//	 * Constructor
	//	 * @param sd
	//	 * @param header
	//	 * @param info
	//	 */	
	//	public Operation(ScenarioDiagram sd, int header, String info)
	//	{
	//		super(sd, header, info);	
	//	}

	/**
	 * Constructor
	 * @param sd
	 * @param header
	 * @param info
	 */
	public Operation(
		String signature,
		List arguments,
		Classifier sourceClassifier,
		Classifier destinationClassifier) {
		super(signature, arguments, sourceClassifier, destinationClassifier);
	}

	public String toString() {
		String info = super.toString() + "<OPERATION>" + this.signature + " (";
		Iterator lt = (Iterator) this.arguments.iterator();
		while (lt.hasNext())
			info += (Argument) lt.next() + ", ";

		if (info.lastIndexOf(",") != -1)
			info = info.substring(0, info.lastIndexOf(","));

		return info
			+ ") CALLEE "
			+ this.destinationClassifier
			+ " CALLER "
			+ this.sourceClassifier
			+ "\n";
	}
	
	

	//	public Operation (String info)
	//	{
	//		super(info);
	//	}

	//	public String toString()
	//	{
	//		String tab = "";
	//		
	//		for(int i = 0 ; i < level ; i++)
	//			tab += "\t";
	//		//tab = "{level " + level + "} " + tab + "<OPERATION> ";
	//		//return tab + originalTraceStatement + "\n" ;
	//		return tab + this.getIndex() + "<OPERATION> "+ originalTraceStatement + "\n" ;				
	//	}
	//	
	
	
	
}
