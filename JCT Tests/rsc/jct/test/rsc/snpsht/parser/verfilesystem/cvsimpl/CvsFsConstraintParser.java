package jct.test.rsc.snpsht.parser.verfilesystem.cvsimpl;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import jct.test.rsc.snpsht.parser.verfilesystem.AbstractVerFsParser;
import jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException;
import jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings;
import jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings;
import jct.test.rsc.snpsht.verfilesystem.VerFsManager;
import jct.test.rsc.snpsht.verfilesystem.cvsimpl.CvsFsManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class CvsFsConstraintParser
extends jct.test.rsc.snpsht.parser.verfilesystem.AbstractVerFsParser
{
public void <init>()
{
this.<init>();

}

protected jct.test.rsc.snpsht.verfilesystem.cvsimpl.CvsFsManager manager;

private javax.xml.parsers.DocumentBuilder domBranchIndex;

public jct.test.rsc.snpsht.verfilesystem.VerFsManager parse(jct.test.rsc.snpsht.verfilesystem.VerFsManager manager) throws jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException
{
java.lang.String constrType;
java.lang.String attrType;
java.lang.String value;
java.util.List attrs = new java.util.ArrayList();
java.lang.String[] typeValue;
org.w3c.dom.Node constraint;
org.w3c.dom.Node attribute;
org.w3c.dom.NodeList constraints;
org.w3c.dom.NodeList attributes;
try
{
javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
try
{
this.manager = (jct.test.rsc.snpsht.verfilesystem.cvsimpl.CvsFsManager)manager;

}
catch(java.lang.Exception e) 
{
throw new jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException(jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException.ILLEGAL_MANAGER_TYPE, "Manger must be a CvsFsManager but is " + manager.getClass().getName());

}
if(this.domBranchIndex == null) 
{
this.domBranchIndex = this.getParser("/jct.test.rsc.snpsht.verfilesystem/resource/repo_general_info.xsd");

}
org.w3c.dom.Document generalInfo = this.getDocument(manager, jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings.GENERAL_INFO_XML_FILE_NAME, this.domBranchIndex);
constraints = (org.w3c.dom.NodeList)xpath.evaluate("/" + jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings.GENERAL_INFO_MAKER + "/" + jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTRAINTS_MARKER + "/" + jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTRAINT_MARKER, generalInfo, javax.xml.xpath.XPathConstants.NODESET);
for(int i = 0; i < constraints.getLength(); i ++) 
{
constraint = constraints.item(i);
constrType = jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings.XML2ASCII(xpath.evaluate(jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTR_TYPE_MARKER, constraint));
attributes = (org.w3c.dom.NodeList)xpath.evaluate(jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTR_ATTRS_MARKER + "/" + jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTR_ATTR_MARKER, constraint, javax.xml.xpath.XPathConstants.NODESET);
for(int j = 0; j < attributes.getLength(); j ++) 
{
attribute = attributes.item(j);
attrType = jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings.XML2ASCII(attribute.getAttributes().getNamedItem(jct.test.rsc.snpsht.serializer.verfilesystem.cvsimpl.CvsFsCommonStrings.CVS_COMMIT_GROUP_CONSTR_ATTR_TYPE_ATTR).getNodeValue());
value = jct.test.rsc.snpsht.serializer.verfilesystem.VerFsCommonStrings.XML2ASCII(attribute.getFirstChild().getNodeValue());
typeValue = new java.lang.String[] { attrType, value };
attrs.add(typeValue);

}
this.manager.addConstraint(constrType, attrs.toArray(new java.lang.String[0][]));

}
return this.manager;

}
catch(javax.xml.xpath.XPathExpressionException e) 
{
throw new jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException(jct.test.rsc.snpsht.parser.verfilesystem.VerFsParserException.BAD_XPATH, e);

}

}


}
