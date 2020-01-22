/*****************************************************************************
 * Copyright (C) 2003 Jean-Daniel Fekete and INRIA, France                   *
 * ------------------------------------------------------------------------- *
 * This software is published under the terms of the QPL Software License    *
 * a copy of which has been included with this distribution in the           *
 * license-infovis.txt file.                                                 *
 *****************************************************************************/
package infovis.graph.io;

import infovis.Graph;
import infovis.column.IntColumn;
import infovis.column.LongColumn;
import infovis.column.ObjectColumn;
import infovis.column.StringColumn;
import infovis.graph.DefaultGraph;
import infovis.io.AbstractTableReader;
import infovis.io.WebLogParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * The HTMLGraphReader is creates a graph from html web sites.
 *
 * @author Jean-Daniel Fekete
 * @version $Revision: 1.1 $
 */
public class HTMLGraphReader extends AbstractTableReader {
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LENGTH = "length";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_MIME_TYPE = "mime";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_REF = "ref";
	protected Graph graph;
	protected LinkedList queue;
	protected HashMap loaded;
	protected URL context;
	protected URL base;
	protected int currentNode;
	protected StringColumn nameColumn;
	protected IntColumn lengthColumn;
	protected LongColumn dateColumn;
	protected StringColumn mimeColumn;
	protected StringColumn titleColumn;
	protected IntColumn refColumn;
	protected StringBuffer characters;
	protected PrintStream log;
	protected URL realRoot;

	class Callback extends HTMLEditorKit.ParserCallback {
		/**
		 * @see javax.swing.text.html.HTMLEditorKit.ParserCallback#handleStartTag(Tag, MutableAttributeSet, int)
		 */
		public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
			if (t == Tag.A) {
				add((String) a.getAttribute(HTML.Attribute.HREF));
			}
			else if (t == Tag.IMG) {
				add((String) a.getAttribute(HTML.Attribute.SRC));
				add((String) a.getAttribute(HTML.Attribute.USEMAP));
			}
			else if (t == Tag.SCRIPT) {
				add((String) a.getAttribute(HTML.Attribute.SRC));
			}
			else if (t == Tag.BODY) {
				add((String) a.getAttribute(HTML.Attribute.BACKGROUND));
			}
			else if (t == Tag.LINK) {
				add((String) a.getAttribute(HTML.Attribute.HREF));
			}
			else if (t == Tag.AREA) {
				add((String) a.getAttribute(HTML.Attribute.HREF));
			}
			else if (t == Tag.OBJECT) {
				add((String) a.getAttribute(HTML.Attribute.CODEBASE));
				add((String) a.getAttribute(HTML.Attribute.CLASSID));
				add((String) a.getAttribute(HTML.Attribute.DATA));
				add((String) a.getAttribute(HTML.Attribute.ARCHIVE));
				add((String) a.getAttribute(HTML.Attribute.USEMAP));
			}
			else if (t == Tag.APPLET) {
				add((String) a.getAttribute(HTML.Attribute.CODEBASE));
			}
			else if (t == Tag.FORM) {
				add((String) a.getAttribute(HTML.Attribute.ACTION));
			}
			else if (t == Tag.INPUT) {
				add((String) a.getAttribute(HTML.Attribute.SRC));
				add((String) a.getAttribute(HTML.Attribute.USEMAP));
			}
			else if (t == Tag.FRAME) {
				add((String) a.getAttribute(HTML.Attribute.SRC));
			}
			else if (t == Tag.BASE) {
				add((String) a.getAttribute(HTML.Attribute.HREF));
			}
			else if (t == Tag.TITLE) {
				HTMLGraphReader.this.characters = new StringBuffer();
			}
		}

		public void handleEndTag(Tag t, int pos) {
			if (t == Tag.TITLE) {
				HTMLGraphReader.this.titleColumn.setExtend(HTMLGraphReader.this.currentNode, HTMLGraphReader.this.characters.toString());
				HTMLGraphReader.this.characters = null;
			}
		}

		public void handleText(char[] text, int pos) {
			if (HTMLGraphReader.this.characters != null) {
				HTMLGraphReader.this.characters.append(text);
			}
		}

