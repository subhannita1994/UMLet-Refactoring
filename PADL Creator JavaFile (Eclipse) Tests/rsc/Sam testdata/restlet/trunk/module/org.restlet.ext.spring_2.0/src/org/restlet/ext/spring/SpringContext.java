/*
 * Copyright 2005-2006 Noelios Consulting.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet.ext.spring;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.resource.Representation;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Spring application context based on a Restlet context. 
 * @author Jerome Louvel (contact@noelios.com) <a href="http://www.noelios.com/">Noelios Consulting</a>
 */
public class SpringContext extends GenericApplicationContext
{
	/** The parent Restlet context. */
	private Context restletContext;

	/** The modifiable list of configuration URIs for bean definition via properties. */
	private List<String> propertyConfigRefs;

	/** The modifiable list of configuration URIs for XML definition via properties. */
	private List<String> xmlConfigRefs;

	/** Indicates if the context was already loaded. */
	private boolean loaded;

	/**
	 * Constructor.
	 * @param restletContext The parent Restlet context.
	 */
	public SpringContext(Context restletContext)
	{
		this.restletContext = restletContext;
		this.propertyConfigRefs = null;
		this.xmlConfigRefs = null;
		this.loaded = false;
	}

	/**
	 * Returns the parent Restlet context.
	 * @return The parent Restlet context.
	 */
	public Context getRestletContext()
	{
		return this.restletContext;
	}

	/**
	 * Returns the modifiable list of configuration URIs for XML definition via properties.
	 * @return The modifiable list of configuration URIs for XML definition via properties.
	 */
	public List<String> getPropertyConfigRefs()
	{
		if (this.propertyConfigRefs == null)
			this.propertyConfigRefs = new ArrayList<String>();
		return this.propertyConfigRefs;
	}

	/**
	 * Returns the modifiable list of configuration URIs for bean definition via properties.
	 * @return The modifiable list of configuration URIs for bean definition via properties.
	 */
	public List<String> getXmlConfigRefs()
	{
		if (this.xmlConfigRefs == null) this.xmlConfigRefs = new ArrayList<String>();
		return this.xmlConfigRefs;
	}

	@Override
	public void refresh()
	{
		// If this context hasn't been loaded yet, read all the configurations registered 
		if (!this.loaded)
		{
			Representation config = null;

			// First, read the bean definitions from properties representations
			PropertiesBeanDefinitionReader propReader = null;
			for (String ref : getPropertyConfigRefs())
			{
				config = getRestletContext().getDispatcher().get(ref).getEntity();

				if (config != null)
				{
					propReader = new PropertiesBeanDefinitionReader(this);
					propReader.loadBeanDefinitions(new SpringResource(config));
				}
			}

			// Then, read the bean definitions from XML representations
			XmlBeanDefinitionReader xmlReader = null;
			for (String ref : getXmlConfigRefs())
			{
				config = getRestletContext().getDispatcher().get(ref).getEntity();

				if (config != null)
				{
					xmlReader = new XmlBeanDefinitionReader(this);
					xmlReader.loadBeanDefinitions(new SpringResource(config));
				}
			}
		}

		// Now load or refresh
		super.refresh();
	}

}
