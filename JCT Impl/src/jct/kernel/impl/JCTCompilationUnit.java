/**
 * @author Mathieu Lemoine
 * @created 2009-01-08 (木)
 *
 * Licensed under 3-clause BSD License:
 * Copyright © 2009, Mathieu Lemoine
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of Mathieu Lemoine nor the
 *    names of contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY Mathieu Lemoine ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Mathieu Lemoine BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jct.kernel.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import jct.kernel.Constants;
import jct.kernel.IJCTClass;
import jct.kernel.IJCTComment;
import jct.kernel.IJCTCompilationUnit;
import jct.kernel.IJCTImport;
import jct.kernel.IJCTPackage;
import jct.kernel.IJCTRootNode;
import jct.kernel.IJCTSourceCodePart;
import jct.kernel.IJCTVisitor;
import jct.kernel.JCTKind;
import jct.util.collection.IndirectCollection;

/**
 * This class represents a compilation unit
 * (typically, a compilation unit is a Java source file).<br>
 * Two compilation units are equals iff they are representing the same file.
 * 
 * Default implementation for {@link jct.kernel.IJCTCompilationUnit}
 *
 * @author Mathieu Lemoine
 */
class JCTCompilationUnit extends JCTSourceCodePart<IJCTSourceCodePart>
		implements IJCTCompilationUnit {
	/**
	 * source file of this compilation unit
	 */
	private File sourceFile;

	/**
	 * Set of importeds of this compilation unit
	 */
	private final Set<IJCTImport> importeds = this.createInternalSet();

	/**
	 * Set of clazzs of this compilation unit
	 */
	private final Set<IJCTClass> clazzs = this.createInternalSet();

	/**
	 * stored source code of this compilation unit
	 */
	private StringBuffer storedSourceCode = null;

	/**
	 * List of comments of this compilation unit
	 */
	private final List<IJCTComment> comments = this.createInternalList();

	JCTCompilationUnit(final IJCTRootNode aRootNode, final File sourceFile) {
		super(aRootNode, sourceFile.getAbsolutePath());
		this.sourceFile = sourceFile;
		super.backpatchElements(new IndirectCollection<IJCTSourceCodePart>(
			this.importeds,
			this.clazzs,
			this.comments));
	}

	/**
	 * Returns whether the compilation unit is a {@literal package-info.java} file
	 */
	public boolean isPackageDeclaration() {
		return this.sourceFile.getName().equals(
			Constants.PACKAGE_DECLARATION_FILENAME);
	}

	/**
	 * Adds a "imported" in the set, if it was not already in it
	 */
	public void addImported(final IJCTImport imported) {
		this.importeds.add(imported);
	}

	/**
	 * Removes this imported from the set
	 */
	public void removeImported(final IJCTImport imported) {
		this.importeds.remove(imported);
	}

	/**
	 * Returns the set of importeds of this compilation unit
	 * <em>Part of the enclosed elements.</em>
	 */
	public Set<IJCTImport> getImporteds() {
		return Collections.unmodifiableSet(this.importeds);
	}

	/**
	 * Adds a "clazz" in the set, if it was not already in it
	 */
	public void addClazz(final IJCTClass clazz) {
		this.clazzs.add(clazz);
	}

	/**
	 * Removes this clazz from the set
	 */
	public void removeClazz(final IJCTClass clazz) {
		this.clazzs.remove(clazz);
	}

	/**
	 * Returns the set of clazzs of this compilation unit
	 * <em>Part of the enclosed elements.</em>
	 */
	public Set<IJCTClass> getClazzs() {
		return Collections.unmodifiableSet(this.clazzs);
	}

	/**
	 * Adds a "comment" at the index (or move it there)
	 */
	@Override
	public void addComment(final int anIndex, final IJCTComment aComment) {
		this.comments.add(anIndex, aComment);
	}

	/**
	 * Adds a "comment" at the end of the list (or move it there)
	 */
	@Override
	public void addComment(final IJCTComment aComment) {
		this.comments.add(aComment);
	}

	/**
	 * Removes this comment from the list
	 */
	@Override
	public void removeComment(final IJCTComment aComment) {
		this.comments.remove(aComment);
	}

	/**
	 * Remove the comment at the index
	 */
	@Override
	public void removeComment(final int anIndex) {
		this.comments.remove(anIndex);
	}

	/**
	 * Returns the list of comments of this compilation unit
	 * <em>Part of the enclosed elements.</em>
	 */
	@Override
	public List<IJCTComment> getComments() {
		return Collections.unmodifiableList(this.comments);
	}

	/**
	 * Returns the kind of this constituent (JCTKind.COMPILATION_UNIT)
	 */
	public JCTKind getKind() {
		return JCTKind.COMPILATION_UNIT;
	}

	/**
	 * Calls the appropriate visit* method on the visitor
	 */
	public <R, P> R accept(final IJCTVisitor<R, P> visitor, final P aP) {
		return visitor.visitCompilationUnit(this, aP);
	}

	@Override
	public String getSourceCode() {
		try {
			final StringWriter w =
				new StringWriter()
					.append(this.getSourceFile().getPath())
					.append(new String(Character.toChars(0x00))); // NULL control character

			return this.getSourceCode(w).toString();
		}
		catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Writer getSourceCode(final Writer aWriter) throws IOException {
		final IJCTPackage p = (IJCTPackage) this.getEnclosingElement();
		if (null != p && !p.isUnnamed())
			aWriter.append("package " + p.getName() + ";\n");

		final SortedSet<IJCTImport> is =
			new TreeSet<IJCTImport>(new Comparator<IJCTImport>() {
				public int compare(final IJCTImport o1, final IJCTImport o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
		is.addAll(this.getImporteds());

		for (final IJCTImport i : is)
			i.getSourceCode(aWriter);

		for (final IJCTClass c : this.getClazzs())
			c.getSourceCode(aWriter);

		return aWriter;
	}

	@Override
	public void setStoredSourceCodeLength(final Integer length) {
		throw new UnsupportedOperationException(
			"A compilation unit is not pre-calculated");
	}

	@Override
	public Integer getStoredSourceCodeLength() {
		if (null != this.storedSourceCode)
			super.setStoredSourceCodeLength(this.storedSourceCode.length());

		return super.getStoredSourceCodeLength();
	}

	@Override
	public void setStoredSourceCodeOffset(final Integer offset) {
		throw new UnsupportedOperationException(
			"A compilation unit is always at offset 0");
	}

	@Override
	public Integer getStoredSourceCodeOffset() {
		super.setStoredSourceCodeOffset(0);
		return null == this.getStoredSourceCode() ? null : 0;
	}

	@Override
	public Integer getStoredSourceCodeOffset(
		final IJCTSourceCodePart enclosingElement) {
		if (this != enclosingElement)
			throw new IllegalArgumentException(
				"enclosingElement must be an Enclosing Element of this");

		return this.getStoredSourceCodeOffset();
	}

	@Override
	public String getStoredSourceCode() {
		return null == this.storedSourceCode ? null : this.storedSourceCode
			.toString();
	}

	@Override
	public void setStoredSourceCode(final String storedSourceCode) {
		this.storedSourceCode = new StringBuffer(storedSourceCode);
	}

	protected void updateStoredSourceCode(
		final JCTSourceCodePart part,
		final String storedSourceCode) {
		if (null == this.storedSourceCode)
			throw new IllegalStateException(
				"Impossible to replace a part of Stored Source Code if there is no SSC.");

		final int absoluteOffset = part.getStoredSourceCodeOffset(this);

		this.storedSourceCode.replace(absoluteOffset, absoluteOffset
				+ part.getStoredSourceCodeLength(), storedSourceCode);

		final int delta =
			storedSourceCode.length() - part.getStoredSourceCodeLength();

		if (delta != 0)
			for (final IJCTSourceCodePart enclosingPart : this
				.getEnclosedElements()) {
				if (!(enclosingPart instanceof JCTSourceCodePart)
						|| null == enclosingPart.getStoredSourceCodeOffset())
					continue;
				final JCTSourceCodePart scp = (JCTSourceCodePart) enclosingPart;
				if (scp.getStoredSourceCodeOffset(this)
						+ scp.getStoredSourceCodeLength() >= absoluteOffset)
					scp.updateStoredSourceCode(part, absoluteOffset, delta);
			}
	}

	public File getSourceFile() {
		return this.sourceFile;
	}

	public void setSourceFile(final File sourceFile) {
		this.sourceFile = sourceFile;
		this.setName(sourceFile.getAbsolutePath());
	}

	private static final long serialVersionUID = -1061725052762178233L;

	@Override
	protected boolean isDesignatedBy(final String data) {
		return super.isDesignatedBy(data)
				|| data.equals(this.getSourceFile().getName());
	}
}
