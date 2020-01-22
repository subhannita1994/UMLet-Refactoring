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
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jct.test.rsc.snpsht.filesystem.IFsFileEntity;
import jct.test.rsc.snpsht.filesystem.IFsNullEntity;
import jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings;
import jct.test.rsc.snpsht.verfilesystem.VerFsFileRev;
import jct.test.rsc.snpsht.verfilesystem.VerFsManager;
import jct.test.rsc.snpsht.verfilesystem.flag.VerFsFile;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VerFsFileRevParser extends AbstractVerFsParser {

	/** DOM Generic Parser */
	private DocumentBuilder domFilesIndex, domFileMetaInfo;

	public void buildFile(String filePathId, String fileId)
			throws VerFsParserException {
		Document fileIndex =
			getDocument(this.manager, filePathId, this.domFileMetaInfo);
		NodeList revs;
		Node revNode;
		List<VerFsFileRev> revsInFile = new ArrayList<VerFsFileRev>();
		String id;

		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			revs =
				(NodeList) xpath.evaluate(
					"/" + VerFsCommonStrings.FILE_MAKER + "/"
							+ VerFsCommonStrings.REVISIONS_MAKER + "/"
							+ VerFsCommonStrings.FILE_REV_MAKER,
					fileIndex,
					XPathConstants.NODESET);

			// For each file revision
			for (int i = 0; i < revs.getLength(); i++) {
				revNode = revs.item(i);

				id =
					VerFsCommonStrings.XML2ASCII(revNode
						.getAttributes()
						.getNamedItem(VerFsCommonStrings.FILE_REV_ID_ATTRIBUTE)
						.getNodeValue());

				revsInFile.add(this.manager.getSimpleRevision(id));
			}

			VerFsFile file =
				this.manager.setAsFile(
					revsInFile.toArray(new VerFsFileRev[0]),
					fileId);

			this.manager.setFileLocation(this.manager
				.getSourceManager()
				.getFileEntity(filePathId), file);
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}

	}

	public void linkFileFilesRev(String fileId) throws VerFsParserException {
		Document fileIndex =
			getDocument(this.manager, fileId, this.domFileMetaInfo);
		NodeList revs, nextRevs;
		Node revNode;
		VerFsFileRev currRev, prevRev, nextRev;
		String id, attr;

		XPath xpath = XPathFactory.newInstance().newXPath();

		try {
			revs =
				(NodeList) xpath.evaluate(
					"/" + VerFsCommonStrings.FILE_MAKER + "/"
							+ VerFsCommonStrings.REVISIONS_MAKER + "/"
							+ VerFsCommonStrings.FILE_REV_MAKER,
					fileIndex,
					XPathConstants.NODESET);

			// For each file revision
			for (int i = 0; i < revs.getLength(); i++) {
				revNode = revs.item(i);

				id =
					VerFsCommonStrings.XML2ASCII(revNode
						.getAttributes()
						.getNamedItem(VerFsCommonStrings.FILE_REV_ID_ATTRIBUTE)
						.getNodeValue());

				currRev = this.manager.getSimpleRevision(id);

				// Load previous revision
				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_PREV_REV_MAKER,
						revNode));

				if (attr != null && attr.compareTo("") != 0) {
					prevRev = this.manager.getSimpleRevision(attr);
					if (prevRev == null) {
						throw new VerFsParserException(
							VerFsParserException.UNEXPECTED_NODE_VALUE,
							"Can't find file revision in manager : " + attr);
					}

					this.manager.setPrevRevision(prevRev, currRev);
				}

				nextRevs =
					(NodeList) xpath.evaluate(
						VerFsCommonStrings.FILE_REV_NEXT_REV_MAKER + "/"
								+ VerFsCommonStrings.FILE_REV_ID_MAKER,
						revNode,
						XPathConstants.NODESET);
				// Load each next file revision
				for (int j = 0; j < nextRevs.getLength(); j++) {
					attr = nextRevs.item(j).getFirstChild().getNodeValue();
					nextRev = this.manager.getSimpleRevision(attr);
					if (nextRev == null) {
						throw new VerFsParserException(
							VerFsParserException.UNEXPECTED_NODE_VALUE,
							"Can't find file revision in manager : " + attr);
					}

					this.manager.addNextRevision(nextRev, currRev);
				}
			}
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}

	}

	public void buildFileFilesRev(String filePathId)
			throws VerFsParserException {
		Document fileIndex =
			getDocument(this.manager, filePathId, this.domFileMetaInfo);
		NodeList revs, tags;
		Node rev;
		String id, path, attr;
		VerFsFileRev fileRev;
		IFsFileEntity location;

		try {
			XPath xpath = XPathFactory.newInstance().newXPath();

			revs =
				(NodeList) xpath.evaluate(
					"/" + VerFsCommonStrings.FILE_MAKER + "/"
							+ VerFsCommonStrings.REVISIONS_MAKER + "/"
							+ VerFsCommonStrings.FILE_REV_MAKER,
					fileIndex,
					XPathConstants.NODESET);

			// For each file revision
			for (int i = 0; i < revs.getLength(); i++) {
				rev = revs.item(i);
				// Create revision
				id =
					VerFsCommonStrings.XML2ASCII(rev
						.getAttributes()
						.getNamedItem(VerFsCommonStrings.FILE_REV_ID_ATTRIBUTE)
						.getNodeValue());

				path =
					VerFsCommonStrings.XML2PATH(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_PATH_MAKER,
						rev));

				fileRev = this.manager.addSimpleRevision(path, id);

				// Load attributes
				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_REV_MAKER,
						rev));

				this.manager.setRevID(attr, fileRev);

				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_ACTION_MAKER,
						rev));

				this.manager.setAction(attr, fileRev);

				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_AUTHOR_MAKER,
						rev));

				this.manager.setAuthor(attr, fileRev);

				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_COMMENT_MAKER,
						rev));

				this.manager.setComment(attr, fileRev);

				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_COMMENT_MAKER,
						rev));

				this.manager.setComment(attr, fileRev);

				attr =
					VerFsCommonStrings.XML2ASCII(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_BRANCH_MAKER,
						rev));

				this.manager.setBranch(attr, fileRev);

				// Parse and add tags
				tags =
					(NodeList) xpath.evaluate(
						VerFsCommonStrings.FILE_REV_TAGS_MAKER + "/"
								+ VerFsCommonStrings.FILE_REV_TAG_MAKER,
						rev,
						XPathConstants.NODESET);
				for (int j = 0; j < tags.getLength(); j++) {
					this.manager.addTag(VerFsCommonStrings.XML2ASCII(tags.item(
						j).getFirstChild().getNodeValue()), fileRev);
				}

				if (existChild(
					rev,
					VerFsCommonStrings.FILE_REV_UPDATE_TIME_MAKER)) {
					// Parse update time
					attr =
						VerFsCommonStrings.XML2ASCII(xpath.evaluate(
							VerFsCommonStrings.FILE_REV_UPDATE_TIME_MAKER + "/"
									+ VerFsCommonStrings.FILE_REV_TIME_MAKER,
							rev));
					
					this.manager.setUpdateTime(
						new Date(new Long(attr)),
						fileRev);
				}

				// Get file revision location
				attr =
					VerFsCommonStrings.XML2PATH(xpath.evaluate(
						VerFsCommonStrings.FILE_REV_FILE_LOCATION_MAKER,
						rev));

				location = this.manager.getSourceManager().getFileEntity(attr);
				if (location == null || location instanceof IFsNullEntity) {
					throw new VerFsParserException(
						VerFsParserException.SOURCE_NOT_FOUND,
						"Can't find source file in manager : " + attr);
				}

				this.manager.setFileRevLocation(location, fileRev);
			}
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}
	}

	@Override
	public VerFsManager parse(VerFsManager manager) throws VerFsParserException {
		NodeList files;
		String metaInfoPath, rootRepo;

		try {
			this.manager = manager;

			// Create parsers with schemas
			if (this.domFileMetaInfo == null) {
				this.domFileMetaInfo =
					getParser("/jct.test.rsc.snpsht.verfilesystem/resource/meta-info.xsd");
			}

			if (this.domFilesIndex == null) {
				this.domFilesIndex =
					getParser("/jct.test.rsc.snpsht.verfilesystem/resource/files_index.xsd");
			}

			// Get DOM for files index XML file
			Document fileIndex =
				getDocument(
					manager,
					VerFsCommonStrings.FILES_INDEX_XML_FILE_NAME,
					this.domFilesIndex);

			XPath xpath = XPathFactory.newInstance().newXPath();

			// Grab root repository name
			rootRepo =
				VerFsCommonStrings.XML2PATH(xpath.evaluate("/"
						+ VerFsCommonStrings.FILES_MAKER + "/"
						+ VerFsCommonStrings.REPO_ROOT_MAKER, fileIndex));

			this.manager.makeRoot(rootRepo);

			files =
				(NodeList) xpath.evaluate(
					"/" + VerFsCommonStrings.FILES_MAKER + "/"
							+ VerFsCommonStrings.FILE_MAKER,
					fileIndex,
					XPathConstants.NODESET);

			// Build files and files revisions.
			// Need 3 pass for more flexibility.
			// 3 pass allows file revs prev/next version to be link out of a
			// same file. This behavior is not needed for the moment, but allows
			// its use.

			// Build files revisions, of each XML file files
			for (int i = 0; i < files.getLength(); i++) {
				metaInfoPath =
					VerFsCommonStrings.XML2PATH(xpath.evaluate(
						VerFsCommonStrings.FILE_LOCATION_MARKER,
						files.item(i)));

				buildFileFilesRev(metaInfoPath);

			}

			// Link files revisions, of each XML file files
			for (int i = 0; i < files.getLength(); i++) {
				metaInfoPath =
					VerFsCommonStrings.XML2PATH(xpath.evaluate(
						VerFsCommonStrings.FILE_LOCATION_MARKER,
						files.item(i)));

				linkFileFilesRev(metaInfoPath);
			}

			// Finally, build file
			for (int i = 0; i < files.getLength(); i++) {

				String id =
					VerFsCommonStrings.XML2ASCII(files
						.item(i)
						.getAttributes()
						.getNamedItem(VerFsCommonStrings.FILE_ID_ATTRIBUTE)
						.getNodeValue());

				metaInfoPath =
					VerFsCommonStrings.XML2PATH(xpath.evaluate(
						VerFsCommonStrings.FILE_LOCATION_MARKER,
						files.item(i)));

				buildFile(metaInfoPath, id);
			}
		} catch (XPathExpressionException e) {
			throw new VerFsParserException(VerFsParserException.BAD_XPATH, e);
		}

		return this.manager;
	}
}
