/*
 * (c) Copyright 2008 and following years, Julien Tanteri, University of
 * Montreal.
 * 
 * Use and copying of this software and preparation of derivative works based
 * upon this software are permitted. Any copy of this software or of any
 * derivative work must include the above copyright notice of the author, this
 * paragraph and the one after it.
 * 
 * This software is made available AS IS, and THE AUTHOR DISCLAIMS ALL
 * WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND NOT
 * WITHSTANDING ANY OTHER PROVISION CONTAINED HEREIN, ANY LIABILITY FOR DAMAGES
 * RESULTING FROM THE SOFTWARE OR ITS USE IS EXPRESSLY DISCLAIMED, WHETHER
 * ARISING IN CONTRACT, TORT (INCLUDING NEGLIGENCE) OR STRICT LIABILITY, EVEN IF
 * THE AUTHOR IS ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * All Rights Reserved.
 */
package jct.test.rsc.snpsht.parser.sax;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleSaxFsmParser extends DefaultHandler {
	public static final SAXParserFactory SAX_FACTORY =
		SAXParserFactory.newInstance();

	private AbstractStateSaxFsm state;
	private SAXParser parser;

	public SimpleSaxFsmParser() {
	}

	public AbstractStateSaxFsm parse(
		AbstractStateSaxFsm startState,
		String xmlFilePath) throws SAXException, IOException,
			ParserConfigurationException {

		File xmlFile = new File(xmlFilePath);

		return parse(startState, xmlFile);
	}

	public AbstractStateSaxFsm parse(
		AbstractStateSaxFsm startState,
		File xmlFile) throws SAXException, IOException,
			ParserConfigurationException {

		makeParser();

		changeState(startState);

		this.parser.parse(xmlFile, this);

		return this.state;
	}

	public AbstractStateSaxFsm getState() {
		return this.state;
	}

	public void changeState(AbstractStateSaxFsm state) {
		this.state = state;
	}

	public SAXParser makeParser() throws ParserConfigurationException,
			SAXException {

		if (this.parser == null) {
			this.parser = SAX_FACTORY.newSAXParser();
		}

		return this.parser;
	}

	public SAXParser getParser() {
		return this.parser;
	}

	/*
	 * Forwarded methods to current state
	 */

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		this.state.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		this.state.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		this.state.endElement(uri, localName, name);
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		this.state.error(e);
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		this.state.fatalError(e);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		this.state.endPrefixMapping(prefix);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		this.state.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		this.state.notationDecl(name, publicId, systemId);
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		this.state.processingInstruction(target, data);
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		this.state.setDocumentLocator(locator);
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		this.state.skippedEntity(name);
	}

	@Override
	public void startDocument() throws SAXException {
		this.state.startDocument();
	}

	@Override
	public void startElement(
		String uri,
		String localName,
		String name,
		Attributes attributes) throws SAXException {
		this.state.startElement(uri, localName, name, attributes);
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		this.state.startPrefixMapping(prefix, uri);
	}

	@Override
	public void unparsedEntityDecl(
		String name,
		String publicId,
		String systemId,
		String notationName) throws SAXException {
		this.state.unparsedEntityDecl(name, publicId, systemId, notationName);
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		this.state.warning(e);
	}
}