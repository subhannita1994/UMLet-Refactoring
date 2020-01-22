/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	(c) by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.test.contrib;

import java.awt.Point;

import junit.framework.TestCase;
// JUnitDoclet begin import
import org.jhotdraw.contrib.GraphicalCompositeFigure;
import org.jhotdraw.contrib.SimpleLayouter;
import org.jhotdraw.figures.RectangleFigure;
// JUnitDoclet end import

/*
 * Generated by JUnitDoclet, a tool provided by
 * ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org
 * and www.objectfab.de for informations about
 * the tool, the licence and the authors.
 */

// JUnitDoclet begin javadoc_class
/**
 * TestCase GraphicalCompositeFigureTest is generated by
 * JUnitDoclet to hold the tests for GraphicalCompositeFigure.
 * @see org.jhotdraw.contrib.GraphicalCompositeFigure
 */
// JUnitDoclet end javadoc_class
public class GraphicalCompositeFigureTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private GraphicalCompositeFigure graphicalcompositefigure;
	// JUnitDoclet end class

	/**
	 * Constructor GraphicalCompositeFigureTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public GraphicalCompositeFigureTest(String name) {
		// JUnitDoclet begin method GraphicalCompositeFigureTest
		super(name);
		// JUnitDoclet end method GraphicalCompositeFigureTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public GraphicalCompositeFigure createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new GraphicalCompositeFigure();
		// JUnitDoclet end method testcase.createInstance
	}

	/**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
	protected void setUp() throws Exception {
		// JUnitDoclet begin method testcase.setUp
		super.setUp();
		graphicalcompositefigure = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		graphicalcompositefigure = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method clone()
	/**
	 * Method testClone is testing clone
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#clone()
	 */
	// JUnitDoclet end javadoc_method clone()
	public void testClone() throws Exception {
		// JUnitDoclet begin method clone
		// JUnitDoclet end method clone
	}

	// JUnitDoclet begin javadoc_method displayBox()
	/**
	 * Method testDisplayBox is testing displayBox
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#displayBox()
	 */
	// JUnitDoclet end javadoc_method displayBox()
	public void testDisplayBox() throws Exception {
		// JUnitDoclet begin method displayBox
		// JUnitDoclet end method displayBox
	}

	// JUnitDoclet begin javadoc_method basicDisplayBox()
	/**
	 * Method testBasicDisplayBox is testing basicDisplayBox
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#basicDisplayBox(java.awt.Point, java.awt.Point)
	 */
	// JUnitDoclet end javadoc_method basicDisplayBox()
	public void testBasicDisplayBox() throws Exception {
		// JUnitDoclet begin method basicDisplayBox
		// JUnitDoclet end method basicDisplayBox
	}

	// JUnitDoclet begin javadoc_method update()
	/**
	 * Method testUpdate is testing update
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#update()
	 */
	// JUnitDoclet end javadoc_method update()
	public void testUpdate() throws Exception {
		// JUnitDoclet begin method update
		// JUnitDoclet end method update
	}

	// JUnitDoclet begin javadoc_method draw()
	/**
	 * Method testDraw is testing draw
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#draw(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method draw()
	public void testDraw() throws Exception {
		// JUnitDoclet begin method draw
		// JUnitDoclet end method draw
	}

	// JUnitDoclet begin javadoc_method handles()
	/**
	 * Method testHandles is testing handles
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#handles()
	 */
	// JUnitDoclet end javadoc_method handles()
	public void testHandles() throws Exception {
		// JUnitDoclet begin method handles
		// JUnitDoclet end method handles
	}

	// JUnitDoclet begin javadoc_method getAttribute()
	/**
	 * Method testGetAttribute is testing getAttribute
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#getAttribute(java.lang.String)
	 */
	// JUnitDoclet end javadoc_method getAttribute()
	public void testGetAttribute() throws Exception {
		// JUnitDoclet begin method getAttribute
		// JUnitDoclet end method getAttribute
	}

	// JUnitDoclet begin javadoc_method setAttribute()
	/**
	 * Method testSetAttribute is testing setAttribute
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#setAttribute(java.lang.String, java.lang.Object)
	 */
	// JUnitDoclet end javadoc_method setAttribute()
	public void testSetAttribute() throws Exception {
		// JUnitDoclet begin method setAttribute
		// JUnitDoclet end method setAttribute
	}

	// JUnitDoclet begin javadoc_method setPresentationFigure()
	/**
	 * Method testSetGetPresentationFigure is testing setPresentationFigure
	 * and getPresentationFigure together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#setPresentationFigure(org.jhotdraw.framework.Figure)
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#getPresentationFigure()
	 */
	// JUnitDoclet end javadoc_method setPresentationFigure()
	public void testSetGetPresentationFigure() throws Exception {
		// JUnitDoclet begin method setPresentationFigure getPresentationFigure
		org.jhotdraw.framework.Figure[] tests = { new RectangleFigure(new Point(10, 10), new Point(100, 100)), null };

		for (int i = 0; i < tests.length; i++) {
			graphicalcompositefigure.setPresentationFigure(tests[i]);
			assertEquals(tests[i], graphicalcompositefigure.getPresentationFigure());
		}
		// JUnitDoclet end method setPresentationFigure getPresentationFigure
	}

	// JUnitDoclet begin javadoc_method layout()
	/**
	 * Method testLayout is testing layout
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#layout()
	 */
	// JUnitDoclet end javadoc_method layout()
	public void testLayout() throws Exception {
		// JUnitDoclet begin method layout
		// JUnitDoclet end method layout
	}

	// JUnitDoclet begin javadoc_method setLayouter()
	/**
	 * Method testSetGetLayouter is testing setLayouter
	 * and getLayouter together by setting some value
	 * and verifying it by reading.
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#setLayouter(org.jhotdraw.contrib.Layouter)
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#getLayouter()
	 */
	// JUnitDoclet end javadoc_method setLayouter()
	public void testSetGetLayouter() throws Exception {
		// JUnitDoclet begin method setLayouter getLayouter
		org.jhotdraw.contrib.Layouter[] tests = { new SimpleLayouter(graphicalcompositefigure), null };

		for (int i = 0; i < tests.length; i++) {
			graphicalcompositefigure.setLayouter(tests[i]);
			assertEquals(tests[i], graphicalcompositefigure.getLayouter());
		}
		// JUnitDoclet end method setLayouter getLayouter
	}

	// JUnitDoclet begin javadoc_method figureRequestRemove()
	/**
	 * Method testFigureRequestRemove is testing figureRequestRemove
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#figureRequestRemove(org.jhotdraw.framework.FigureChangeEvent)
	 */
	// JUnitDoclet end javadoc_method figureRequestRemove()
	public void testFigureRequestRemove() throws Exception {
		// JUnitDoclet begin method figureRequestRemove
		// JUnitDoclet end method figureRequestRemove
	}

	// JUnitDoclet begin javadoc_method read()
	/**
	 * Method testRead is testing read
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#read(org.jhotdraw.util.StorableInput)
	 */
	// JUnitDoclet end javadoc_method read()
	public void testRead() throws Exception {
		// JUnitDoclet begin method read
		// JUnitDoclet end method read
	}

	// JUnitDoclet begin javadoc_method write()
	/**
	 * Method testWrite is testing write
	 * @see org.jhotdraw.contrib.GraphicalCompositeFigure#write(org.jhotdraw.util.StorableOutput)
	 */
	// JUnitDoclet end javadoc_method write()
	public void testWrite() throws Exception {
		// JUnitDoclet begin method write
		// JUnitDoclet end method write
	}

	// JUnitDoclet begin javadoc_method testVault
	/**
	 * JUnitDoclet moves marker to this method, if there is not match
	 * for them in the regenerated code and if the marker is not empty.
	 * This way, no test gets lost when regenerating after renaming.
	 * <b>Method testVault is supposed to be empty.</b>
	 */
	// JUnitDoclet end javadoc_method testVault
	public void testVault() throws Exception {
		// JUnitDoclet begin method testcase.testVault
		// JUnitDoclet end method testcase.testVault
	}
}
