/*
 * Created on 7 juil. 2003
 *
 */
package fr.emn.oadymppac.graph;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import javax.swing.BoundedRangeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import salvo.jesus.graph.DirectedGraph;
import fr.emn.oadymppac.ConstraintException;
import fr.emn.oadymppac.DefaultExplanation;
import fr.emn.oadymppac.Explanation;
import fr.emn.oadymppac.event.TimeEvent;
import fr.emn.oadymppac.event.TimeListener;
import fr.emn.oadymppac.utils.IntervalList;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.2 $
 *
 * This class provides a loader for static constraint graphs. 
 * The graph is loaded once and for all. The vertices of the
 * graph may represent constraints only, variables only or
 * both constraints and variables. The user may indicate which
 * mode he whishes to have, the default mode handles constraints 
 * only. The vertices of this graph are linked explanation-wise ie.
 * in the CONSCONS mode, every pair of constraints enclosed in 
 * the same explanation are linked, in the VARVAR mode,
 * every pair of variables belonging to two constraints enclosed
 * in the same explanation are linked together and, lastly, in the
 * CONSVAR mode the variables of a given constraint are linked to
 * the other constraints enclosed in the same explanation.  
 */
public class ConstraintGraphLoader extends DefaultHandler implements
		GraphLoader {

	public static final int CONSCONS = 0;
	public static final int VARVAR = 1;
	public static final int CONSVAR = 2;
	public static int DEFAULT_MODE = ConstraintGraphLoader.CONSCONS;

	public int mode = ConstraintGraphLoader.DEFAULT_MODE;

	NamedGraphDelegator graph;
	BoundedRangeModel maxModel;
	BoundedRangeModel visibleEdgeModel;
	IntervalList intervalList;
	int maxWeight = 0;

	HashMap map = new HashMap();

	private final DefaultExplanation explanation = new DefaultExplanation();
	private final Vector constraintList = new Vector();
	private String[] clist;
	private String[] vlist;
	private String[] vlist2;
	private String value, cname, vname, othername;
	private int n;
	private boolean directed, seeStructure;
	private HashSet set;
	private final TimeEvent timeEvent = new TimeEvent(this);
	// association between external and internal 
	// representation of variables and constraints
	private final HashMap externalNames = new HashMap();

	private String structureType = "onevar";
	String sliceEvent = "solution";

	Vector timeListeners = new Vector();

	public void addTimeListener(final TimeListener l) {
		this.timeListeners.addElement(l);
	}

	/**
	 * This method adds the vertex passed by name
	 * to the graph.
	 * 
	 * @param name
	 */
	void addVertex(final String name) {
		try {
			this.graph.findVertex(name);
		}
		catch (final Exception e) {
			System.err.println("insertion error : could not insert vertex "
					+ name);
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
		this.intervalList.addValue(this.n);
		this.intervalList.createListIterator();
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(
		final String namespaceURI,
		final String localName,
		final String qName) throws SAXException {
		if (qName.equals("new-constraint")) {
			this.map.put(this.cname, this.set);
		}
		else if (qName.equals("explanation") && !this.seeStructure) {
			if (this.constraintList.size() > 0) {
				final String[] list = new String[this.constraintList.size()];
				this.explanation
					.setConstraintList((String[]) this.constraintList
						.toArray(list));
				// link the suitable vertices depending on the mode
				switch (this.mode) {
					case CONSCONS :
						this.linkConstraints(this.explanation);
						break;
					case VARVAR :
						this.linkVariables(this.explanation);
						break;
					case CONSVAR :
						this.linkConsVar(this.explanation);
						break;
				}
				this.constraintList.clear();
			}
		}
		else if (qName.equals("reduce") && this.seeStructure) {
			this.constraintList.clear();
			String[] list;
			String[] varList;
			if (this.mode == ConstraintGraphLoader.CONSCONS) {
				if (this.structureType.equals("allvars")) {
					varList = this.getVariablesforConstraint(this.cname);
					for (int i = 0; i < varList.length; i++) {
						list = this.getConstraintsForVariable(varList[i]);
						for (int j = 0; j < list.length; j++) {
							if (this.cname.equals(list[j])) {
								continue;
							}
							this.updateEdge(this.cname, list[j]);
						}
					}
				}
				else if (this.structureType.equals("onevar")) {
					list = this.getConstraintsForVariable(this.vname);
					for (int i = 0; i < list.length; i++) {
						if (this.cname.equals(list[i])) {
							continue;
						}
						this.updateEdge(this.cname, list[i]);
					}
				}
			}
			else if (this.mode == ConstraintGraphLoader.VARVAR) {
				list = new String[1];
				list[0] = this.cname;
				this.explanation.setConstraintList(list);
				this.linkVariables(this.explanation);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(final String prefix) throws SAXException {
		// TODO Auto-generated method stub

	}

	/**
	 * Notifies all time listeners of <code>TimeEvent</code>s.
	 *
	 */
	public void fireTimeChanged() {
		this.timeEvent.setN(this.n);
		for (int i = 0; i < this.timeListeners.size(); i++) {
			((TimeListener) this.timeListeners.elementAt(i))
				.timeChanged(this.timeEvent);
		}
	}

	/**
	 * Returns the list of constraints related to 
	 * the passed variable.
	 */
	String[] getConstraintsForVariable(final String vname) {
		Set vars;
		String tmpCons;
		final Set constraints = new HashSet();
		final Iterator iter = this.map.keySet().iterator();
		while (iter.hasNext()) {
			tmpCons = (String) iter.next();
			vars = (Set) this.map.get(tmpCons);
			if (vars.contains(vname)) {
				constraints.add(tmpCons);
			}
		}
		String[] list = new String[constraints.size()];
		list = (String[]) constraints.toArray(list);
		return list;
	}

	/**
	 * Returns the internal / external names 
	 * association map.
	 * 
	 * @return
	 */
	public HashMap getExternalNames() {
		return this.externalNames;
	}

	/**
	 * @return
	 */
	public BoundedRangeModel getMaxModel() {
		return this.maxModel;
	}

	/**
	 * Returns the active mode.
	 * 
	 * @return
	 */
	public int getMode() {
		return this.mode;
	}

	/**
	 * @return
	 */
	public String getSliceEvent() {
		return this.sliceEvent;
	}

	/**
	 * Returns the variables related to the passed
	 * constraint.
	 */
	String[] getVariablesforConstraint(final String cname) {
		Set vars;
		vars = (Set) this.map.get(cname);
		String[] list = new String[vars.size()];
		if (vars != null) {
			list = (String[]) vars.toArray(list);
		}
		return list;
	}

	/**
	 * @return
	 */
	public BoundedRangeModel getVisibleEdgeModel() {
		return this.visibleEdgeModel;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(
		final char[] ch,
		final int start,
		final int length) throws SAXException {
		// TODO Auto-generated method stub

	}

	/**
	 * This method links the constraints enclosed
	 * in the passed explanation.
	 * 
	 * @param exp
	 */
	void linkConstraints(final Explanation exp) {
		this.clist = exp.getConstraintList();
		if (this.clist.length > 0) {
			for (int i = 0; i < this.clist.length; i++) {
				for (int j = i + 1; j < this.clist.length; j++) {
					this.updateEdge(this.clist[i], this.clist[j]);
				}
			}
		}
		this.reset();
	}

	/**
	 * This method links the variables of the constraints
	 * enclosed in the passed explanation. It calls 
	 * <code>processConstraints</code> on every possible 
	 * pair of constraints. N.B : There are !n possible 
	 * combinations where n is the number of constraints 
	 * in the passed explanation.
	 * 
	 * @param exp
	 */
	//	void linkVariables(Explanation exp) {
	//		clist = exp.getConstraintList();
	//		for (int i = 0; i < clist.length; i++) {
	//			for (int j = i; j < clist.length; j++){
	//				try {
	//					processConstraints(clist[i], clist[j]);
	//				} catch (ConstraintException e){
	//					e.printStackTrace();
	//				}
	//			}
	//		}

	/**
	 * This method links the constraints enclosed in the
	 * passed explanation to each other's variables.
	 * 
	 * @param exp
	 */
	void linkConsVar(final Explanation exp) {
		this.clist = exp.getConstraintList();
		HashSet vars;
		if (this.clist.length > 0) {
			for (int i = 0; i < this.clist.length; i++) {
				if (this.map.get(this.clist[i]) == null) {
					this.map.put(this.clist[i], new HashSet());
				}

				vars = (HashSet) this.map.get(this.clist[i]);

				this.vlist = new String[vars.size()];
				this.vlist = (String[]) vars.toArray(this.vlist);

				for (int j = 0; j < this.clist.length; j++) {
					if (j == i) {
						continue;
					}
					for (int k = 0; k < this.vlist.length; k++) {
						this.updateEdge(this.clist[j], this.vlist[k]);
					}
				}
			}
		}
		this.reset();
	}

	/**
	 * The currently updated variable is pointed to
	 * by the variables of the constraints enclosed
	 * in the passed explanation.
	 * 
	 * @param exp
	 */
	void linkVariables(final Explanation exp) {
		this.clist = exp.getConstraintList();
		for (int i = 0; i < this.clist.length; i++) {
			try {
				this.processConstraint(this.vname, this.clist[i]);
			}
			catch (final ConstraintException e) {
				e.printStackTrace();
			}
		}
		this.reset();
	}

	/* (non-Javadoc)
	 * @see fr.emn.oadymppac.graph.GraphLoader#load(java.net.URL, fr.emn.oadymppac.graph.NamedGraphDelegator)
	 */
	public float load(final URL url, final NamedGraphDelegator graph) {
		this.graph = graph;
		this.directed = graph.getUnderlyingGraph() instanceof DirectedGraph;
		final File file = new File(url.getFile());
		BufferedInputStream bis;
		SAXParser parser;

		try {
			InputSource is;

			if (url.getFile().endsWith(".gz")) {
				is =
					new InputSource(new GZIPInputStream(new FileInputStream(
						url.getFile())));
			}
			else {
				is = new InputSource(url.getFile());
			}

			bis = new BufferedInputStream(new FileInputStream(url.getFile()));

			is.setByteStream(bis);
			is.setSystemId(url.toExternalForm());

			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(is, this);

		}
		catch (final SAXException e) {
			e.printStackTrace();
		}
		catch (final FileNotFoundException e) {
			System.err.println("file: " + file.getName() + " not found");
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
		catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		finally {
			//fireStateChanged();
		}
		return this.maxWeight;
	}

	/**
	 * Prints the contents of the internal / external names
	 * association map.
	 *
	 */
	public void printAllNames() {
		final Set entries = this.externalNames.entrySet();
		final Iterator iter = entries.iterator();
		Map.Entry tmp;
		while (iter.hasNext()) {
			tmp = (Map.Entry) iter.next();
			System.out.println((String) tmp.getKey() + " : "
					+ (String) tmp.getValue());
		}
	}

	/**
	 * The variables of the passed constraint point
	 * to the passed variable. (the variable graph is 
	 * directed)
	 * 
	 * @param vname
	 * @param cname
	 * @throws ConstraintException
	 */
	void processConstraint(final String vname, final String cname)
			throws ConstraintException {
		if (!this.map.containsKey(cname)) {
			throw new ConstraintException("Undefined constraint : " + cname);
		}

		if (this.map.get(cname) == null) {
			this.map.put(cname, new HashSet());
		}

		final HashSet vars = (HashSet) this.map.get(cname);
		this.vlist = new String[vars.size()];
		this.vlist = (String[]) vars.toArray(this.vlist);

		for (int i = 0; i < this.vlist.length; i++) {
			if (this.vlist[i].equals(vname)) {
				continue;
			}
			this.updateEdge(this.vlist[i], vname);
		}
	}

	/**
	 * This method links the variables of the passed 
	 * constraints. It throws a <code>ConstraintException</code>
	 * if either constraints is undefined. 
	 * 
	 * @param cname1
	 * @param cname2
	 * @throws ConstraintException
	 */
	void processConstraints(final String cname1, final String cname2)
			throws ConstraintException {
		if (!this.map.containsKey(cname1)) {
			throw new ConstraintException("Undefined constraint : " + cname1);
		}
		if (!this.map.containsKey(cname2)) {
			throw new ConstraintException("Undefined constraint : " + cname2);
		}

		if (this.map.get(cname1) == null) {
			this.map.put(cname1, new HashSet());
		}
		if (this.map.get(cname2) == null) {
			this.map.put(cname2, new HashSet());
		}

		final HashSet vars = (HashSet) this.map.get(cname1);
		final HashSet vars2 = (HashSet) this.map.get(cname2);

		this.vlist = new String[vars.size()];
		this.vlist = (String[]) vars.toArray(this.vlist);
		this.vlist2 = new String[vars2.size()];
		this.vlist2 = (String[]) vars2.toArray(this.vlist2);

		for (int i = 0; i < this.vlist.length; i++) {
			for (int j = 0; j < this.vlist2.length; j++) {
				// don't link a variable with itself
				if (this.vlist[i].equals(this.vlist2[j])) {
					continue;
				}
				this.updateEdge(this.vlist[i], this.vlist2[j]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(final String target, final String data)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	public void removeAllListeners() {
		this.timeListeners.clear();
	}

	public void removeTimeListener(final TimeListener l) {
		this.timeListeners.removeElement(l);
	}

	/**
	 * Resets the buffers.
	 */
	private void reset() {
		this.clist = null;
		this.vlist = null;
		this.vlist2 = null;
		//vname = null;
		//cname = null;
		this.set = null;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(final Locator locator) {
		// TODO Auto-generated method stub

	}

	public void setIntervalList(final IntervalList list) {
		this.intervalList = list;
	}

	/**
	 * @param model
	 */
	public void setMaxModel(final BoundedRangeModel model) {
		this.maxModel = model;
	}

	/**
	 * Sets the active mode, possible values are 
	 * <code>CONSCONS</code>, <code>VARVAR</code> 
	 * and <code>CONSVAR</code>.
	 * 
	 * @param i
	 */
	public void setMode(final int i) {
		switch (i) {
			case CONSCONS :
				this.mode = i;
				break;
			case VARVAR :
				this.mode = i;
				break;
			case CONSVAR :
				this.mode = i;
				break;
		}
	}

	/**
	 * @param b
	 */
	public void setSeeStructure(final boolean b) {
		this.seeStructure = b;
	}

	/**
	 * @param string
	 */
	public void setSliceEvent(final String string) {
		this.sliceEvent = string;
	}

	/**
	 * @param string
	 */
	public void setStructureType(final String string) {
		this.structureType = string;
	}

	/**
	 * @param model
	 */
	public void setVisibleEdgeModel(final BoundedRangeModel model) {
		this.visibleEdgeModel = model;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(final String name) throws SAXException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
		this.intervalList.addValue(0);
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(
		final String namespaceURI,
		final String localName,
		final String qName,
		final Attributes atts) throws SAXException {

		if (qName.equals(this.sliceEvent)) {
			this.intervalList.addValue(Integer.parseInt(atts.getValue("n")));
		}

		if (qName.equals("new-variable")) {
			this.value = atts.getValue("vname");
			if (this.mode == ConstraintGraphLoader.VARVAR
					|| this.mode == ConstraintGraphLoader.CONSVAR) {
				if (this.value != null) {
					this.addVertex(this.value.intern());
				}
				this.othername = atts.getValue("externalrep");
				this.externalNames.put(
					this.value.intern(),
					this.othername == null ? this.value.intern()
							: this.othername.intern());
			}
		}
		else if (qName.equals("new-constraint")) {
			this.value = atts.getValue("cname");
			if (this.value != null) {
				this.cname = this.value.intern();
				this.set = new HashSet();
				this.map.put(this.cname, this.set);
				if (this.mode == ConstraintGraphLoader.CONSCONS
						|| this.mode == ConstraintGraphLoader.CONSVAR) {
					this.othername = atts.getValue("externalrep");
					this.externalNames.put(
						this.cname,
						this.othername == null ? this.cname : this.othername
							.intern());
					this.addVertex(this.cname);
				}
			}
		}
		else if (qName.equals("reduce")) {
			this.n = Integer.parseInt(atts.getValue("n"));
			if (this.seeStructure) {
				this.cname = atts.getValue("cname");
			}
			this.fireTimeChanged();
		}
		else if (qName.equals("solution")) {
		}
		else if (qName.equals("update")) {
			this.value = atts.getValue("vname");
			if (this.value != null) {
				this.vname = this.value.intern();
			}
			// true when declaring a new constraint
			if (this.set != null) {
				this.set.add(this.vname);
			}
		}
		else if (qName.equals("constraint")) {
			this.value = atts.getValue("cname");

			if (this.value != null) {
				this.constraintList.addElement(this.value.intern());
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(final String prefix, final String uri)
			throws SAXException {
		// TODO Auto-generated method stub

	}

	/**
	 * This method updates the history of the edge
	 * specified by the passed vertices. The vertices
	 * and the edge are created when necessary.
	 * 
	 * @param v1
	 * @param v2
	 */
	void updateEdge(final String v1, final String v2) {
		EdgeWithHistory edge =
			(EdgeWithHistory) this.graph.getEdge(
				this.graph.findVertex(v1),
				this.graph.findVertex(v2));
		if (edge == null) {
			try {
				edge =
					this.directed ? new DirectedEdgeWithHistory(
						this.graph.findVertex(v1),
						this.graph.findVertex(v2)) : new EdgeWithHistory(
						this.graph.findVertex(v1),
						this.graph.findVertex(v2));
				this.graph.addEdge(edge);
			}
			catch (final Exception e) {
				e.printStackTrace();
			}
		}
		edge.add(this.n);
		this.updateMax(edge.getHistoryLength());
		this.updateVisible();
	}

	/**
	 * Updates the maximum bound of the intensity model.
	 * 
	 * @param m
	 */
	public void updateMax(final int m) {
		if (this.maxWeight < m) {
			this.maxWeight = m;
			if (this.maxModel != null) {
				this.maxModel.setMaximum(m);
			}
		}
	}

	/**
	 * Updates the visible edge model.
	 */
	public void updateVisible() {
		if (this.visibleEdgeModel != null) {
			this.visibleEdgeModel.setMaximum(this.graph.getEdgesCount());
		}
	}

}