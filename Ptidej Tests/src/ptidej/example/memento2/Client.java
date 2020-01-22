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
package ptidej.example.memento2;

public class Client {
	public static void main(final String[] args) {
		final Client client = new Client();

		final Document document = new Document();
		document.addElement(new Title());
		document.addElement(new Paragraph());

		// Remember previous state. The nice thing about this
		// implementation is that the internal state of the
		// Document class is never exposed, not even through the
		// DocumentMemento interface, beacause a private inner class
		// realises the concrete implementation of the memento.
		client.setDocumentMemento(document.createMemento());

		document.addElement(new Title());
		document.addElement(new ParaIndent());
		document.addElement(new ParaIndent());

		System.out
			.println("-- FIRST PRINTALL -------------------------------------");
		document.printAll();
		System.out
			.println("-- SECOND PRINTALL AFTER UNDO -------------------------");
		document.setMemento(client.getDocumentMemento());
		document.printAll();
	}
	private DocumentMemento previousDocumentMemento;
	public DocumentMemento getDocumentMemento() {
		return this.previousDocumentMemento;
	}
	public void setDocumentMemento(final DocumentMemento documentMemento) {
		this.previousDocumentMemento = documentMemento;
	}
}
