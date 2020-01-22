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
package org.jhotdraw.test.standard;

// JUnitDoclet begin import
import org.jhotdraw.application.DrawApplication;
import org.jhotdraw.framework.Tool;
import org.jhotdraw.standard.SelectionTool;
import org.jhotdraw.standard.ToolButton;
import org.jhotdraw.test.JHDTestCase;
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
 * TestCase ToolButtonTest is generated by
 * JUnitDoclet to hold the tests for ToolButton.
 * @see org.jhotdraw.standard.ToolButton
 */
// JUnitDoclet end javadoc_class
public class ToolButtonTest
// JUnitDoclet begin extends_implements
extends JHDTestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private ToolButton toolbutton;
	// JUnitDoclet end class

	/**
	 * Constructor ToolButtonTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public ToolButtonTest(String name) {
		// JUnitDoclet begin method ToolButtonTest
		super(name);
		// JUnitDoclet end method ToolButtonTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public ToolButton createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		Tool tool = new SelectionTool(getDrawingEditor());
		return new ToolButton(getDrawingEditor(), DrawApplication.IMAGES + "SEL", "Selection Tool", tool);
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
		toolbutton = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		toolbutton = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method tool()
	/**
	 * Method testTool is testing tool
	 * @see org.jhotdraw.standard.ToolButton#tool()
	 */
	// JUnitDoclet end javadoc_method tool()
	public void testTool() throws Exception {
		// JUnitDoclet begin method tool
		// JUnitDoclet end method tool
	}

	// JUnitDoclet begin javadoc_method name()
	/**
	 * Method testName is testing name
	 * @see org.jhotdraw.standard.ToolButton#name()
	 */
	// JUnitDoclet end javadoc_method name()
	public void testName() throws Exception {
		// JUnitDoclet begin method name
		// JUnitDoclet end method name
	}

	// JUnitDoclet begin javadoc_method attributeValue()
	/**
	 * Method testAttributeValue is testing attributeValue
	 * @see org.jhotdraw.standard.ToolButton#attributeValue()
	 */
	// JUnitDoclet end javadoc_method attributeValue()
	public void testAttributeValue() throws Exception {
		// JUnitDoclet begin method attributeValue
		// JUnitDoclet end method attributeValue
	}

	// JUnitDoclet begin javadoc_method getMinimumSize()
	/**
	 * Method testGetMinimumSize is testing getMinimumSize
	 * @see org.jhotdraw.standard.ToolButton#getMinimumSize()
	 */
	// JUnitDoclet end javadoc_method getMinimumSize()
	public void testGetMinimumSize() throws Exception {
		// JUnitDoclet begin method getMinimumSize
		// JUnitDoclet end method getMinimumSize
	}

	// JUnitDoclet begin javadoc_method getPreferredSize()
	/**
	 * Method testGetPreferredSize is testing getPreferredSize
	 * @see org.jhotdraw.standard.ToolButton#getPreferredSize()
	 */
	// JUnitDoclet end javadoc_method getPreferredSize()
	public void testGetPreferredSize() throws Exception {
		// JUnitDoclet begin method getPreferredSize
		// JUnitDoclet end method getPreferredSize
	}

	// JUnitDoclet begin javadoc_method getMaximumSize()
	/**
	 * Method testGetMaximumSize is testing getMaximumSize
	 * @see org.jhotdraw.standard.ToolButton#getMaximumSize()
	 */
	// JUnitDoclet end javadoc_method getMaximumSize()
	public void testGetMaximumSize() throws Exception {
		// JUnitDoclet begin method getMaximumSize
		// JUnitDoclet end method getMaximumSize
	}

	// JUnitDoclet begin javadoc_method paintSelected()
	/**
	 * Method testPaintSelected is testing paintSelected
	 * @see org.jhotdraw.standard.ToolButton#paintSelected(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method paintSelected()
	public void testPaintSelected() throws Exception {
		// JUnitDoclet begin method paintSelected
		// JUnitDoclet end method paintSelected
	}

	// JUnitDoclet begin javadoc_method paint()
	/**
	 * Method testPaint is testing paint
	 * @see org.jhotdraw.standard.ToolButton#paint(java.awt.Graphics)
	 */
	// JUnitDoclet end javadoc_method paint()
	public void testPaint() throws Exception {
		// JUnitDoclet begin method paint
		// JUnitDoclet end method paint
	}

	// JUnitDoclet begin javadoc_method toolUsable()
	/**
	 * Method testToolUsable is testing toolUsable
	 * @see org.jhotdraw.standard.ToolButton#toolUsable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolUsable()
	public void testToolUsable() throws Exception {
		// JUnitDoclet begin method toolUsable
		// JUnitDoclet end method toolUsable
	}

	// JUnitDoclet begin javadoc_method toolUnusable()
	/**
	 * Method testToolUnusable is testing toolUnusable
	 * @see org.jhotdraw.standard.ToolButton#toolUnusable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolUnusable()
	public void testToolUnusable() throws Exception {
		// JUnitDoclet begin method toolUnusable
		// JUnitDoclet end method toolUnusable
	}

	// JUnitDoclet begin javadoc_method toolActivated()
	/**
	 * Method testToolActivated is testing toolActivated
	 * @see org.jhotdraw.standard.ToolButton#toolActivated(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolActivated()
	public void testToolActivated() throws Exception {
		// JUnitDoclet begin method toolActivated
		// JUnitDoclet end method toolActivated
	}

	// JUnitDoclet begin javadoc_method toolDeactivated()
	/**
	 * Method testToolDeactivated is testing toolDeactivated
	 * @see org.jhotdraw.standard.ToolButton#toolDeactivated(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolDeactivated()
	public void testToolDeactivated() throws Exception {
		// JUnitDoclet begin method toolDeactivated
		// JUnitDoclet end method toolDeactivated
	}

	// JUnitDoclet begin javadoc_method toolEnabled()
	/**
	 * Method testToolEnabled is testing toolEnabled
	 * @see org.jhotdraw.standard.ToolButton#toolEnabled(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolEnabled()
	public void testToolEnabled() throws Exception {
		// JUnitDoclet begin method toolEnabled
		// JUnitDoclet end method toolEnabled
	}

	// JUnitDoclet begin javadoc_method toolDisabled()
	/**
	 * Method testToolDisabled is testing toolDisabled
	 * @see org.jhotdraw.standard.ToolButton#toolDisabled(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method toolDisabled()
	public void testToolDisabled() throws Exception {
		// JUnitDoclet begin method toolDisabled
		// JUnitDoclet end method toolDisabled
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
