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
package jct.test.rsc.snpsht.parser.verfilesystem;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jct.test.rsc.snpsht.parser.verfilesystem.cvsimpl.CvsFsConstraintParser;
import jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings;
import jct.test.rsc.snpsht.verfilesystem.VerFsFileRev;
import jct.test.rsc.snpsht.verfilesystem.VerFsManager;
import jct.test.rsc.snpsht.verfilesystem.cvsimpl.CvsFsManager;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VerFsCommitParser extends AbstractVerFsParser {

	/** DOM Generic Parser */
	private DocumentBuilder domCommitsIndex;

	@Override
	public VerFsManager parse(VerFsManager manager) throws VerFsParserException {
		NodeList commits, revs;
		Node commit, rev;
		VerFsFileRev fileRev;
		List<VerFsFileRev> commitFilesRev;
		String fileRevId;

		try {
			this.manager = manager;

			// Create jct.test.rsc.snpsht.parser with schemas
			if (this.domCommitsIndex == null) {
				this.domCommitsIndex =
					getParser("/jct.test.rsc.snpsht.verfilesystem/resource/commits_index.xsd");
			}

			// Get DOM for files index XML file
			Document commitsIndex =
				getDocument(
					manager,
					VerFsCommonStrings.COMMITS_INDEX_XML_FILE_NAME,
					this.domCommitsIndex);

			XPath xpath = XPathFactory.newInstance().newXPath();

			commits =
				(NodeList) xpath.evaluate(
					"/" + VerFsCommonStrings.COMMITS_MAKER + "/"
							+ VerFsCommonStrings.COMMIT_MAKER,
					commitsIndex,
					XPathConstants.NODESET);

			// For each commit
			for (int i = 0; i < commits.getLength(); i++) {
				commit = commits.item(i);

				// For each file rev in commit
				commitFilesRev = new ArrayList<VerFsFileRev>();
				revs =
					(NodeList) xpath.evaluate(
						VerFsCommonStrings.REVISIONS_MAKER + "/"
								+ VerFsCommonStrings.FILE_REV_MAKER,
						commit,
						XPathConstants.NODESET);
				for (int j = 0; j < revs.getLength(); j++) {
					rev = revs.item(j);

					// Grab file revision
					fileRevId =
						VerFsCommonStrings.XML2ASCII(rev
							.getAttributes()
							.getNamedItem(
								VerFsCommonStrings.FILE_REV_ID_ATTRIBUTE)
							.getNodeValue());

					fileRev = this.manager.getSimpleRevision(fileRevId);
					if (fileRev == null) {
						throw new VerFsParserException(
							VerFsParserException.UNEXPECTED_NODE_VALUE,
							"Can't find file revision in manager : "
									+ fileRevId);
					}

					// Add file revision to commit
					commitFilesRev.add(fileRev);
				}

				// Set commit
				this.manager.setAsCommit(commitFilesRev
					.toArray(new VerFsFileRev[0]));
			}

			// If system comes form a CVS, we parse constraints use to built
			// commits
			try {
				CvsFsManager cvsManager = (CvsFsManager) this.manager;

				CvsFsConstraintParser constraintParser =
					new CvsFsConstraintParser();
				this.manager = constraintParser.parse(cvsManager);
			} catch (Exception e) {
			}

		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}

		return this.manager;
	}
}
