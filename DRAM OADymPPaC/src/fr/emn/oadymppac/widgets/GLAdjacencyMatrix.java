package fr.emn.oadymppac.widgets;

import fr.emn.oadymppac.graph.AdjacencyMatrixRowColumn;
import fr.emn.oadymppac.graph.EdgeWeightGetter;
import fr.emn.oadymppac.graph.NamedGraphDelegator;
import fr.emn.oadymppac.graph.clustering.AbstractMatrixClusterizer;
import fr.emn.oadymppac.graph.clustering.DefaultMatrixClusterizer;
import fr.emn.oadymppac.graph.clustering.MatrixClusterizer;
import fr.emn.oadymppac.utils.FloatStack;
import fr.emn.oadymppac.utils.MinMax;
import gl4java.GLEnum;
import gl4java.awt.GLCanvas;
import gl4java.utils.glut.GLUTEnum;
import gl4java.utils.glut.fonts.GLUTFuncLightImplWithFonts;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import salvo.jesus.graph.DirectedGraph;
import salvo.jesus.graph.Edge;
import salvo.jesus.graph.Graph;
import salvo.jesus.graph.Vertex;

/**
 * A Swing component to visualize a graph as an adjacency matrix.
 * Copyright (c) 2002 Ecole des Mines de Nantes, Mohammad Ghoniem
 * and Jean-Daniel Fekete
 * 
 * @version $Revision: 1.3 $
 * @author Mohammad Ghoniem and Jean-Daniel Fekete
 */
