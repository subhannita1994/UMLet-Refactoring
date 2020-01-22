/*
 * Created on 23 juil. 2003
 *
 */
package fr.emn.oadymppac.graph.clustering;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import fr.emn.oadymppac.graph.AdjacencyMatrixRowColumn;
import fr.emn.oadymppac.tree.StateNode;
import fr.emn.oadymppac.widgets.ClusteringHandler;

/**
 * @author Mohammad Ghoniem
 * @version $Revision: 1.1 $
 *
 * This class provides the common behavior required
 * for all matrix clusterizers. The inheriting classes
 * must implement their own clustering strategy in the
 * <code>clusterize()</code> method.
 */
public abstract class AbstractMatrixClusterizer implements MatrixClusterizer {

	/**
	 * This interface defines a metric used to compute 
	 * the distance between tow rows (resp. columns).
	 */
	public interface Metric {
		public float distance(AdjacencyMatrixRowColumn rc, int rc1, int rc2);
	}

	/**
	 * Scalar Product based metric.
	 */
	public static class ScalarProduct implements Metric {
		/**
		 * The distance between two rows or columns is set 
		 * to the number of "ones" they *don't* have in common i.e. 
		 * the number of vertices they don't share.
		 */
		public float distance(
			final AdjacencyMatrixRowColumn rc,
			final int index1,
			final int index2) {
			float count = 0;
			final List vertices1 =
				rc.getGraph().getAdjacentVertices(
					rc.getGraph().findVertex(rc.getName(index1)));
			final List vertices2 =
				rc.getGraph().getAdjacentVertices(
					rc.getGraph().findVertex(rc.getName(index2)));
			final Iterator iter = vertices1.iterator();

			while (iter.hasNext()) {
				if (vertices2.contains(iter.next())) {
					count++;
				}
			}
			return vertices2.size() > vertices1.size() ? Math.abs(vertices2
				.size() - count) : Math.abs(vertices1.size() - count);
		}
	}
	public static final int SCALAR_PRODUCT = 0;
	public static int DEFAULT_METRIC = AbstractMatrixClusterizer.SCALAR_PRODUCT;
	public static final int DEFAULT_TOLERANCE = 5;
	AdjacencyMatrixRowColumn row;

	AdjacencyMatrixRowColumn column;
	boolean animated = true;

	int distanceMetric = AbstractMatrixClusterizer.DEFAULT_METRIC;
	int tolerance = AbstractMatrixClusterizer.DEFAULT_TOLERANCE;
	Metric metric;

	TreeModel horizontalTreeModel;
	TreeModel verticalTreeModel;

	EventListenerList listenerList = new EventListenerList();

	protected transient ChangeEvent changeEvent = null;

	/**
	 * Creates a clusterizer with the passed row and column and
	 * a <code>ScalarProduct</code> metric by default.
	 * 
	 * @param row
	 * @param column
	 */
	public AbstractMatrixClusterizer(
		final AdjacencyMatrixRowColumn row,
		final AdjacencyMatrixRowColumn column) {
		this(row, column, new ScalarProduct(), new DefaultTreeModel(
			new StateNode("root")), new DefaultTreeModel(new StateNode("root")));
	}

	/**
	 * Default constructor.
	 * 
	 * @param row
	 * @param column
	 */
	public AbstractMatrixClusterizer(
		final AdjacencyMatrixRowColumn row,
		final AdjacencyMatrixRowColumn column,
		final Metric m) {
		this(
			row,
			column,
			m,
			new DefaultTreeModel(new StateNode("root")),
			new DefaultTreeModel(new StateNode("root")));
	}

	public AbstractMatrixClusterizer(
		final AdjacencyMatrixRowColumn row,
		final AdjacencyMatrixRowColumn column,
		final Metric m,
		final TreeModel htree,
		final TreeModel vtree) {
		this.row = row;
		this.column = column;
		this.horizontalTreeModel = htree;
		this.verticalTreeModel = vtree;
		this.extractNodes(column, this.horizontalTreeModel);
		this.extractNodes(row, this.verticalTreeModel);
		this.setMetric(m);
	}