		public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
			handleStartTag(t, a, pos);
		}
	}

	/**
	 * Constructor for HTMLGraphReader.
	 * @param name the URL to load.
	 * @param graph the Graph to read.
	 */
	public HTMLGraphReader(String name, Graph graph) {
		super(null, name, graph.getEdgeTable());
		this.graph = graph;
		try {
			this.base = new URL(name);
		}
		catch (MalformedURLException e) {
			this.base = null;
		}
		this.context = this.base;
		this.queue = new LinkedList();
		this.loaded = new HashMap();
		this.currentNode = Graph.NIL;
		this.nameColumn =
			StringColumn.findColumn(graph.getVertexTable(), COLUMN_NAME);
		this.lengthColumn =
			IntColumn.findColumn(graph.getVertexTable(), COLUMN_LENGTH);
		this.dateColumn = LongColumn.findColumn(graph.getVertexTable(), COLUMN_DATE);
		this.mimeColumn =
			StringColumn.findColumn(graph.getVertexTable(), COLUMN_MIME_TYPE);
		this.titleColumn =
			StringColumn.findColumn(graph.getVertexTable(), COLUMN_TITLE);
		this.refColumn = IntColumn.findColumn(graph.getEdgeTable(), COLUMN_REF);
	}

	/**
	 * Test whether a specified URL should be considered for loading.
	 *
	 * @param url the url.
	 *
	 * @return <code>true</code> if a specified URL should be
	 * considered for loading.
	 */
	public boolean considerURL(URL url) {
		return url.getHost().equalsIgnoreCase(this.base.getHost())
				&& url.getPath().startsWith(this.base.getPath());
	}

	/**
	 * Adds a specified url to the queue of URL to load.
	 *
	 * @param url the url.
	 *
	 * @return <code>true</code> if the URL has been queued.
	 */
	public boolean add(String url) {
		if (url == null)
			return false;
		try {
			url = URLDecoder.decode(url, null);
			int query = Math.max(url.indexOf("?"), url.indexOf("#"));
			String strippedUrl;
			if (query == -1)
				strippedUrl = url;
			else
				strippedUrl = url.substring(0, query);

			return add(new URL(this.context, strippedUrl));
		}
		catch (UnsupportedEncodingException e) {
			return false;
		}
		catch (MalformedURLException e) {
			return false;
		}
	}

	/**
	 * Normalize a specified URL.
	 *
	 * @param url the URL.
	 *
	 * @return the normalized URL.
	 */
	public URL normalize(URL url) {
		int i = 0;
		String f = url.getFile();
		if ((i = f.indexOf("//", i)) != -1) {
			while ((i = f.indexOf("//", i)) != -1) {
				f = f.substring(0, i + 1) + f.substring(i + 2);
			}
			try {
				url = new URL(url, f);
			}
			catch (MalformedURLException e) {
			}
		}
		return url;
	}

	/**
	 * Adds a specified url to the queue of URL to load.
	 *
	 * @param url the url.
	 *
	 * @return <code>true</code> if the URL has been queued.
	 */
	public boolean add(URL url) {
		if (url == null)
			return false;
		url = normalize(url);
		if (!considerURL(url))
			return false;

		String urlString = url.toString();
		Integer i = (Integer) this.loaded.get(urlString);
		if (i == null) {
			int node = this.graph.addVertex();
			this.nameColumn.setExtend(node, urlString);
			this.lengthColumn.setExtend(node, 0);
			this.loaded.put(urlString, new Integer(node));
			if (this.log != null) {
				this.log.println("adding '" + urlString + "' " + this.queue.size() + " "
						+ this.loaded.size());
			}
			this.queue.addLast(urlString);
			if (this.currentNode != Graph.NIL) {
				int edge = this.graph.addEdge(this.currentNode, node);
				this.refColumn.setExtend(edge, 1);
			}
			return true;
		}
		if (this.currentNode != Graph.NIL) {
			int edge = this.graph.getEdge(this.currentNode, i.intValue());
			if (edge == Graph.NIL) {
				edge = this.graph.addEdge(this.currentNode, i.intValue());
				this.refColumn.setExtend(edge, 1);
			}
			else {
				this.refColumn.set(edge, this.refColumn.get(edge) + 1);
			}
		}
		return false;
	}

	URL tryDirectory(File dir, String index) throws MalformedURLException {
		File file = new File(dir, index);
		if (file.exists()) {
			return new URL("file", "", file.getPath());
		}
		return null;
	}

	URLConnection openConnection(URL url) throws IOException {
		if (this.realRoot == null) {
			return this.context.openConnection();
		}
		if (!url.getFile().startsWith(this.base.getFile())) {
			return null;
		}

		String newFile = url.getFile().substring(this.base.getFile().length());
		URL newUrl = new URL(this.realRoot, newFile);
		if (newUrl.getProtocol().equalsIgnoreCase("file")) {
			File file = new File(newUrl.getPath());
			if (file.isDirectory()) {
				String[] indexes = { "index.html", "index.htm", "index.shtml" };
				newUrl = null;
				for (int i = 0; i < indexes.length; i++) {
					newUrl = tryDirectory(file, indexes[i]);
					if (newUrl != null)
						break;
				}
				if (newUrl == null)
					return null;
			}
		}
		return newUrl.openConnection();
	}

	/**
	 * @see infovis.io.AbstractTableReader#load()
	 */
	public boolean load() {
		ParserDelegator parser = new ParserDelegator();
		Callback callback = new Callback();

		while (!this.queue.isEmpty()) {
			try {
				this.context = new URL((String) this.queue.getFirst());
				this.queue.removeFirst();
				this.currentNode = ((Integer) this.loaded.get(this.context)).intValue();
				URLConnection conn = openConnection(this.context);
				conn.setUseCaches(false);
				//    if (conn == null)
				//        continue;

				int size = conn.getContentLength();
				if (size >= 0) {
					this.lengthColumn.setExtend(this.currentNode, size);
				}

				long date = conn.getDate();
				if (date > 0) {
					this.dateColumn.setExtend(this.currentNode, date);
				}

				String contentType = conn.getContentType();
				if (contentType == null) {
					contentType =
						URLConnection.guessContentTypeFromName(this.context
							.getFile());
				}
				if (contentType != null) {
					this.mimeColumn.setExtend(this.currentNode, contentType);
				}

				if (contentType != null
						&& contentType.equalsIgnoreCase("TEXT/HTML")) {
					//					System.out.println("processing "+context);
					//					System.out.println("processed: "+currentNode);
					//					System.out.println("remaining: "+queue.sizeColumn());
					InputStream in = conn.getInputStream();
					BufferedReader reader =
						new BufferedReader(new InputStreamReader(in));
					setIn(reader);
					parser.parse(reader, callback, true);
					reader.close();
				}
			}
			catch (Exception e) {
				//e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Creates a column of URLs.
	 *
	 * @param obj an ObjectColumn which will contain the urls.
	 * If <code>null</code>, a new one will be created with "URL" as name.
	 */
	public ObjectColumn createUrlColumn(ObjectColumn obj) {
		if (obj == null) {
			obj = new ObjectColumn("URL");
		}

		obj.ensureCapacity(this.graph.getVerticesCount());

		for (Iterator i = this.loaded.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			URL url = (URL) entry.getKey();
			Integer val = (Integer) entry.getValue();
			obj.set(val.intValue(), url);
		}
		return obj;
	}

	/**
	 * Returns the URL map.
	 *
	 * @return the URL map.
	 */
	public Map getUrlMap() {
		return this.loaded;
	}

	/**
	 * Returns the log.
	 * @return PrintStream
	 */
	public PrintStream getLog() {
		return this.log;
	}

	/**
	 * Sets the log.
	 * @param log The log to set
	 */
	public void setLog(PrintStream log) {
		this.log = log;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param in DOCUMENT ME!
	 * @param path DOCUMENT ME!
	 */
	public void annotateWithLog(BufferedReader in, String path) {
		WebLogParser logParser = new WebLogParser();
		WebLogParser.Entry entry = new WebLogParser.Entry();
		IntColumn hitCount =
			IntColumn.findColumn(this.graph.getVertexTable(), "hits");

		try {
			while ((entry = logParser.readEntry(in, entry)) != null) {
				URL url = new URL(this.base, entry.httpArg);

				Integer i = (Integer) this.loaded.get(url);
				if (i != null) {
					int vertex = i.intValue();
					if (hitCount.isValueUndefined(vertex)) {
						hitCount.setExtend(vertex, 0);
					}
					else {
						hitCount.set(vertex, hitCount.get(vertex) + 1);
					}
				}
			}
		}
		catch (IOException e) {
		}
	}

	/**
	 * Returns the realRoot.
	 * @return URL
	 */
	public URL getRealRoot() {
		return this.realRoot;
	}

	/**
	 * Sets the realRoot.
	 * @param realRoot The realRoot to set
	 */
	public void setRealRoot(URL realRoot) {
		this.realRoot = realRoot;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		Graph graph = new DefaultGraph();
		if (args.length < 2) {
			System.err.println("syntax: <base url> <start-path> [real-url]");
		}
		FileOutputStream out;

		try {
			out = new FileOutputStream("HTMLGraphReader.log");
		}
		catch (FileNotFoundException e) {
			out = null;
		}

		HTMLGraphReader reader = new HTMLGraphReader(args[0], graph);
		reader.setLog(new PrintStream(out));
		if (args.length > 2) {
			try {
				reader.setRealRoot(new URL(args[2]));
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		reader.add(args[1]);
		reader.load();

		GraphMLWriter writer =
			new GraphMLWriter(new OutputStreamWriter(System.out), graph);
		writer.setInterlaced(true);
		writer.write();
	}
}