public class GLAdjacencyMatrix extends GLCanvas implements MouseMotionListener,
		ChangeListener, ClusterListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1559087386883755291L;
	static float[] colorRamp =
		{ 255 / 255.0f, 255 / 255.0f, 217 / 255.0f, 255 / 255.0f, 237 / 255.0f,
				248 / 255.0f, 177 / 255.0f, 255 / 255.0f, 199 / 255.0f,
				233 / 255.0f, 180 / 255.0f, 255 / 255.0f, 127 / 255.0f,
				205 / 255.0f, 187 / 255.0f, 255 / 255.0f, 65 / 255.0f,
				182 / 255.0f, 196 / 255.0f, 255 / 255.0f, 29 / 255.0f,
				145 / 255.0f, 192 / 255.0f, 255 / 255.0f, 56 / 255.0f,
				108 / 255.0f, 176 / 255.0f, 255 / 255.0f, 12 / 255.0f,
				44 / 255.0f, 132 / 255.0f, 255 / 255.0f };
	static float[] saturatedColorRamp = { 255 / 255.0f, 255 / 255.0f,
			255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 0 / 255.0f, 0 / 255.0f,
			255 / 255.0f, 0 / 255.0f, 255 / 255.0f, 0 / 255.0f, 255 / 255.0f,
			0 / 255.0f, 0 / 255.0f, 255 / 255.0f, 255 / 255.0f, 255 / 255.0f,
			255 / 255.0f, 0 / 255.0f, 255 / 255.0f, 255 / 255.0f, 0 / 255.0f,
			255 / 255.0f, 255 / 255.0f, 0 / 255.0f, 255 / 255.0f, 255 / 255.0f,
			255 / 255.0f, 0 / 255.0f, 0 / 255.0f, 0 / 255.0f, 255 / 255.0f };
	static final float TOP = 120;
	static final float BOTTOM = 34;

	private AdjacencyMatrixRowColumn row;
	private AdjacencyMatrixRowColumn column;
	private MatrixClusterizer clusterizer;
	NamedGraphDelegator graph;
	EdgeWeightGetter weightGetter;
	float maxIntensity = 1.0f;
	int labelSize = 100; // size dedicated to labels
	int width;
	int height;
	int colorTexture;
	FloatStack vertices;
	FloatStack texCoords;
	BoundedRangeModel visibleEdges;
	Fisheye lens;

	//    // change that to a list of ranges
	int focusCol;
	int focusRow;
	int focusCount;

	float focusX;
	float focusY;
	float labelHeight;
	GLUTFuncLightImplWithFonts glut;
	float[] hiliteColor = { 1, 0, 0, 0.3f };

	int orientation;

	/**
	 * Creates a new GLAdjacencyMatrix object from a row and a column
	 * with the given dimensions.
	 * 
	 * @param row the <code>AdjacencyMatrixRowColumn</code> controlling the rows
	 * @param column the <code>AdjacencyMatrixRowColumn</code> controlling the columns
	 */
	public GLAdjacencyMatrix(
		final AdjacencyMatrixRowColumn row,
		final AdjacencyMatrixRowColumn column,
		final int width,
		final int height) {
		super(width, height);
		this.clusterizer = new DefaultMatrixClusterizer(row, column);
		this.initData(row, column);
		//setGraph(column.getGraph());
		this.colorTexture = 0;
		this.vertices = new FloatStack();
		this.texCoords = new FloatStack();
		this.focusCount = 5;
		this.focusCol = -this.focusCount;
		this.focusRow = -this.focusCount;
		this.labelHeight = 13;

		this.setLens(new Fisheye());
		this.addMouseMotionListener(this);
		this.glut = new GLUTFuncLightImplWithFonts(this.gl, this.glu);
	}

	/**
	 * Creates a new GLAdjacencyMatrix object from
	 * a graph.
	 * 
	 * @param row the <code>AdjacencyMatrixRowColumn</code> controlling the rows
	 * @param column the <code>AdjacencyMatrixRowColumn</code> controlling the columns
	 */
	public GLAdjacencyMatrix(final NamedGraphDelegator g) {
		this(
			new AdjacencyMatrixRowColumn(g),
			new AdjacencyMatrixRowColumn(g),
			600,
			600);
	}

	/**
	 * Creates a new GLAdjacencyMatrix object from a graph with
	 * the given dimensions.
	 * 
	 * @param g the <code>NamedGraphDelegator</code> that handles the underlying graph
	 * @param width the width of the grid
	 * @param height the height of the grid
	 */
	public GLAdjacencyMatrix(
		final NamedGraphDelegator g,
		final int width,
		final int height) {
		this(
			new AdjacencyMatrixRowColumn(g),
			new AdjacencyMatrixRowColumn(g),
			width,
			height);
	}

	void addRect(
		final float x1,
		final float y1,
		final float x2,
		final float y2,
		final float t) {
		if (this.vertices.size() > 100000) {
			this.flush();
		}

		final float z1 = this.fisheye(x1, y1);
		final float z2 = this.fisheye(x2, y1);
		final float z3 = this.fisheye(x2, y2);
		final float z4 = this.fisheye(x1, y2);

		final boolean allNull = z1 == 0 && z2 == 0 && z3 == 0 && z4 == 0;
		final boolean anyNonNull = z1 != 0 && z2 != 0 || z3 != 0 || z4 != 0;
		final boolean shouldSplit = anyNonNull && !allNull;

		if (shouldSplit) {
			final float x12 = (x1 + x2) / 2;
			final float y12 = (y1 + y2) / 2;
			final float z12 = this.fisheye(x12, y1);
			final float z23 = this.fisheye(x2, y12);
			final float z34 = this.fisheye(x12, y2);
			final float z41 = this.fisheye(x1, y12);
			final float zmid = this.fisheye(x12, y12);
			this.addRect(x1, y1, x12, y12, z1, z12, zmid, z41, t);
			this.addRect(x12, y1, x2, y12, z12, z2, z23, zmid, t);
			this.addRect(x1, y12, x12, y2, z41, zmid, z34, z4, t);
			this.addRect(x12, y12, x2, y2, zmid, z23, z3, z34, t);
		}
		else {
			this.addRect(x1, y1, x2, y2, z1, z2, z3, z4, t);
		}
	}

	void addRect(
		final float x1,
		final float y1,
		final float x2,
		final float y2,
		final float z1,
		final float z2,
		final float z3,
		final float z4,
		final float t) {
		this.vertices.push(x1);
		this.vertices.push(y1);
		this.vertices.push(z1);
		this.texCoords.push(t);

		this.vertices.push(x2);
		this.vertices.push(y1);
		this.vertices.push(z2);
		this.texCoords.push(t);

		this.vertices.push(x2);
		this.vertices.push(y2);
		this.vertices.push(z3);
		this.texCoords.push(t);

		this.vertices.push(x2);
		this.vertices.push(y2);
		this.vertices.push(z3);
		this.texCoords.push(t);

		this.vertices.push(x1);
		this.vertices.push(y2);
		this.vertices.push(z4);
		this.texCoords.push(t);

		this.vertices.push(x1);
		this.vertices.push(y1);
		this.vertices.push(z1);
		this.texCoords.push(t);
	}

	/**
	 * @see fr.emn.oadymppac.widgets.ClusterListener#clusterChanged(ClusterEvent)
	 */
	public void clusterChanged(final ClusterEvent event) {
		if (event.getSource() instanceof GLTreeBrowser) {
			this.orientation =
				((GLTreeBrowser) event.getSource()).getOrientation();
		}
		else if (event.getSource() instanceof JTreeBrowser) {
			this.orientation =
				((JTreeBrowser) event.getSource()).getOrientation();
		}
		switch (event.getType()) {
			case ClusterEvent.SINGLE_SWAP :
				this.swap(
					event.node1,
					event.node2,
					this.orientation == IcicleTreeBrowser.HORIZONTAL ? this
						.getColumn() : this.getRow());
				break;
			case ClusterEvent.ARRAY_SWAP :
				this.swapArrays(
					event.nodes1,
					event.nodes2,
					this.orientation == IcicleTreeBrowser.HORIZONTAL ? this
						.getColumn() : this.getRow());
				break;
			case ClusterEvent.SINGLE_INSERT :
				this.insert(
					event.node2,
					event.node1,
					this.orientation == IcicleTreeBrowser.HORIZONTAL ? this
						.getColumn() : this.getRow());
				break;
			case ClusterEvent.ARRAY_INSERT :
				this.insert(
					event.nodes1,
					event.node1,
					this.orientation == IcicleTreeBrowser.HORIZONTAL ? this
						.getColumn() : this.getRow());
				break;
			default :
				return;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param minmax DOCUMENT ME!
	 */
	public void computeIntensity(final MinMax minmax) {
		this.computeIntensity(minmax, this.graph.getEdgeSet().iterator());
	}

	void computeIntensity(final MinMax minmax, final Iterator it) {
		while (it.hasNext()) {
			final Edge edge = (Edge) it.next();
			final float intensity = this.getIntensity(edge);
			minmax.setMin(Math.min(minmax.getMin(), intensity));
			minmax.setMax(Math.max(minmax.getMax(), intensity));
		}
	}

	/**
	 * Compute the size of columns or rows
	 * given their overall length and number of elements.
	 * 
	 * @param length the length of the dimension (total width or heigth)
	 * @param elements number of elements in the dimension
	 * @return the size.
	 */
	public float computeSize(final float length, final int elements) {
		return (length - this.labelSize) / elements;
	}

	/**
	 * Computes the visible intensity for the edges of the graph,
	 * and stores the bounds of the intensity range in the passed
	 * <code>MinMax</code>.
	 * 
	 * @param minmax DOCUMENT ME!
	 */
	public void computeVisibleIntensity(final MinMax minmax) {
		final HashSet doneVertices = new HashSet();
		Iterator it;

		for (it = this.row.getVertices().iterator(); it.hasNext();) {
			final String label = (String) it.next();

			if (!doneVertices.contains(label)) {
				doneVertices.add(label);

				final Vertex v = this.graph.getVertex(label);

				if (v != null) {
					this.computeIntensity(minmax, this.graph
						.getEdges(v)
						.iterator());
				}
			}
		}
	}

	/**
	 * @see gl4java.awt.GLCanvas#display()
	 */
	public synchronized void display() {
		/* Standard GL4Java Init */
		if (this.glj.gljMakeCurrent() == false) {
			System.out.println("problem with gljMakeCurrent");
			return;
		}

		float w = this.computeSize(this.width, this.column.getCount());
		float h = this.computeSize(this.height, this.row.getCount());

		if (w < h) {
			h = w;
		}
		else {
			w = h;
		}

		this.gl
			.glClear(GLEnum.GL_COLOR_BUFFER_BIT | GLEnum.GL_DEPTH_BUFFER_BIT);
		this.glj.gljCheckGL();
		this.gl.glBindTexture(GLEnum.GL_TEXTURE_1D, this.colorTexture);
		this.gl.glEnable(GLEnum.GL_TEXTURE_1D);
		this.gl.glEnableClientState(GLEnum.GL_VERTEX_ARRAY);
		this.gl.glEnableClientState(GLEnum.GL_TEXTURE_COORD_ARRAY);
		this.gl.glEnable(GLEnum.GL_DEPTH_TEST);

		float x = this.labelSize;
		float y = this.labelSize;

		if (this.lens != null) {
			this.lens.beginTransform(this, this.width, this.height);
		}

		int edges = 0;
		final Graph g = this.row.getGraph().getUnderlyingGraph();
		final DirectedGraph dgraph =
			g instanceof DirectedGraph ? (DirectedGraph) g : null;

		try {
			synchronized (g) {
				for (int i = 0; i < this.column.getCount(); i++) {
					final String columnName = this.column.getName(i);
					final Vertex vertex = this.graph.getVertex(columnName);

					if (vertex == null) {
						continue;
					}

					final java.util.List l =
						dgraph != null ? dgraph.getOutgoingEdges(vertex) : g
							.getEdges(vertex);

					for (final ListIterator it = l.listIterator(); it.hasNext();) {
						final Edge edge = (Edge) it.next();
						final String rowName =
							edge.getOppositeVertex(vertex).getLabel();
						final int r = this.row.indexOf(rowName);
						y = this.labelSize + r * h;
						if (this.focusRow == r && this.focusCol == i) {
							//							picked++;
							//							System.out.println("Pick "+edge);
							//							if (picked > 1)
							//								System.out.println(picked+" times");
						}

						final float v =
							this.getIntensity(edge) / this.maxIntensity;
						//System.out.println(v);

						if (v <= 0) {
							continue;
						}

						edges++;
						this.addRect(x, y, x + w, y + h, v);
					}

					x += w;
				}

			}
		}
		finally {
			this.flush();
			if (this.lens != null) {
				this.lens.endTransform(this);
			}
		}

		if (this.visibleEdges != null) {
			this.visibleEdges.setValue(edges);
		}

		this.gl.glDisable(GLEnum.GL_TEXTURE_1D);
		this.gl.glDisable(GLEnum.GL_DEPTH_TEST);

		this.gl.glColor3f(0, 0, 0);
		this.gl.glEnable(GLEnum.GL_BLEND);
		this.gl.glBlendFunc(GLEnum.GL_SRC_ALPHA, GLEnum.GL_ONE_MINUS_SRC_ALPHA);

		this.gl.glPushMatrix();
		this.displayLabels(this.row, this.focusRow, h, 1);
		this.gl.glPopMatrix();

		this.gl.glPushMatrix();
		this.gl.glRotatef(90, 0, 0, 1);
		this.displayLabels(this.column, this.focusCol, w, -1);
		this.gl.glPopMatrix();

		this.gl.glDisableClientState(GLEnum.GL_TEXTURE_COORD_ARRAY);
		this.displayCursor(w, h);

		this.gl.glDisable(GLEnum.GL_BLEND);
		this.glj.gljSwap();
		this.glj.gljCheckGL();
		this.glj.gljFree();

		this.gl.glDisableClientState(GLEnum.GL_VERTEX_ARRAY);

		this.gl.glBindTexture(GLEnum.GL_TEXTURE_1D, 0);
	}

	void displayCursor(final float w, final float h) {
		this.gl.glColor4fv(this.hiliteColor);
		//gl.glColor4f(1, 0, 0, 0.9f);
		this.lens.beginTransform(this, this.width, this.height);

		if (this.focusRow >= 0 && this.focusRow < this.row.getCount()) {
			float x = this.labelSize;
			final float y = this.toRowPosition(this.focusRow, h);

			this.addRect(0, y + h / 2 - this.labelHeight / 2f, x, y + h / 2
					+ this.labelHeight / 2f, 0);

			for (int i = 0; i < this.column.getCount(); i++) {
				this.addRect(x, y, x + w, y + h, 0);
				x += w;
			}
		}

		if (this.focusCol >= 0 && this.focusCol < this.column.getCount()) {
			final float x = this.toColumnPosition(this.focusCol, w);
			float y = this.labelSize;

			this.addRect(x + w / 2 - this.labelHeight / 2, 0, x + w / 2
					+ this.labelHeight / 2, y, 0);
			for (int i = 0; i < this.row.getCount(); i++) {
				this.addRect(x, y, x + w, y + h, 0);
				y += h;
			}
		}
		this.flush();
		this.lens.endTransform(this);
	}

	void displayLabels(
		final AdjacencyMatrixRowColumn rowOrCol,
		final int pos,
		final float w,
		final int direction) {

		float y = direction * this.labelSize;

		if (w >= this.labelHeight) {
			final float delta = -direction * w * 10;

			y += -GLAdjacencyMatrix.BOTTOM / 10f - (w - this.labelHeight) / 2;
			if (direction == 1) {
				y += w;
			}

			this.gl.glTranslatef(0, y, 0);
			this.gl.glScalef(0.1f, -0.1f, 0.1f);

			for (int i = 0; i < rowOrCol.getCount(); i++) {
				this.gl.glPushMatrix();

				this.glut.glutStrokeString(
					GLUTEnum.GLUT_STROKE_ROMAN,
					rowOrCol.getName(i));
				this.gl.glPopMatrix();
				this.gl.glTranslatef(0, delta, 0);
			}
		}
		else {
			final float delta = -direction * this.labelHeight * 10;
			int i = pos - this.focusCount / 2;
			int last = pos + (this.focusCount + 1) / 2;

			if (i < 0) {
				i = 0;
			}

			if (last > rowOrCol.getCount()) {
				last = rowOrCol.getCount();
			}
			y +=
				direction * (i * w - (pos - i) * this.labelHeight)
						+ GLAdjacencyMatrix.BOTTOM / 10f;

			if (direction == 1) {
				y += w;
			}

			this.gl.glTranslatef(0, y, 0);
			this.gl.glScalef(0.1f, -0.1f, 0.1f);

			while (i < last) {
				this.gl.glPushMatrix();
				this.glut.glutStrokeString(
					GLUTEnum.GLUT_STROKE_ROMAN,
					rowOrCol.getName(i));
				this.gl.glPopMatrix();
				this.gl.glTranslatef(0, delta, 0);
				i++;
			}
		}
	}

	Edge edgeAt(final int r, final int c) {
		final Vertex v1 = this.graph.getVertex(this.row.getName(r));
		final Vertex v2 = this.graph.getVertex(this.column.getName(c));

		final List l = this.graph.getEdges(v1);
		for (final Iterator it = l.iterator(); it.hasNext();) {
			final Edge e = (Edge) it.next();
			if (e.getOppositeVertex(v1) == v2) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Compute the Z displacement of a vertex located at (x, y)
	 * 
	 * @param x the X coordinate
	 * @param y the Y coordinate
	 * 
	 * @return the Z displacement
	 */
	public float fisheye(final float x, final float y) {
		if (this.lens != null) {
			return this.lens.pointAltitude(x, y);
		}
		return 0;
	}

	protected void flush() {
		this.gl.glVertexPointer(3, GLEnum.GL_FLOAT, 0, this.vertices.getData());
		this.gl.glTexCoordPointer(
			1,
			GLEnum.GL_FLOAT,
			0,
			this.texCoords.getData());
		this.gl.glDrawArrays(GLEnum.GL_TRIANGLES, 0, this.texCoords.size());

		//System.out.println("Sent " + vertices.size()/2 + " vertices");
		this.vertices.clear();
		this.texCoords.clear();
	}

	/**
	 * Returns the clusterizer.
	 * @return DefaultMatrixClusterizer
	 */
	public MatrixClusterizer getClusterizer() {
		return this.clusterizer;
	}

	/**
	 * Returns the column.
	 * 
	 * @return AdjacencyMatrixRowColumn
	 */
	public AdjacencyMatrixRowColumn getColumn() {
		return this.column;
	}

	/**
	 * Returns the focusCount.
	 * 
	 * @return float
	 */
	public float getFocusCount() {
		return this.focusCount;
	}

	/**
	 * Returns the hiliteColor.
	 * 
	 * @return float[]
	 */
	public float[] getHiliteColor() {
		return this.hiliteColor;
	}

	/**
	 * Returns the weight of the passed weight, 
	 * if a <code>weightGetter</code> is available. 
	 * Otherwise, it returns 1.
	 * 
	 * @param e DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float getIntensity(final Edge e) {
		if (this.weightGetter != null) {
			return this.weightGetter.getWeight(e);

		}

		return 1.0f;
	}

	/**
	 * Looks up the edge corresponding the given column and row
	 * indexes and calls the previous method.
	 * 
	 * @param c DOCUMENT ME!
	 * @param r DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float getIntensity(final int c, final int r) {
		return this.getIntensity(this.graph.getEdge(
			this.graph.getVertex(this.column.getName(c)),
			this.graph.getVertex(this.row.getName(r))));
	}

	/**
	 * Calls the method <code>getIntensity(EdgeWithHistory e)</code>
	 * with the edge corresponding to the given column and row.
	 * 
	 * @param columnName DOCUMENT ME!
	 * @param rowName DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public float getIntensity(final String columnName, final String rowName) {
		return this.getIntensity(this.graph.getEdge(
			this.graph.getVertex(columnName),
			this.graph.getVertex(rowName)));
	}

	/**
	 * Returns the labelHeight.
	 * 
	 * @return float
	 */
	public float getLabelHeight() {
		return this.labelHeight;
	}

	/**
	 * Returns the labelSize.
	 * 
	 * @return int
	 */
	public int getLabelSize() {
		return this.labelSize;
	}

	/**
	 * Returns the lens.
	 * 
	 * @return Fisheye
	 */
	public Fisheye getLens() {
		return this.lens;
	}

	/**
	 * Returns the maxIntensity.
	 * 
	 * @return float
	 */
	public float getMaxIntensity() {
		return this.maxIntensity;
	}

	/**
	 * Returns the row.
	 * 
	 * @return AdjacencyMatrixRowColumn
	 */
	public AdjacencyMatrixRowColumn getRow() {
		return this.row;
	}

	/**
	 * Returns the visibleEdges.
	 * 
	 * @return BoundedRangeModel
	 */
	public BoundedRangeModel getVisibleEdges() {
		return this.visibleEdges;
	}

	/**
	 * Returns the weightGetter.
	 * 
	 * @return EdgeWeightGetter
	 */
	public EdgeWeightGetter getWeightGetter() {
		return this.weightGetter;
	}

	/**
	 * @see gl4java.awt.GLCanvas#init()
	 */
	public void init() {
		this.gl.glClearColor(1, 1, 1, 1);
		this.gl.glShadeModel(GLEnum.GL_FLAT);
		this.gl.glDisable(GLEnum.GL_DITHER);
		this.gl.glEnable(GLEnum.GL_POLYGON_SMOOTH);
		this.gl.glEnable(GLEnum.GL_LINE_SMOOTH);

		final int[] tex = new int[1];
		this.gl.glGenTextures(1, tex);
		this.colorTexture = tex[0];
		this.gl.glBindTexture(GLEnum.GL_TEXTURE_1D, this.colorTexture);
		this.gl.glTexParameteri(
			GLEnum.GL_TEXTURE_1D,
			GLEnum.GL_TEXTURE_WRAP_S,
			GLEnum.GL_CLAMP);
		this.gl.glTexImage1D(
			GLEnum.GL_TEXTURE_1D,
			0,
			GLEnum.GL_RGBA,
			8,
			0,
			GLEnum.GL_RGBA,
			GLEnum.GL_FLOAT,
			GLAdjacencyMatrix.colorRamp);
		this.gl.glTexEnvf(
			GLEnum.GL_TEXTURE_ENV,
			GLEnum.GL_TEXTURE_ENV_MODE,
			GLEnum.GL_REPLACE);
		this.gl.glTexParameteri(
			GLEnum.GL_TEXTURE_1D,
			GLEnum.GL_TEXTURE_MAG_FILTER,
			GLEnum.GL_LINEAR);
		this.gl.glTexParameteri(
			GLEnum.GL_TEXTURE_1D,
			GLEnum.GL_TEXTURE_MIN_FILTER,
			GLEnum.GL_LINEAR);

		this.glj.gljCheckGL();
	}

	public void initData(
		final AdjacencyMatrixRowColumn row,
		final AdjacencyMatrixRowColumn column) {
		this.setRow(row);
		this.setColumn(column);
		this.graph = column.getGraph();
		//row.populateMatrix();
		//column.populateMatrix();
		this.setMinMaxIntensity();
	}

	public void insert(
		final String node,
		final String previous,
		final AdjacencyMatrixRowColumn rc) {
		if (rc.indexOf(node) != -1) {
			rc.remove(node);
		}
		rc.insert(rc.indexOf(previous) + 1, node);
	}

	public void insert(
		final String[] nodes,
		final String previous,
		final AdjacencyMatrixRowColumn rc) {
		String name = previous;

		for (int i = 0; i < nodes.length; i++) {
			this.insert(nodes[i], name, rc);
			name = nodes[i];
		}
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
	 */
	public void mouseDragged(final MouseEvent e) {
		this.focusX = e.getX();
		this.focusY = e.getY();

		float w = this.computeSize(this.width, this.column.getCount());
		float h = this.computeSize(this.height, this.row.getCount());

		if (w < h) {
			h = w;
		}
		else {
			w = h;
		}

		this.focusCol = (int) ((this.focusX - this.labelSize) / w);
		this.focusRow = (int) ((this.focusY - this.labelSize) / h);

		if (this.lens != null) {
			this.lens.setFocus(this.focusX, this.focusY);
		}
		this.repaint();
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
	 */
	public void mouseMoved(final MouseEvent e) {
	}

	Edge pickEdge(final int x, final int y) {
		float w = this.computeSize(this.width, this.column.getCount());
		float h = this.computeSize(this.height, this.row.getCount());

		if (w < h) {
			h = w;
		}
		else {
			w = h;
		}

		final int row = (int) ((x - this.labelSize) / w);
		final int col = (int) ((y - this.labelSize) / h);
		return this.edgeAt(row, col);
	}

	/**
	 * @see gl4java.awt.GLCanvas#preInit()
	 */
	public void preInit() {
		this.doubleBuffer = true;
	}

	/**
	 * @see gl4java.awt.GLCanvas#reshape(int, int)
	 */
	public void reshape(final int w, final int h) {
		//System.out.println("w = "+w+" h="+h);
		this.width = w;
		this.height = h;
		this.gl.glViewport(0, 0, w, h);
		this.gl.glMatrixMode(GLEnum.GL_PROJECTION);
		this.gl.glLoadIdentity();
		this.gl.glOrtho(0, this.width, this.height, 0, -1, 1);
		this.gl.glMatrixMode(GLEnum.GL_MODELVIEW);
		this.gl.glLoadIdentity();

		this.glj.gljCheckGL();
	}

	/**
	 * Sets the clusterizer.
	 * @param clusterizer The clusterizer to set
	 */
	public void setClusterizer(final AbstractMatrixClusterizer clusterizer) {
		if (this.clusterizer != null) {
			System.out.println("saving current trees");

			clusterizer.setHorizontalTreeModel(this.clusterizer
				.getHorizontalTreeModel());
			clusterizer.setVerticalTreeModel(this.clusterizer
				.getVerticalTreeModel());
		}

		this.clusterizer = clusterizer;
	}

	/**
	 * Sets the column.
	 * 
	 * @param column The column to set
	 */
	public void setColumn(AdjacencyMatrixRowColumn column) {
		if (this.column != null) {
			this.column.removeChangeListener(this);
		}

		if (column == null) {
			column = this.row;
		}

		this.column = column;
		this.column.addChangeListener(this);
		this.repaint();
	}

	/**
	 * Sets the focusCount.
	 * 
	 * @param focusCount The focusCount to set
	 */
	public void setFocusCount(final int focusCount) {
		this.focusCount = focusCount;
	}

	public void setGraph(final NamedGraphDelegator g) {
		if (this.graph == g) {
			return;
		}

		this.graph = g;
	}

	/**
	 * Sets the hiliteColor.
	 * 
	 * @param r The hiliteColor to set
	 * @param g DOCUMENT ME!
	 * @param b DOCUMENT ME!
	 * @param a DOCUMENT ME!
	 */
	public void setHiliteColor(
		final float r,
		final float g,
		final float b,
		final float a) {
		this.hiliteColor[0] = r;
		this.hiliteColor[1] = g;
		this.hiliteColor[2] = b;
		this.hiliteColor[3] = a;
	}

	/**
	 * Sets the labelHeight.
	 * 
	 * @param labelHeight The labelHeight to set
	 */
	public void setLabelHeight(final float labelHeight) {
		this.labelHeight = labelHeight;
	}

	/**
	 * Sets the labelSize.
	 * 
	 * @param labelSize The labelSize to set
	 */
	public void setLabelSize(final int labelSize) {
		this.labelSize = labelSize;
	}

	/**
	 * Sets the lens.
	 * 
	 * @param lens The lens to set
	 */
	public void setLens(final Fisheye lens) {
		if (this.lens != null) {
			this.lens.removeChangeListener(this);
		}
		this.lens = lens;
		if (this.lens != null) {
			this.lens.addChangeListener(this);
			lens.setFocus(this.focusX, this.focusY);
		}
		this.repaint();
	}

	/**
	 * Sets the maxIntensity.
	 * 
	 * @param maxIntensity The maxIntensity to set
	 */
	public void setMaxIntensity(final float maxIntensity) {
		this.maxIntensity = maxIntensity;

		//System.out.println("Max intensity = "+maxIntensity);
		this.repaint();
	}

	/**
	 * Determines the maximum weight in the 
	 * edgeset of the graph and sets it as the maximum
	 * value for intensity multiplied by the passed coef.
	 * 
	 * @param rel DOCUMENT ME!
	 */
	public void setMaxIntensityRel(final float rel) {
		final MinMax minmax = new MinMax();
		this.computeIntensity(minmax);
		this.setMaxIntensity(minmax.getMax() * rel);
	}

	/**
	 * Determines the maximum weight in the 
	 * edgeset of the graph and sets it as the maximum
	 * value for intensity.
	 */
	public void setMinMaxIntensity() {
		final MinMax minmax = new MinMax();
		this.computeIntensity(minmax);
		this.setMaxIntensity(minmax.getMax());
	}

	/**
	 * Sets the row.
	 * 
	 * @param row The row to set
	 */
	public void setRow(AdjacencyMatrixRowColumn row) {
		if (this.row != null) {
			this.row.removeChangeListener(this);
		}

		if (row == null) {
			row = this.column;
		}

		this.row = row;
		this.row.addChangeListener(this);
		this.repaint();
	}

	/**
	 * Sets the visibleEdges.
	 * 
	 * @param visibleEdges The visibleEdges to set
	 */
	public void setVisibleEdges(final BoundedRangeModel visibleEdges) {
		this.visibleEdges = visibleEdges;
	}

	/**
	 * Sets the weightGetter.
	 * 
	 * @param weightGetter The weightGetter to set
	 */
	public void setWeightGetter(final EdgeWeightGetter weightGetter) {
		this.weightGetter = weightGetter;
	}

	/**
	 * @see javax.swing.event.ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(final ChangeEvent e) {
		this.repaint();
	}

	public void swap(
		final String node1,
		final String node2,
		final AdjacencyMatrixRowColumn rc) {
		System.out.println("swapping " + node1 + " & " + node2);
		rc.swap(node1, node2);
	}

	public void swapArrays(
		String[] nodes1,
		String[] nodes2,
		final AdjacencyMatrixRowColumn rc) {
		if (nodes1.length > nodes2.length) {
			final String[] buff = nodes1;
			nodes1 = nodes2;
			nodes2 = buff;
		}

		for (int i = 0; i < nodes1.length; i++) {
			rc.swap(nodes1[i], nodes2[i]);
		}

		for (int j = nodes1.length; j < nodes2.length; j++) {
			this.insert(nodes2[j], nodes2[j - 1], rc);
		}
	}

	/**
	 * Compute the X position of a column
	 * 
	 * @param col the column
	 * @param w the column width
	 * 
	 * @return the X position
	 */
	public float toColumnPosition(final int col, final float w) {
		return this.labelSize + col * w;
	}

	/**
	 * Compute the Y position of a row
	 * 
	 * @param row the row
	 * @param h the row height
	 * 
	 * @return the Y position
	 */
	public float toRowPosition(final int row, final float h) {
		return this.labelSize + row * h;
	}
}