	/**
	 * Adds a <code>ChangeListener</code>.  The change listeners are
	 * run each time any one of the properties changes.
	 * 
	 * @param l the ChangeListener to add
	 * 
	 * @see #removeChangeListener
	 * @see BoundedRangeModel#addChangeListener
	 */
	public void addChangeListener(final ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/* (non-Javadoc)
	 * @see fr.emn.oadymppac.graph.MatrixClusterizer#clusterize()
	 */
	public void clusterize() {
		this.preClusterize();
		this.doClusterize();
		this.postClusterize();
	}

	/**
	 * This method computes the distance between the rows or columns given by index.
	 */
	public float distance(
		final AdjacencyMatrixRowColumn rc,
		final int index1,
		final int index2) {
		return this.metric.distance(rc, index1, index2);
	}

	/**
	 * This method computes the distance between the rows or columns given by name.
	 */
	public float distance(
		final AdjacencyMatrixRowColumn rc,
		final String name1,
		final String name2) {
		return this.metric.distance(rc, rc.indexOf(name1), rc.indexOf(name2));
	}

	public abstract void doClusterize();

	/**
	 * Creates a flat tree from the passed <code>AdjacencyMatrixRowColumn</code> 
	 * and stores it in the passed <code>TreeModel</code>.
	 */
	public void extractNodes(
		final AdjacencyMatrixRowColumn rc,
		final TreeModel tree) {
		((DefaultMutableTreeNode) tree.getRoot()).removeAllChildren();
		for (int i = 0; i < rc.getCount(); i++) {
			((DefaultMutableTreeNode) tree.getRoot()).add(new StateNode(rc
				.getName(i)));
		}
	}

	/**
	 * Runs each <code>ChangeListener</code>'s
	 * <code>stateChanged</code> method.
	 * 
	 * @see #setRangeProperties
	 * @see EventListenerList
	 */
	public void fireStateChanged() {
		final Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (this.changeEvent == null) {
					this.changeEvent = new ChangeEvent(this);
				}

				((ChangeListener) listeners[i + 1])
					.stateChanged(this.changeEvent);
			}
		}
	}

	/**
	 * Returns an array of all the change listeners registered on
	 * this <code>BasicAjacencyMatrix</code>.
	 * 
	 * @return all of this object <code>ChangeListener</code>s or an
	 *         empty array if no change listeners are currently
	 *         registered
	 * 
	 * @since 1.4
	 * @see #addChangeListener
	 * @see #removeChangeListener
	 */
	public ChangeListener[] getChangeListeners() {
		return (ChangeListener[]) this.listenerList
			.getListeners(ChangeListener.class);
	}

	/**
	 * Returns the column count.
	 */
	public int getColumnCount() {
		return this.column.getCount();
	}

	public DefaultMutableTreeNode getHorizontalRoot() {
		return (DefaultMutableTreeNode) this.horizontalTreeModel.getRoot();
	}

	/**
	 * Returns the treeModel.
	 * @return TreeModel
	 */
	public TreeModel getHorizontalTreeModel() {
		return this.horizontalTreeModel;
	}

	/**
	 * Returns the row count.
	 */
	public int getRowCount() {
		return this.row.getCount();
	}

	public DefaultMutableTreeNode getVerticalRoot() {
		return (DefaultMutableTreeNode) this.verticalTreeModel.getRoot();
	}

	/**
	 * Returns the verticalTreeModel.
	 * @return TreeModel
	 */
	public TreeModel getVerticalTreeModel() {
		return this.verticalTreeModel;
	}

	/**
	 * @return
	 */
	public boolean isAnimated() {
		return this.animated;
	}

	/**
	 * Loads a cluster hierarchy from the passed <code>File</code>.
	 */
	public void loadClustering(final File file) {
		BufferedInputStream bis;
		SAXParser parser;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			parser = SAXParserFactory.newInstance().newSAXParser();
			parser.parse(bis, new ClusteringHandler(this));
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
			this.fireStateChanged();
		}
	}

	private String nodeInfo(final StateNode node) {
		String indent = "";
		for (int i = 0; i < node.getLevel(); i++) {
			indent += "\t";
		}
		final String openTag = "<node ";
		final String nameAttr = "name=\"";
		final String closeTag = ">\n";
		final String str =
			indent + openTag + nameAttr + node.getUserObject() + "\""
					+ closeTag;
		return str;
	}

	/**
	 * This methods performs final steps after the
	 * clustering is actualy computed e.g. it handles notification
	 * of components interested in cluster hierarchies etc.
	 */
	public void postClusterize() {
		if (!this.animated) {
			this.row.enableNotify();
			this.column.enableNotify();
			this.row.notifyListeners();
			this.column.notifyListeners();
		}
		((DefaultTreeModel) this.horizontalTreeModel)
			.nodeChanged((TreeNode) this.horizontalTreeModel.getRoot());
		((DefaultTreeModel) this.verticalTreeModel)
			.nodeChanged((TreeNode) this.verticalTreeModel.getRoot());
	}

	/**
	 * This methods performs initialization steps before the
	 * clustering is actualy computed e.g. it resets the TreeModels.
	 */
	public void preClusterize() {
		if (!this.animated) {
			this.row.disableNotify();
			this.column.disableNotify();
		}
		((StateNode) this.horizontalTreeModel.getRoot()).removeAllChildren();
		((StateNode) this.verticalTreeModel.getRoot()).removeAllChildren();
	}

	/**
	 * Removes a <code>ChangeListener</code>.
	 * 
	 * @param l the <code>ChangeListener</code> to remove
	 * 
	 * @see #addChangeListener
	 * @see BoundedRangeModel#removeChangeListener
	 */
	public void removeChangeListener(final ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * @param b
	 */
	public void setAnimated(final boolean b) {
		this.animated = b;
	}

	/**
	 * Sets the treeModel.
	 * @param treeModel The treeModel to set
	 */
	public void setHorizontalTreeModel(final TreeModel treeModel) {
		this.horizontalTreeModel = treeModel;
	}

	/**
	 * This method sets the metric used to compute 
	 * the distance between rows and between colums.
	 */
	public void setMetric(final int select) {
		switch (select) {
			case SCALAR_PRODUCT :
				this.metric = new ScalarProduct();
				break;
			default :
				this.metric = new ScalarProduct();
		}
	}

	/**
	 * This method sets the metric used to compute
	 * the distance between rows and columns to the 
	 * given custom metric.
	 */
	public void setMetric(final Metric custom) {
		this.metric = custom;
	}

	/**
	 * This method sets the tolerance used 
	 * in the clusterization algorithm.
	 */
	public void setTolerance(final int t) {
		this.tolerance = t;
	}

	/**
	 * Sets the verticalTreeModel.
	 * @param verticalTreeModel The verticalTreeModel to set
	 */
	public void setVerticalTreeModel(final TreeModel verticalTreeModel) {
		this.verticalTreeModel = verticalTreeModel;
	}

	/**
	 * Stores a cluster hierarchy in the passed <code>File</code>.
	 */
	public void storeClustering(final File file) {
		BufferedOutputStream bof;
		final String header =
			"<?xml version='1.0' encoding='utf-8'?>\n<clustering>\n";
		final String footer = "</clustering>";
		try {
			bof = new BufferedOutputStream(new FileOutputStream(file));
			bof.write(header.getBytes());
			this.storeTreeModel(bof, this.horizontalTreeModel, "horizontal");
			this.storeTreeModel(bof, this.verticalTreeModel, "vertical");
			bof.write(footer.getBytes());
			bof.flush();
		}
		catch (final FileNotFoundException fex) {
			fex.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void storeNode(final BufferedOutputStream bof, final StateNode node) {
		String indent = "";
		for (int i = 0; i < node.getLevel(); i++) {
			indent += "\t";
		}
		final String footer = indent + "</node>\n";
		try {
			bof.write(this.nodeInfo(node).getBytes());
			final Enumeration e = node.children();
			while (e.hasMoreElements()) {
				this.storeNode(bof, (StateNode) e.nextElement());
			}
			bof.write(footer.getBytes());
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void storeTreeModel(
		final BufferedOutputStream bof,
		final TreeModel tree,
		final String orientation) {
		final String header =
			"<tree orientation=\"" + orientation + "\"" + " size=\""
					+ ((StateNode) tree.getRoot()).getLeafCount() + "\""
					+ ">\n";
		final String footer = "</tree>\n";
		try {
			bof.write(header.getBytes());
			this.storeNode(bof, (StateNode) tree.getRoot());
			bof.write(footer.getBytes());
			bof.flush();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
