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
package jct.test.rsc.snpsht.parser.verfilesystem.cvsimpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jct.test.rsc.snpsht.parser.verfilesystem.AbstractVerFsParser;
import jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException;
import jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings;
import jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings;
import jct.test.rsc.snpsht.utils.cvsutils.CVSRoot;
import jct.test.rsc.snpsht.verfilesystem.VerFsManager;
import jct.test.rsc.snpsht.verfilesystem.cvsimpl.CvsFsManager;

import org.w3c.dom.Document;

public class CvsFsGeneralInfoParser extends AbstractVerFsParser {
	protected CvsFsManager manager;

	/** DOM Generic Parser */
	private DocumentBuilder domGeneralInfoIndex;

	public CvsFsGeneralInfoParser() {
	}

	@Override
	public CvsFsManager getManager() {
		return this.manager;
	}

	private void loadCvsInfo(Document generalInfo) throws VerFsParserException {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			// Parse CVS info
			String descr, revMlVer;

			descr =
				VerFsCommonStrings
					.XML2ASCII(xpath.evaluate(
						"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
								+ CvsFsCommonStrings.CVS_INFO_MAKER + "/"
								+ CvsFsCommonStrings.CVS_INFO_DESCR_MAKER,
						generalInfo));

			revMlVer =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_INFO_REVML_VERSION_MAKER,
					generalInfo));

			this.manager.setDescription(descr);
			this.manager.setRevmlVersion(revMlVer);
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}
	}

	private void loadCvsRoot(Document generalInfo) throws VerFsParserException {
		String server, protocol, user, pass, separator, rootRepo, currRepo;
		CVSRoot cvsRoot;

		try {
			XPath xpath = XPathFactory.newInstance().newXPath();

			// Parse CVSRoot
			protocol =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_PROTOCOL_MAKER,
					generalInfo));

			server =
				VerFsCommonStrings.XML2ASCII(xpath
					.evaluate(
						"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
								+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
								+ CvsFsCommonStrings.CVS_ROOT_SERVER_MAKER,
						generalInfo));

			user =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate("/"
						+ VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
						+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
						+ CvsFsCommonStrings.CVS_ROOT_USER_MAKER, generalInfo));

			pass =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_PASSWORD_MAKER,
					generalInfo));

			currRepo =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_CURRENT_REPO_MAKER,
					generalInfo));

			rootRepo =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_ROOT_REPO_MAKER,
					generalInfo));

			separator =
				VerFsCommonStrings.XML2ASCII(xpath.evaluate(
					"/" + VerFsCommonStrings.GENERAL_INFO_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_MAKER + "/"
							+ CvsFsCommonStrings.CVS_ROOT_SEPARATOR_MAKER,
					generalInfo));

			cvsRoot = new CVSRoot(protocol, server, rootRepo, currRepo);

			if (user != null && user.compareTo("") != 0) {
				cvsRoot.setUser(user);
			}

			if (pass != null && pass.compareTo("") != 0) {
				cvsRoot.setPassword(pass);
			}

			if (separator != null && separator.compareTo("") != 0) {
				cvsRoot.setSeparator(separator);
			}

			this.manager.setCvsRoot(cvsRoot);
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}
	}

	@Override
	public VerFsManager parse(VerFsManager manager) throws VerFsParserException {

		// Check if manager is a CvsFsManager
		try {
			this.manager = (CvsFsManager) manager;
		} catch (Exception e) {
			throw new VerFsParserException(
				VerFsParserException.ILLEGAL_MANAGER_TYPE,
				"Manger must be a CvsFsManager but is "
						+ manager.getClass().getName());
		}

		// Create parsers with schemas
		if (this.domGeneralInfoIndex == null) {
			this.domGeneralInfoIndex =
				getParser("/jct.test.rsc.snpsht.verfilesystem/resource/repo_general_info.xsd");
		}

		// Get DOM for files index XML file
		Document generalInfo =
			getDocument(
				manager,
				VerFsCommonStrings.GENERAL_INFO_XML_FILE_NAME,
				this.domGeneralInfoIndex);

		loadCvsInfo(generalInfo);
		loadCvsRoot(generalInfo);

		return this.manager;
	}
}
