/**
 * @author Mathieu Lemoine
 * @created 2009-05-06 (水)
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

package jct.nonjunittest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import jct.kernel.IJCTAssert;
import jct.kernel.IJCTAssignment;
import jct.kernel.IJCTBinaryOperator;
import jct.kernel.IJCTBlock;
import jct.kernel.IJCTBreak;
import jct.kernel.IJCTCase;
import jct.kernel.IJCTCast;
import jct.kernel.IJCTCatch;
import jct.kernel.IJCTClass;
import jct.kernel.IJCTClassMember;
import jct.kernel.IJCTClassType;
import jct.kernel.IJCTCompilationUnit;
import jct.kernel.IJCTContinue;
import jct.kernel.IJCTDoWhile;
import jct.kernel.IJCTElement;
import jct.kernel.IJCTErroneousExpression;
import jct.kernel.IJCTExpression;
import jct.kernel.IJCTExpressionStatement;
import jct.kernel.IJCTFactory;
import jct.kernel.IJCTFor;
import jct.kernel.IJCTIdentifiable;
import jct.kernel.IJCTIf;
import jct.kernel.IJCTImport;
import jct.kernel.IJCTLabel;
import jct.kernel.IJCTLiteral;
import jct.kernel.IJCTMethod;
import jct.kernel.IJCTMethodInvocation;
import jct.kernel.IJCTNewArray;
import jct.kernel.IJCTNewClass;
import jct.kernel.IJCTPackage;
import jct.kernel.IJCTParameter;
import jct.kernel.IJCTParenthesis;
import jct.kernel.IJCTReturn;
import jct.kernel.IJCTRootNode;
import jct.kernel.IJCTSelector;
import jct.kernel.IJCTSourceCodePart;
import jct.kernel.IJCTStatement;
import jct.kernel.IJCTSwitch;
import jct.kernel.IJCTSynchronized;
import jct.kernel.IJCTTry;
import jct.kernel.IJCTType;
import jct.kernel.IJCTUnaryOperator;
import jct.kernel.IJCTVariable;
import jct.kernel.IJCTWhile;
import jct.kernel.JCTKind;
import jct.kernel.JCTModifiers;
import jct.kernel.JCTPrimitiveTypes;
import jct.kernel.impl.JCTFactory;
import jct.util.misc.Counter;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.AssertTree;
import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.BreakTree;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.CatchTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.DoWhileLoopTree;
import com.sun.source.tree.EmptyStatementTree;
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.ExpressionStatementTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.IfTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.SynchronizedTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;
import com.sun.source.tree.TypeCastTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.tree.WhileLoopTree;
import com.sun.source.tree.WildcardTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.SourcePositions;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Pair;

interface Setter<T> {
	public void set(T value);
}

/**
 * This class can be use to create a JavaC AST (JCT) from a bunch of java source files
 *
 * @author Mathieu Lemoine
 */
public class JCTDistordedCreatorFromSourceCode implements
		TreeVisitor<IJCTElement, Object>,
		ElementVisitor<IJCTIdentifiable, Object>, TypeVisitor<IJCTType, Object> {
	public static enum VisitingLevel {
		STATEMENTS, BOUNDING;
	}

	public static IJCTRootNode createJCT(
		final String aName,
		final boolean extractActualSourceCode,
		final EnumSet<VisitingLevel> visitingLevel,
		final DiagnosticListener<? super JavaFileObject> diag,
		final Iterable<String> options,
		final Counter counter,
		final File... files) throws IOException {
		final JavacTool comp = JavacTool.create();
		final StandardJavaFileManager fm =
			comp.getStandardFileManager(null, null, null);
		final Iterable<? extends JavaFileObject> obj =
			fm.getJavaFileObjects(files);
		final JavacTask task = comp.getTask(null, fm, diag, options, null, obj);
		final JavacElements elements = (JavacElements) task.getElements();
		final JavacTrees trees = JavacTrees.instance(task);

		final Set<PackageElement> packages = new HashSet<PackageElement>();
		for (final Element s : task.analyze())
			packages.add(elements.getPackageOf(s));

		final IJCTRootNode JCTRootNode = JCTFactory.createJCT(aName);
		final IJCTFactory JCTFactory = JCTRootNode.getFactory();
		final Map<CompilationUnitTree, IJCTPackage> cup =
			new HashMap<CompilationUnitTree, IJCTPackage>();

		counter.reset();

		for (final PackageElement p : packages) {
			counter.inc();
			final IJCTPackage aJCTPackage =
				JCTFactory.createPackage(p.isUnnamed() ? null : p
					.getQualifiedName()
					.toString(), false);
			JCTRootNode.addPackage(aJCTPackage);

			final Pair<JCTree, JCTree.JCCompilationUnit> pt =
				elements.getTreeAndTopLevel(p, null, null);
			if (null != pt) {
				counter.inc();
				aJCTPackage.setPackageDeclaration(JCTFactory
					.createCompilationUnit(new File(pt.snd
						.getSourceFile()
						.toUri())));
			}

			final Set<CompilationUnitTree> cus =
				new HashSet<CompilationUnitTree>();
			for (final Element e : p.getEnclosedElements()) {
				final Pair<JCTree, JCTree.JCCompilationUnit> et =
					elements.getTreeAndTopLevel(e, null, null);
				if (null != et && null != et.snd)
					cus.add(et.snd);
			}

			for (final CompilationUnitTree cu : cus)
				if (null != cu)
					cup.put(cu, aJCTPackage);
		}

		final JCTDistordedCreatorFromSourceCode jctCreator =
			new JCTDistordedCreatorFromSourceCode(
				JCTRootNode,
				elements,
				trees,
				cup.keySet(),
				counter,
				extractActualSourceCode,
				visitingLevel);

		for (final Map.Entry<CompilationUnitTree, IJCTPackage> e : cup
			.entrySet())
			e.getValue().addCompilationUnit(
				(IJCTCompilationUnit) e.getKey().accept(jctCreator, null));

		// Add Ghost Package Declaration Compilation Unit
		{
			final Pattern package_line =
				Pattern.compile("^\\s*package\\s+([^\\s;]+)\\s*;\\s*$");
			final String comment_start = "/*";
			final String comment_end = "*/";

			for (final File file : files)
				if ("package-info.java".equals(file.getName()))
					try

					{
						final BufferedReader reader =
							new BufferedReader(new FileReader(file));
						String line;
						boolean in_code = true;
						file_reading: while (null != (line = reader.readLine()))
							if (in_code) {
								final Matcher m = package_line.matcher(line);
								if (m.matches()) {
									for (final IJCTPackage p : JCTRootNode
										.getPackages())
										if (!p.isUnnamed()
												&& m.group(1).equals(
													p.getName())) {
											if (null == p
												.getPackageDeclaration()) {
												counter.inc();
												p
													.setPackageDeclaration(JCTFactory
														.createCompilationUnit(file));
											}
											break file_reading;
										}
									counter.inc();
									final IJCTPackage p =
										JCTFactory.createPackage(
											m.group(1),
											true);
									JCTRootNode.addPackage(p);
									counter.inc();
									p.setPackageDeclaration(JCTFactory
										.createCompilationUnit(file));
									break file_reading;
								}

								if (line.contains(comment_start))
									in_code = false;
							}
							else if (line.contains(comment_end))
								in_code = true;
					}
					catch (final IOException e) {
					}
		}

		// Add Ghost Classes and Packages
		while (jctCreator.classes.entrySet().size() > 0) {

			final Iterator<Map.Entry<String, IJCTClass>> it =
				jctCreator.classes.entrySet().iterator();
			final Map.Entry<String, IJCTClass> e = it.next();
			it.remove();

			if (null != e.getValue().getEnclosingElement())
				continue;

			e.getValue().setIsGhost(true);

			int index = e.getKey().lastIndexOf('$');
			if (-1 != index) // Inner Class
			{
				final String enclosingClassBName =
					e.getKey().substring(0, index);
				final IJCTClassType enclosingClassType =
					JCTRootNode.getType(
						"L" + enclosingClassBName,
						IJCTClassType.class);

				IJCTClass enclosingClass =
					enclosingClassType == null ? null : enclosingClassType
						.getSelector()
						.getElement();

				if (null == enclosingClass)
					enclosingClass =
						jctCreator.classes.get(enclosingClassBName);

				if (null == enclosingClass) {
					index = enclosingClassBName.lastIndexOf('$');

					if (-1 == index)
						index = enclosingClassBName.lastIndexOf('.');

					++index;

					final String name = enclosingClassBName.substring(index);
					enclosingClass = JCTFactory.createClass(name, false, false);
					jctCreator.classes.put(enclosingClassBName, enclosingClass);
					jctCreator.classeNames.put(
						enclosingClass,
						enclosingClassBName);
				}

				enclosingClass.addDeclaredMember(e.getValue());
			}
			else // Top Level Class
			{
				index = e.getKey().lastIndexOf('.');
				IJCTPackage p = null;
				if (-1 == index) {
					p = JCTFactory.createPackage(null, false);
					JCTRootNode.addPackage(p);
				}
				else {
					final String packageName = e.getKey().substring(0, index);
					for (final IJCTPackage ip : JCTRootNode.getPackages())
						if (packageName.equals(ip.getName())) {
							p = ip;
							break;
						}

					if (null == p) {
						p = JCTFactory.createPackage(packageName, true);
						JCTRootNode.addPackage(p);
					}
				}

				final IJCTCompilationUnit cu =
					JCTFactory.createCompilationUnit(new File(e
						.getKey()
						.replace('.', File.separatorChar) + ".class"));
				p.addCompilationUnit(cu);

				cu.addClazz(e.getValue());
			}
		}

		return JCTRootNode;
	}

	private final EnumSet<VisitingLevel> visitingLevel;
	private final boolean extractActualSourceCode;
	private final Counter counter;

	private final IJCTRootNode rootNode;
	private final IJCTFactory factory;

	private final JavacElements javacElements;
	private final SourcePositions positions;

	private final Map<String, IJCTLabel> labels =
		new HashMap<String, IJCTLabel>();
	private final Map<String, IJCTClass> classes =
		new HashMap<String, IJCTClass>();
	private final Map<IJCTClass, String> classeNames =
		new HashMap<IJCTClass, String>();

	private final Map<Element, IJCTIdentifiable> identifiables =
		new HashMap<Element, IJCTIdentifiable>();

	private final Set<CompilationUnitTree> cus;

	protected JCTDistordedCreatorFromSourceCode(
		final IJCTRootNode root,
		final JavacElements elements,
		final JavacTrees trees,
		final Set<CompilationUnitTree> cus,
		final Counter counter,
		final boolean extractActualSourceCode,
		final EnumSet<VisitingLevel> visitingLevel) {
		this.rootNode = root;
		this.factory = root.getFactory();
		this.javacElements = elements;
		this.positions = trees.getSourcePositions();
		this.cus = cus;
		this.counter = counter;
		this.extractActualSourceCode = extractActualSourceCode;
		this.visitingLevel = visitingLevel;
	}

	public IJCTType visit(final TypeMirror t) {
		return this.visit(t, null);
	}

	public IJCTType visit(final TypeMirror t, final Object p) {
		throw new IllegalArgumentException("Unknown type " + t);
	}

	public IJCTType visitArray(final ArrayType t, final Object p) {
		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.rootNode.registerArrayType(null, "arrayType");

		TypeMirror aTypeMirror = t.getComponentType();

		String name;

		while (TypeKind.TYPEVAR == aTypeMirror.getKind())
			aTypeMirror = ((Type.TypeVar) aTypeMirror).getUpperBound();

		final IJCTType aJCTType = aTypeMirror.accept(this, p);

		if (JCTKind.CLASS_TYPE == aJCTType.getKind())
			name =
				"L"
						+ ((Symbol.ClassSymbol) ((DeclaredType) aTypeMirror)
							.asElement()).flatname.toString();
		else
			name = aJCTType.getTypeName();

		return this.rootNode.registerArrayType(aJCTType, name);
	}

	public IJCTType visitDeclared(final DeclaredType t, final Object p) {
		this.counter.inc();
		return !this.visitingLevel.contains(VisitingLevel.BOUNDING) ? null
				: ((IJCTClass) t.asElement().accept(this, p)).createClassType();
	}

	public IJCTType visitError(final ErrorType t, final Object p) {
		return this.visit(t, p);
	}

	public IJCTType visitExecutable(final ExecutableType t, final Object p) {
		return this.visit(t, p);
	}

	public IJCTType visitNoType(final NoType t, final Object p) {
		this.counter.inc();
		return this.rootNode.getType(JCTPrimitiveTypes.VOID);
	}

	public IJCTType visitNull(final NullType t, final Object p) {
		return this.visit(t, p);
	}

	public IJCTType visitPrimitive(final PrimitiveType t, final Object p) {
		IJCTType aType;

		switch (t.getKind()) {
			case INT :
				aType = this.rootNode.getType(JCTPrimitiveTypes.INTEGER);
				break;
			case CHAR :
				aType = this.rootNode.getType(JCTPrimitiveTypes.CHARACTER);
				break;
			default :
				aType =
					this.rootNode.getType(JCTPrimitiveTypes.valueOf(t
						.getKind()
						.toString()));
				break;
		}

		this.counter.inc();
		return aType;
	}

	public IJCTType visitTypeVariable(final TypeVariable t, final Object p) {
		this.counter.inc();
		return !this.visitingLevel.contains(VisitingLevel.BOUNDING) ? null
				: ((IJCTClass) t.asElement().accept(this, p)).createClassType();
	}

	public IJCTType visitUnknown(final TypeMirror t, final Object p) {
		return this.visit(t, p);
	}

	public IJCTType visitWildcard(final WildcardType t, final Object p) {
		this.counter.inc();
		return !this.visitingLevel.contains(VisitingLevel.BOUNDING) ? null : t
			.getExtendsBound()
			.accept(this, p);
	}

	public IJCTIdentifiable visit(final Element e) {
		return this.visit(e, null);
	}

	public IJCTIdentifiable visit(final Element e, final Object p) {
		throw new IllegalArgumentException("Unknown element");
	}

	public IJCTIdentifiable visitPackage(
		final PackageElement e,
		final Object param) {
		final IJCTIdentifiable i = this.identifiables.get(e);
		if (null != i)
			return i;

		this.counter.inc();

		final Tree t = this.javacElements.getTree(e);
		if (null != t) {
			final IJCTIdentifiable aJCTIdentifiable =
				(IJCTIdentifiable) t.accept(this, param);
			this.identifiables.put(e, aJCTIdentifiable);
			return aJCTIdentifiable;
		}

		final IJCTPackage p =
			e.isUnnamed() ? this.factory.createPackage(null, false)
					: this.factory.createPackage(e
						.getQualifiedName()
						.toString(), false);

		this.identifiables.put(e, p);
		this.rootNode.addPackage(p);

		for (final Element aElement : e.getEnclosedElements())
			aElement.accept(this, param);

		return p;
	}

	public IJCTIdentifiable visitType(final TypeElement e, final Object p) {
		final IJCTIdentifiable i = this.identifiables.get(e);
		if (null != i)
			return i;

		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createClass("", false, false);

		final Tree t = this.javacElements.getTree(e);
		if (null != t) {
			final IJCTIdentifiable aJCTIdentifiable =
				(IJCTIdentifiable) t.accept(this, p);
			this.identifiables.put(e, aJCTIdentifiable);
			return aJCTIdentifiable;
		}

		final IJCTClass c =
			this.factory.createClass(
				e.getSimpleName().toString(),
				ElementKind.INTERFACE == e.getKind()
						|| ElementKind.ANNOTATION_TYPE == e.getKind(),
				false);

		this.classes.put(this.javacElements.getBinaryName(e).toString(), c);
		this.classeNames.put(c, this.javacElements.getBinaryName(e).toString());

		this.identifiables.put(e, c);

		while (c.getModifiers().size() > 0) {
			c.removeModifier(c.getModifiers().iterator().next());
		}

		for (final Modifier m : e.getModifiers())
			c.addModifier(JCTModifiers.valueOf(m.toString().toUpperCase()));

		if (null != e.getSuperclass()
				&& TypeKind.NONE != e.getSuperclass().getKind()) {
			final IJCTClassType aJCTClassType =
				(IJCTClassType) e.getSuperclass().accept(this, p);
			if (aJCTClassType.getSelector().getElement().getIsInterface())
				c.addDirectlyImplementedInterface(aJCTClassType);
			else
				c.setDirectSuperClass(aJCTClassType);
		}

		for (final TypeMirror aTypeMirror : e.getInterfaces())
			c.addDirectlyImplementedInterface((IJCTClassType) aTypeMirror
				.accept(this, p));

		for (final Element anElement : e.getEnclosedElements()) {
			final IJCTElement aJCTElement = anElement.accept(this, p);
			switch (aJCTElement.getKind()) {
				case CLASS :
				case METHOD :
				case VARIABLE :
					c.addDeclaredMember((IJCTClassMember) aJCTElement);
					break;
				case BLOCK :
					{
						this.counter.inc();
						final IJCTMethod init;
						if (anElement.getModifiers().contains(Modifier.STATIC)) {
							init = this.factory.createMethod("<clinit>");
							init.addModifier(JCTModifiers.STATIC);
						}
						else
							init = this.factory.createMethod(">init<");

						init.addModifier(JCTModifiers.PRIVATE);
						init.addModifier(JCTModifiers.FINAL);
						init.setBody((IJCTBlock) aJCTElement);
						c.addDeclaredMember(init);
					}
					break;
				default :
					throw new AssertionError(
						"The elements authorized in a class are only class members and blocks (initializers).");
			}
		}

		return c;
	}

	public IJCTIdentifiable visitVariable(
		final VariableElement e,
		final Object p) {
		if (null != e.getEnclosingElement())
			e.getEnclosingElement().accept(this, p);

		final IJCTIdentifiable i = this.identifiables.get(e);
		if (null != i)
			return i;

		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING)) {
			final IJCTVariable v = this.factory.createVariable("");
			v.setType(this.rootNode.getType(JCTPrimitiveTypes.VOID));
			return v;
		}

		final Tree t = this.javacElements.getTree(e);
		if (null != t) {
			final IJCTIdentifiable aJCTIdentifiable =
				(IJCTIdentifiable) t.accept(this, p);
			this.identifiables.put(e, aJCTIdentifiable);
			return aJCTIdentifiable;
		}

		final IJCTVariable v =
			this.factory.createVariable(e.getSimpleName().toString());
		v
			.setType(this.rootNode.getType(
				"Ljava.lang.Void",
				IJCTClassType.class));
		this.identifiables.put(e, v);

		while (v.getModifiers().size() > 0) {
			v.removeModifier(v.getModifiers().iterator().next());
		}

		for (final Modifier m : e.getModifiers())
			v.addModifier(JCTModifiers.valueOf(m.toString().toUpperCase()));

		final Pair<JCTree, JCTree.JCCompilationUnit> tree =
			this.javacElements.getTreeAndTopLevel(e, null, null);
		if (null != tree) {
			final int offset =
				(int) this.positions.getStartPosition(tree.snd, tree.fst);
			if (Diagnostic.NOPOS != offset) {
				v.setStoredSourceCodeOffset(offset);
				final int end =
					(int) this.positions.getEndPosition(tree.snd, tree.fst);
				if (Diagnostic.NOPOS != end)
					v.setStoredSourceCodeLength(end - offset);
			}
		}

		return v;
	}

	public IJCTIdentifiable visitExecutable(
		final ExecutableElement e,
		final Object p) {
		if (null != e.getEnclosingElement())
			e.getEnclosingElement().accept(this, p);

		final IJCTIdentifiable i = this.identifiables.get(e);
		if (null != i)
			return i;

		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createMethod("");

		final Tree t = this.javacElements.getTree(e);
		if (null != t) {
			final IJCTIdentifiable aJCTIdentifiable =
				(IJCTIdentifiable) t.accept(this, p);
			this.identifiables.put(e, aJCTIdentifiable);
			return aJCTIdentifiable;
		}

		final IJCTMethod m =
			this.factory.createMethod(e.getSimpleName().toString());
		this.identifiables.put(e, m);

		if (null != e.asType() && null != e.getReturnType())
			m.setReturnType(e.getReturnType().accept(this, p));

		while (m.getModifiers().size() > 0) {
			m.removeModifier(m.getModifiers().iterator().next());
		}

		for (final Modifier mod : e.getModifiers())
			m.addModifier(JCTModifiers.valueOf(mod.toString().toUpperCase()));

		if (null != e.asType()) {
			for (final VariableElement v : e.getParameters())
				m.addParameter((IJCTParameter) v.accept(this, p));

			for (final TypeMirror aTypeMirror : e.getThrownTypes())
				m.addThrownException((IJCTClassType) aTypeMirror
					.accept(this, p));
		}

		final Pair<JCTree, JCTree.JCCompilationUnit> tree =
			this.javacElements.getTreeAndTopLevel(e, null, null);
		if (null != tree) {
			final int offset =
				(int) this.positions.getStartPosition(tree.snd, tree.fst);
			if (Diagnostic.NOPOS != offset) {
				m.setStoredSourceCodeOffset(offset);
				final int end =
					(int) this.positions.getEndPosition(tree.snd, tree.fst);
				if (Diagnostic.NOPOS != end)
					m.setStoredSourceCodeLength(end - offset);
			}
		}
		return m;
	}

	public IJCTIdentifiable visitTypeParameter(
		final TypeParameterElement e,
		final Object p) {
		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createClass("", false, false);
		if (null != e.getEnclosingElement())
			e.getEnclosingElement().accept(this, p);

		for (final TypeMirror aTypeMirror : e.getBounds()) {
			final IJCTType t = aTypeMirror.accept(this, p);
			if (JCTKind.CLASS_TYPE == t.getKind())
				return ((IJCTClassType) t).getSelector().getElement();
		}

		return this.rootNode
			.getType("Ljava.lang.Object", IJCTClassType.class)
			.getSelector()
			.getElement();
	}

	public IJCTIdentifiable visitUnknown(final Element e, final Object p) {
		this.counter.inc();
		return null;
	}

	public IJCTElement visitOther(final Tree o, final Object p) {
		this.counter.inc();
		return null;
	}

	public IJCTElement error(final Tree o, final Object p) {
		throw new IllegalStateException("Unknown Tree Part ==> " + o.getKind()
				+ " :: " + o + " (argument :" + p + ")");
	}

	public IJCTElement visitAnnotation(final AnnotationTree node, final Object p) {
		return this.visitOther(node, p);
	}

	private IJCTSourceCodePart putSourceCodePosition(
		final IJCTSourceCodePart part,
		final Tree t) {
		if (!this.extractActualSourceCode)
			return part;

		for (final CompilationUnitTree cu : this.cus) {
			final int offset = (int) this.positions.getStartPosition(cu, t);
			if (Diagnostic.NOPOS != offset) {
				part.setStoredSourceCodeOffset(offset);
				final int end = (int) this.positions.getEndPosition(cu, t);
				if (Diagnostic.NOPOS != end)
					part.setStoredSourceCodeLength(end - offset);
				break;
			}
		}

		return part;
	}

	public IJCTElement visitArrayAccess(
		final ArrayAccessTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory.createArrayAccess(
			(IJCTExpression) node.getExpression().accept(this, p),
			(IJCTExpression) node.getIndex().accept(this, p)), node);
	}

	public IJCTElement visitArrayType(final ArrayTypeTree node, final Object p) {
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.rootNode.getType(JCTPrimitiveTypes.VOID);
		this.counter.inc();
		final IJCTElement t = node.getType().accept(this, p);

		IJCTType underlyingType = null;

		String underlyingTypeName = null;

		if (t instanceof IJCTType) {
			underlyingType = (IJCTType) t;
			if (JCTKind.CLASS_TYPE == t.getKind())
				underlyingTypeName =
					this.javacElements
						.getBinaryName(
							(Symbol.ClassSymbol) ((JCTree.JCIdent) node
								.getType()).sym).toString();
		}
		else if (t instanceof IJCTSelector<?>
				&& ((IJCTSelector<?>) t).getElement() instanceof IJCTClass) {
			Tree type = node.getType();

			if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
				type = ((ParameterizedTypeTree) type).getType();

			underlyingType =
				((IJCTClass) ((IJCTSelector<?>) t).getElement())
					.createClassType();
			underlyingTypeName =
				"L" + this.classeNames.get(((IJCTSelector<?>) t).getElement());
		}

		if (null != underlyingType)
			return this.rootNode.registerArrayType(
				underlyingType,
				underlyingTypeName);

		if (JCTKind.ERRONEOUS_SELECTOR == t.getKind()) {
			System.out.println("ArrayType with erroneous selector "
					+ t.getSourceCode());
			return this.rootNode.registerArrayType(null, t.getSourceCode());
		}

		return null;
	}

	public IJCTElement visitAssert(final AssertTree node, final Object p) {
		this.counter.inc();
		final IJCTAssert r =
			this.factory.createAssert((IJCTExpression) node
				.getCondition()
				.accept(this, p));
		if (null != node.getDetail())
			r.setDetail((IJCTExpression) node.getDetail().accept(this, p));
		return this.putSourceCodePosition(r, node);
	}

	public IJCTElement visitAssignment(final AssignmentTree node, final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory.createAssignment(
			(IJCTExpression) node.getVariable().accept(this, p),
			(IJCTExpression) node.getExpression().accept(this, p)), node);
	}

	public IJCTElement visitBinary(final BinaryTree node, final Object p) {
		this.counter.inc();
		final IJCTExpression left =
			(IJCTExpression) node.getLeftOperand().accept(this, p);
		final IJCTExpression right =
			(IJCTExpression) node.getRightOperand().accept(this, p);
		IJCTBinaryOperator result;
		switch (node.getKind()) {
			case EQUAL_TO :
				result = this.factory.createEqualTo(left, right);
				break;
			case NOT_EQUAL_TO :
				result = this.factory.createNotEqualTo(left, right);
				break;
			case CONDITIONAL_AND :
				result = this.factory.createConditionalAnd(left, right);
				break;
			case CONDITIONAL_OR :
				result = this.factory.createConditionalOr(left, right);
				break;
			case LESS_THAN :
				result = this.factory.createLessThan(left, right);
				break;
			case LESS_THAN_EQUAL :
				result = this.factory.createLessThanOrEqual(left, right);
				break;
			case GREATER_THAN :
				result = this.factory.createGreaterThan(left, right);
				break;
			case GREATER_THAN_EQUAL :
				result = this.factory.createGreaterThanOrEqual(left, right);
				break;

			case AND :
				result = this.factory.createAnd(left, right);
				break;
			case DIVIDE :
				result = this.factory.createDivide(left, right);
				break;
			case LEFT_SHIFT :
				result = this.factory.createLeftShift(left, right);
				break;
			case MINUS :
				result = this.factory.createMinus(left, right);
				break;
			case MULTIPLY :
				result = this.factory.createMultiply(left, right);
				break;
			case OR :
				result = this.factory.createOr(left, right);
				break;
			case PLUS :
				result = this.factory.createPlus(left, right);
				break;
			case REMAINDER :
				result = this.factory.createRemainder(left, right);
				break;
			case RIGHT_SHIFT :
				result = this.factory.createRightShift(left, right);
				break;
			case UNSIGNED_RIGHT_SHIFT :
				result = this.factory.createUnsignedRightShift(left, right);
				break;
			case XOR :
				result = this.factory.createXor(left, right);
				break;
			default :
				throw new IllegalStateException(
					"A Binary Operator must have a binary operator kind");
		}
		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitBlock(final BlockTree node, final Object p) {
		this.counter.inc();
		final IJCTBlock aJCTBlock = this.factory.createBlock();
		for (final StatementTree statement : node.getStatements())
			aJCTBlock.addStatement((IJCTStatement) statement.accept(this, p));
		return this.putSourceCodePosition(aJCTBlock, node);
	}

	public IJCTElement visitBreak(final BreakTree node, final Object p) {
		this.counter.inc();
		final IJCTBreak aJCTBreak = this.factory.createBreak();
		final Name name = node.getLabel();
		if (name != null)
			aJCTBreak.setLabel(this.labels.get(name.toString()));
		return this.putSourceCodePosition(aJCTBreak, node);
	}

	public IJCTElement visitCase(final CaseTree node, final Object p) {
		this.counter.inc();
		final IJCTCase aJCTCase = this.factory.createCase();
		if (null != node.getExpression())
			aJCTCase.setLabel((IJCTExpression) node.getExpression().accept(
				this,
				p));
		for (final StatementTree statement : node.getStatements())
			aJCTCase.addStatement((IJCTStatement) statement.accept(this, p));
		return this.putSourceCodePosition(aJCTCase, node);
	}

	public IJCTElement visitCatch(final CatchTree node, final Object p) {
		this.counter.inc();
		final IJCTCatch aJCTCatch =
			this.factory.createCatch((IJCTVariable) node.getParameter().accept(
				this,
				p));
		aJCTCatch.setBody((IJCTBlock) node.getBlock().accept(this, p));
		return this.putSourceCodePosition(aJCTCatch, node);
	}

	public IJCTElement visitClass(final ClassTree node, final Object p) {
		final IJCTClass c =
			(IJCTClass) this.identifiables.get(((JCTree.JCClassDecl) node).sym);

		if (c != null)
			return c;

		this.counter.inc();
		final IJCTClass aJCTClass =
			this.factory.createClass(
				node.getSimpleName().toString(),
				((JCTree.JCClassDecl) node).sym.isInterface(),
				false);

		this.classes.put(
			this.javacElements
				.getBinaryName(((JCTree.JCClassDecl) node).sym)
				.toString(),
			aJCTClass);
		this.classeNames.put(
			aJCTClass,
			this.javacElements
				.getBinaryName(((JCTree.JCClassDecl) node).sym)
				.toString());

		this.identifiables.put(((JCTree.JCClassDecl) node).sym, aJCTClass);

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING)
				&& !aJCTClass.getIsInterface()
				&& null != node.getExtendsClause())
			aJCTClass
				.setDirectSuperClass((IJCTClassType) ((JCTree.JCClassDecl) node).sym
					.getSuperclass()
					.accept(this, p));

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING))
			for (final Type implemented : ((JCTree.JCClassDecl) node).sym
				.getInterfaces())
				aJCTClass
					.addDirectlyImplementedInterface((IJCTClassType) implemented
						.accept(this, p));

		for (final Modifier m : node.getModifiers().getFlags())
			aJCTClass.addModifier(JCTModifiers.valueOf(m
				.toString()
				.toUpperCase()));

		for (final Tree member : node.getMembers()) {
			final IJCTElement aJCTElement = member.accept(this, p);
			switch (aJCTElement.getKind()) {
				case CLASS :
				case METHOD :
				case VARIABLE :
					aJCTClass.addDeclaredMember((IJCTClassMember) aJCTElement);
					break;
				case BLOCK :
					{
						this.counter.inc();
						final IJCTMethod init;
						if (((BlockTree) member).isStatic()) {
							init = this.factory.createMethod("<clinit>");
							init.addModifier(JCTModifiers.STATIC);
						}
						else
							init = this.factory.createMethod(">init<");

						init.addModifier(JCTModifiers.PRIVATE);
						init.addModifier(JCTModifiers.FINAL);
						if (this.visitingLevel
							.contains(VisitingLevel.STATEMENTS))
							init.setBody((IJCTBlock) aJCTElement);
						aJCTClass.addDeclaredMember(init);
					}
					break;
				default :
					throw new AssertionError(
						"The elements authorized in a class are only class members and blocks (initializers).");
			}
		}

		return this.putSourceCodePosition(aJCTClass, node);
	}

	public IJCTElement visitCompilationUnit(
		final CompilationUnitTree node,
		final Object p) {
		this.counter.inc();
		final IJCTCompilationUnit aJCTCompilationUnit =
			this.factory.createCompilationUnit(new File(node
				.getSourceFile()
				.toUri()));

		for (final Tree t : node.getTypeDecls())
			aJCTCompilationUnit.addClazz((IJCTClass) t.accept(this, p));

		if (this.visitingLevel.contains(VisitingLevel.STATEMENTS))
			for (final ImportTree i : node.getImports()) {
				final IJCTImport aJCTImport =
					(IJCTImport) i.accept(this, aJCTCompilationUnit);
				if (null != aJCTImport)
					aJCTCompilationUnit.addImported(aJCTImport);
			}

		try {
			final char characters[] =
				new char[(int) aJCTCompilationUnit.getSourceFile().length()];
			final BufferedReader buffer =
				new BufferedReader(new InputStreamReader(new FileInputStream(
					aJCTCompilationUnit.getSourceFile())));
			final int length = buffer.read(characters, 0, characters.length);
			buffer.close();
			aJCTCompilationUnit.setStoredSourceCode(new String(
				characters,
				0,
				length));
		}
		catch (final IOException e) {
		}

		return aJCTCompilationUnit;
	}
	public IJCTElement visitCompoundAssignment(
		final CompoundAssignmentTree node,
		final Object p) {
		this.counter.inc();
		final IJCTExpression variable =
			(IJCTExpression) node.getVariable().accept(this, p);
		final IJCTExpression value =
			(IJCTExpression) node.getExpression().accept(this, p);
		IJCTAssignment result;
		switch (node.getKind()) {
			case AND_ASSIGNMENT :
				result = this.factory.createAndAssignment(variable, value);
				break;
			case DIVIDE_ASSIGNMENT :
				result = this.factory.createDivideAssignment(variable, value);
				break;
			case LEFT_SHIFT_ASSIGNMENT :
				result =
					this.factory.createLeftShiftAssignment(variable, value);
				break;
			case MINUS_ASSIGNMENT :
				result = this.factory.createMinusAssignment(variable, value);
				break;
			case MULTIPLY_ASSIGNMENT :
				result = this.factory.createMultiplyAssignment(variable, value);
				break;
			case OR_ASSIGNMENT :
				result = this.factory.createOrAssignment(variable, value);
				break;
			case PLUS_ASSIGNMENT :
				result = this.factory.createPlusAssignment(variable, value);
				break;
			case REMAINDER_ASSIGNMENT :
				result =
					this.factory.createRemainderAssignment(variable, value);
				break;
			case RIGHT_SHIFT_ASSIGNMENT :
				result =
					this.factory.createRightShiftAssignment(variable, value);
				break;
			case UNSIGNED_RIGHT_SHIFT_ASSIGNMENT :
				result =
					this.factory.createUnsignedRightShiftAssignment(
						variable,
						value);
				break;
			case XOR_ASSIGNMENT :
				result = this.factory.createXorAssignment(variable, value);
				break;
			default :
				throw new IllegalStateException(
					"An Assignment Operator must have an assignment kind");
		}
		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitConditionalExpression(
		final ConditionalExpressionTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory
			.createConditionalOperator((IJCTExpression) node
				.getCondition()
				.accept(this, p), (IJCTExpression) node
				.getTrueExpression()
				.accept(this, p), (IJCTExpression) node
				.getFalseExpression()
				.accept(this, p)), node);
	}

	public IJCTElement visitContinue(final ContinueTree node, final Object p) {
		this.counter.inc();
		final IJCTContinue aJCTContinue = this.factory.createContinue();
		final Name name = node.getLabel();
		if (name != null)
			aJCTContinue.setLabel(this.labels.get(name.toString()));
		return this.putSourceCodePosition(aJCTContinue, node);
	}

	public IJCTElement visitDoWhileLoop(
		final DoWhileLoopTree node,
		final Object p) {
		this.counter.inc();
		final IJCTDoWhile result =
			this.factory.createDoWhile((IJCTExpression) node
				.getCondition()
				.accept(this, p));
		result.setBody((IJCTStatement) node.getStatement().accept(this, p));

		if (JCTKind.PARENTHESIS == result.getCondition().getKind()) {
			final IJCTExpression expr =
				((IJCTParenthesis) result.getCondition()).getExpression();
			((IJCTParenthesis) result.getCondition())
				.setExpression(this.factory.createBooleanLiteral(true));
			result.setCondition(expr);
		}

		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitEmptyStatement(
		final EmptyStatementTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(
			this.factory.createEmptyStatement(),
			node);
	}

	public IJCTElement visitEnhancedForLoop(
		final EnhancedForLoopTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory.createEnhancedFor(
			(IJCTVariable) node.getVariable().accept(this, p),
			(IJCTExpression) node.getExpression().accept(this, p),
			(IJCTStatement) node.getStatement().accept(this, p)), node);
	}

	public IJCTElement visitErroneous(final ErroneousTree node, final Object p) {
		this.counter.inc();
		final IJCTErroneousExpression aJCTErroneousExpression =
			this.factory.createErroneousExpression();
		for (final Tree t : node.getErrorTrees())
			aJCTErroneousExpression.addExpression((IJCTExpression) t.accept(
				this,
				p));
		return this.putSourceCodePosition(aJCTErroneousExpression, node);
	}

	public IJCTElement visitExpressionStatement(
		final ExpressionStatementTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory
			.createExpressionStatement((IJCTExpression) node
				.getExpression()
				.accept(this, p)), node);
	}

	public IJCTElement visitForLoop(final ForLoopTree node, final Object p) {
		this.counter.inc();
		final IJCTFor aJCTFor =
			this.factory.createFor(
				(IJCTExpression) node.getCondition().accept(this, p),
				(IJCTStatement) node.getStatement().accept(this, p));

		for (final StatementTree init : node.getInitializer())
			aJCTFor.addInitializer((IJCTStatement) init.accept(this, p));

		for (final ExpressionStatementTree update : node.getUpdate())
			aJCTFor
				.addUpdater((IJCTExpressionStatement) update.accept(this, p));

		return this.putSourceCodePosition(aJCTFor, node);
	}

	public IJCTElement visitIdentifier(final IdentifierTree node, final Object p) {
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createErroneousSelector("");
		this.counter.inc();
		if (null != ((JCTree.JCIdent) node).sym)
			return this
				.putSourceCodePosition(
					this.factory
						.createSimpleSelector((IJCTClassMember) ((JCTree.JCIdent) node).sym
							.accept(this, p)),
					node);
		return this.putSourceCodePosition(
			this.factory.createErroneousSelector(node.getName().toString()),
			node);
	}

	public IJCTElement visitIf(final IfTree node, final Object p) {
		this.counter.inc();
		final IJCTIf aJCTIf =
			this.factory.createIf(
				(IJCTExpression) node.getCondition().accept(this, p),
				(IJCTStatement) node.getThenStatement().accept(this, p));
		if (null != node.getElseStatement())
			aJCTIf.setElseStatement((IJCTStatement) node
				.getElseStatement()
				.accept(this, p));

		if (JCTKind.PARENTHESIS == aJCTIf.getCondition().getKind()) {
			final IJCTExpression expr =
				((IJCTParenthesis) aJCTIf.getCondition()).getExpression();
			((IJCTParenthesis) aJCTIf.getCondition())
				.setExpression(this.factory.createBooleanLiteral(true));
			aJCTIf.setCondition(expr);
		}

		return this.putSourceCodePosition(aJCTIf, node);
	}

	public IJCTElement visitImport(final ImportTree node, final Object param) {
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createImport(null, false, false);
		this.counter.inc();
		final boolean isStatic = node.isStatic();
		final String isGlobalIdentifier =
			node.getQualifiedIdentifier().toString();
		final boolean isOnDemand =
			'*' == isGlobalIdentifier.charAt(isGlobalIdentifier.length() - 1);
		final String anIdentifier =
			isOnDemand ? isGlobalIdentifier.substring(
				0,
				isGlobalIdentifier.length() - 2) : isGlobalIdentifier;

		if (!isStatic)
			if (isOnDemand) // anIdentifier is a Package FQN
			{
				final IJCTPackage p =
					(IJCTPackage) this.javacElements.getPackageElement(
						anIdentifier).accept(this, param);
				return this.putSourceCodePosition(
					this.factory.createImport(p, isStatic, isOnDemand),
					node);
			}
			else // anIdentifier is a Class FQN
			{
				final IJCTClass c =
					(IJCTClass) this.javacElements
						.getTypeElement(anIdentifier)
						.accept(this, param);
				return this.putSourceCodePosition(
					this.factory.createImport(c, isStatic, isOnDemand),
					node);
			}
		else if (isOnDemand) // anIdentifier is a Class FQN
		{
			final IJCTClass c =
				(IJCTClass) this.javacElements
					.getTypeElement(anIdentifier)
					.accept(this, param);
			return this.putSourceCodePosition(
				this.factory.createImport(c, isStatic, isOnDemand),
				node);
		}
		else // anIdentifier is a static member FQN
		{
			final String classFQN =
				anIdentifier.substring(0, anIdentifier.lastIndexOf('.'));
			final String memberName =
				anIdentifier.substring(anIdentifier.lastIndexOf('.') + 1);

			final Symbol.ClassSymbol classSym =
				this.javacElements.getTypeElement(classFQN);
			final Symbol sym =
				classSym.members().lookup(
					this.javacElements.getName(memberName)).sym;
			final IJCTClassMember cm =
				(IJCTClassMember) sym.accept(this, param);
			return this.putSourceCodePosition(
				this.factory.createImport(cm, isStatic, isOnDemand),
				node);
		}
	}

	public IJCTElement visitInstanceOf(final InstanceOfTree node, final Object p) {
		this.counter.inc();
		final IJCTExpression e =
			(IJCTExpression) node.getExpression().accept(this, p);
		IJCTType t;

		Tree type = node.getType();

		if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
			type = ((ParameterizedTypeTree) type).getType();

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING))
			switch (type.getKind()) {
				case ARRAY_TYPE :
					t = (IJCTType) type.accept(this, p);
					break;
				case MEMBER_SELECT :
					{
						this.counter.inc();
						final Symbol sym = ((JCTree.JCFieldAccess) type).sym;
						t = ((IJCTClass) sym.accept(this, p)).createClassType();
					}
					break;
				case IDENTIFIER :
					{
						this.counter.inc();
						final Symbol sym = ((JCTree.JCIdent) type).sym;
						t = ((IJCTClass) sym.accept(this, p)).createClassType();
					}
					break;
				default :
					throw new IllegalStateException("instance of \"(" + node
							+ ")\" unknown type kind ==> " + type.getKind());
			}
		else
			t = null;

		return this.putSourceCodePosition(
			this.factory.createInstanceOf(e, t),
			node);
	}

	public IJCTElement visitLabeledStatement(
		final LabeledStatementTree node,
		final Object p) {
		this.counter.inc();
		final IJCTLabel jctLabel =
			this.factory.createLabel(
				node.getLabel().toString(),
				this.factory.createEmptyStatement());
		final IJCTLabel l = this.labels.put(jctLabel.getName(), jctLabel);
		jctLabel.setStatement((IJCTStatement) node.getStatement().accept(
			this,
			p));
		this.labels.put(jctLabel.getName(), l);
		return this.putSourceCodePosition(jctLabel, node);
	}

	public IJCTElement visitLiteral(final LiteralTree node, final Object p) {
		this.counter.inc();
		final IJCTLiteral result;
		switch (node.getKind()) {
			case BOOLEAN_LITERAL :
				result =
					this.factory
						.createBooleanLiteral((Boolean) node.getValue());
				break;
			case CHAR_LITERAL :
				result =
					this.factory.createCharacterLiteral((Character) node
						.getValue());
				break;
			case DOUBLE_LITERAL :
				result =
					this.factory.createDoubleLiteral((Double) node.getValue());
				break;
			case FLOAT_LITERAL :
				result =
					this.factory.createFloatLiteral((Float) node.getValue());
				break;
			case INT_LITERAL :
				result =
					this.factory
						.createIntegerLiteral((Integer) node.getValue());
				break;
			case LONG_LITERAL :
				result = this.factory.createLongLiteral((Long) node.getValue());
				break;
			case NULL_LITERAL :
				result = this.factory.createNullLiteral();
				break;
			case STRING_LITERAL :
				result =
					this.factory.createStringLiteral((String) node.getValue());
				break;
			default :
				throw new AssertionError(
					"the type of a litteral must represent a litteral type.");
		}
		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitMemberSelect(
		final MemberSelectTree node,
		final Object p) {
		this.counter.inc();
		final IJCTExpression aJCTExpression =
			!this.visitingLevel.contains(VisitingLevel.BOUNDING) ? null
					: (IJCTExpression) node.getExpression().accept(this, p);
		final Symbol sym = ((JCTree.JCFieldAccess) node).sym;

		final IJCTClassMember m = (IJCTClassMember) sym.accept(this, p);

		return this.putSourceCodePosition(m.isStatic() == null
				|| m.isStatic() == true ? this.factory.createSimpleSelector(m)
				: this.factory.createMemberSelector(aJCTExpression, m), node);
	}

	public IJCTElement visitMethod(final MethodTree node, final Object p) {
		IJCTMethod aJCTMethod =
			(IJCTMethod) this.identifiables
				.get(((JCTree.JCMethodDecl) node).sym);

		if (aJCTMethod != null)
			return aJCTMethod;

		this.counter.inc();
		aJCTMethod = this.factory.createMethod(node.getName().toString());

		this.identifiables.put(((JCTree.JCMethodDecl) node).sym, aJCTMethod);

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING)
				&& null != node.getReturnType()) {
			IJCTType t = null;

			Tree type = node.getReturnType();

			if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
				type = ((ParameterizedTypeTree) type).getType();

			switch (type.getKind()) {
				case PRIMITIVE_TYPE :
				case ARRAY_TYPE :
					t = (IJCTType) type.accept(this, p);
					break;
				case MEMBER_SELECT :
					this.counter.inc();
					t =
						((IJCTClass) ((JCTree.JCFieldAccess) type).sym.accept(
							this,
							p)).createClassType();
					break;
				case IDENTIFIER :
					this.counter.inc();
					t =
						((IJCTClass) ((JCTree.JCIdent) type).sym
							.accept(this, p)).createClassType();
					break;
				default :
					throw new IllegalStateException("instance of \"(" + node
							+ ")\" unknown type kind ==> " + type.getKind());
			}

			aJCTMethod.setReturnType(t);
		}

		while (aJCTMethod.getModifiers().size() > 0) {
			aJCTMethod.removeModifier(aJCTMethod
				.getModifiers()
				.iterator()
				.next());
		}

		for (final Modifier m : node.getModifiers().getFlags())
			aJCTMethod.addModifier(JCTModifiers.valueOf(m
				.toString()
				.toUpperCase()));

		for (final VariableTree v : node.getParameters())
			aJCTMethod.addParameter((IJCTParameter) v.accept(this, p));

		if (this.visitingLevel.contains(VisitingLevel.STATEMENTS)
				&& null != node.getBody())
			aJCTMethod.setBody((IJCTBlock) node.getBody().accept(this, p));

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING))
			for (final ExpressionTree e : node.getThrows()) {
				IJCTClassType t;

				switch (e.getKind()) {
					case MEMBER_SELECT :
						this.counter.inc();
						t =
							((IJCTClass) ((JCTree.JCFieldAccess) e).sym.accept(
								this,
								p)).createClassType();
						break;
					case IDENTIFIER :
						this.counter.inc();
						t =
							((IJCTClass) ((JCTree.JCIdent) e).sym.accept(
								this,
								p)).createClassType();
						break;
					default :
						throw new IllegalStateException("method \"" + node
								+ "\" unknown exception type kind ==> "
								+ e.getKind());
				}

				aJCTMethod.addThrownException(t);
			}

		return this.putSourceCodePosition(aJCTMethod, node);
	}

	public IJCTElement visitMethodInvocation(
		final MethodInvocationTree node,
		final Object p) {
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createMethodInvocation(this.factory
				.createSimpleSelector((IJCTMethod) null));
		this.counter.inc();
		final IJCTMethodInvocation aJCTMethodInvocation =
			this.factory.createMethodInvocation((IJCTSelector<IJCTMethod>) node
				.getMethodSelect()
				.accept(this, p));

		for (final ExpressionTree a : node.getArguments())
			aJCTMethodInvocation
				.addArgument((IJCTExpression) a.accept(this, p));

		return this.putSourceCodePosition(aJCTMethodInvocation, node);
	}

	public IJCTElement visitModifiers(final ModifiersTree node, final Object p) {
		this.counter.inc();
		return this.error(node, p);
	}

	public IJCTElement visitNewArray(final NewArrayTree node, final Object p) {
		this.counter.inc();
		// Recover underlying type, store the number of unspecified dimensions
		Tree type = node.getType();
		int unspecifiedDims = 0;
		IJCTType t = null;

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING) && null != type) {
			while (Tree.Kind.ARRAY_TYPE == type.getKind()) {
				type = ((ArrayTypeTree) type).getType();
				++unspecifiedDims;
			}

			switch (type.getKind()) {
				case PRIMITIVE_TYPE :
					t = (IJCTType) type.accept(this, p);
					break;
				case MEMBER_SELECT :
					this.counter.inc();
					t =
						((IJCTClass) ((JCTree.JCFieldAccess) type).sym.accept(
							this,
							p)).createClassType();
					break;
				case IDENTIFIER :
					this.counter.inc();
					t =
						((IJCTClass) ((JCTree.JCIdent) type).sym
							.accept(this, p)).createClassType();
					break;
				default :
					throw new IllegalStateException("new array \"(" + node
							+ ")\" unknown type kind ==> " + type.getKind());
			}
		}

		final IJCTNewArray aJCTNewArray = this.factory.createNewArray(t);

		// sets dimensions
		for (final ExpressionTree d : node.getDimensions())
			aJCTNewArray.addDimension((IJCTExpression) d.accept(this, p));

		// computes the real number of unspecified dimensions :
		//  number of unspecified dimensions + (if (nullp ( node.getInitializers() )) 0 1)
		// set initializers
		if (null != node.getInitializers()) {
			++unspecifiedDims;
			for (final ExpressionTree i : node.getInitializers())
				aJCTNewArray.addInitializer((IJCTExpression) i.accept(this, p));
		}

		aJCTNewArray.setUnspecifiedDimensions(unspecifiedDims);

		return this.putSourceCodePosition(aJCTNewArray, node);
	}

	public IJCTElement visitNewClass(final NewClassTree node, final Object p) {
		this.counter.inc();
		Tree type = node.getIdentifier();

		if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
			type = ((ParameterizedTypeTree) type).getType();

		final IJCTClassType aJCTClassType;

		if (this.visitingLevel.contains(VisitingLevel.BOUNDING))
			switch (type.getKind()) {
				case MEMBER_SELECT :
					this.counter.inc();
					aJCTClassType =
						((IJCTClass) ((JCTree.JCFieldAccess) type).sym.accept(
							this,
							p)).createClassType();
					break;
				case IDENTIFIER :
					this.counter.inc();
					if (null == ((JCTree.JCIdent) type).sym)
						System.err.println(((JCTree.JCIdent) type)
							.getName()
							.toString());
					aJCTClassType =
						((IJCTClass) ((JCTree.JCIdent) type).sym
							.accept(this, p)).createClassType();
					break;
				default :
					throw new IllegalStateException("new class \"" + node
							+ "\" unknown class identifier type kind ==> "
							+ type.getKind());
			}
		else
			aJCTClassType = null;

		final IJCTNewClass aJCTNewClass =
			this.factory.createNewClass(aJCTClassType);

		for (final ExpressionTree a : node.getArguments())
			aJCTNewClass.addArgument((IJCTExpression) a.accept(this, p));

		if (null != node.getEnclosingExpression())
			aJCTNewClass.setSelectingExpression((IJCTExpression) node
				.getEnclosingExpression()
				.accept(this, p));

		if (null != node.getClassBody()) {
			final IJCTClass c = (IJCTClass) node.getClassBody().accept(this, p);

			if (null != aJCTClassType
					&& null != aJCTClassType.getSelector().getElement()
					&& aJCTClassType
						.getSelector()
						.getElement()
						.getIsInterface())
				c.addDirectlyImplementedInterface(aJCTClassType);
			else
				c.setDirectSuperClass(aJCTClassType);

			aJCTNewClass.setAnnonymousClass(c);
		}

		return this.putSourceCodePosition(aJCTNewClass, node);
	}

	public IJCTElement visitParameterizedType(
		final ParameterizedTypeTree node,
		final Object p) {
		this.counter.inc();
		return node.getType().accept(this, p);
	}

	public IJCTElement visitParenthesized(
		final ParenthesizedTree node,
		final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(this.factory
			.createParenthesis((IJCTExpression) node.getExpression().accept(
				this,
				p)), node);
	}

	public IJCTElement visitPrimitiveType(
		final PrimitiveTypeTree node,
		final Object p) {
		this.counter.inc();
		JCTPrimitiveTypes t = null;
		final String name = node.getPrimitiveTypeKind().name().toUpperCase();

		if ("INT".equals(name))
			t = JCTPrimitiveTypes.INTEGER;
		else if ("CHAR".equals(name))
			t = JCTPrimitiveTypes.CHARACTER;
		else
			t = JCTPrimitiveTypes.valueOf(name);

		return this.rootNode.getType(t);
	}

	public IJCTElement visitReturn(final ReturnTree node, final Object p) {
		this.counter.inc();
		final IJCTReturn aJCTReturn = this.factory.createReturn();
		if (null != node.getExpression())
			aJCTReturn.setReturnedExpression((IJCTExpression) node
				.getExpression()
				.accept(this, p));
		return this.putSourceCodePosition(aJCTReturn, node);
	}

	public IJCTElement visitSwitch(final SwitchTree node, final Object p) {
		this.counter.inc();
		final IJCTSwitch aJCTSwitch =
			this.factory.createSwitch((IJCTExpression) node
				.getExpression()
				.accept(this, p));
		for (final CaseTree c : node.getCases())
			if (null == c.getExpression())
				aJCTSwitch.setDefaultCase((IJCTCase) c.accept(this, p));
			else
				aJCTSwitch.addCase((IJCTCase) c.accept(this, p));
		return this.putSourceCodePosition(aJCTSwitch, node);
	}

	public IJCTElement visitSynchronized(
		final SynchronizedTree node,
		final Object p) {
		this.counter.inc();
		final IJCTSynchronized aJCTSynchronized =
			this.factory.createSynchronized((IJCTExpression) node
				.getExpression()
				.accept(this, p));
		aJCTSynchronized.setBody((IJCTBlock) node.getBlock().accept(this, p));
		return this.putSourceCodePosition(aJCTSynchronized, node);
	}

	public IJCTElement visitThrow(final ThrowTree node, final Object p) {
		this.counter.inc();
		return this.putSourceCodePosition(
			this.factory.createThrow((IJCTExpression) node
				.getExpression()
				.accept(this, p)),
			node);
	}

	public IJCTElement visitTry(final TryTree node, final Object p) {
		this.counter.inc();
		final IJCTTry aJCTTry = this.factory.createTry();

		aJCTTry.setTryBlock((IJCTBlock) node.getBlock().accept(this, p));

		if (null != node.getFinallyBlock())
			aJCTTry.setFinallyBlock((IJCTBlock) node.getFinallyBlock().accept(
				this,
				p));

		for (final CatchTree c : node.getCatches())
			aJCTTry.addCatchBlock((IJCTCatch) c.accept(this, p));

		return this.putSourceCodePosition(aJCTTry, node);
	}

	public IJCTElement visitTypeCast(final TypeCastTree node, final Object p) {
		this.counter.inc();
		IJCTType t = null;

		final IJCTExpression e =
			(IJCTExpression) node.getExpression().accept(this, p);

		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING))
			return this.factory.createCast(null, e);

		Tree type = node.getType();

		if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
			type = ((ParameterizedTypeTree) type).getType();

		switch (type.getKind()) {
			case ARRAY_TYPE :
			case PRIMITIVE_TYPE :
				t = (IJCTType) type.accept(this, p);
				break;
			case MEMBER_SELECT :
				this.counter.inc();
				t =
					((IJCTClass) ((JCTree.JCFieldAccess) type).sym.accept(
						this,
						p)).createClassType();
				break;
			case IDENTIFIER :
				this.counter.inc();
				t =
					((IJCTClass) ((JCTree.JCIdent) type).sym.accept(this, p))
						.createClassType();
				break;
			default :
				throw new IllegalStateException("cast \"(" + node
						+ ")\" unknown type kind ==> " + type.getKind());
		}

		final IJCTCast aJCTCast = this.factory.createCast(t, e);

		return this.putSourceCodePosition(aJCTCast, node);
	}

	public IJCTElement visitTypeParameter(
		final TypeParameterTree node,
		final Object p) {
		return this.visitOther(node, p);
	}

	public IJCTElement visitUnary(final UnaryTree node, final Object p) {
		this.counter.inc();
		final IJCTExpression operand =
			(IJCTExpression) node.getExpression().accept(this, p);
		IJCTUnaryOperator result;
		switch (node.getKind()) {
			case BITWISE_COMPLEMENT :
				result = this.factory.createBitwiseComplement(operand);
				break;
			case UNARY_MINUS :
				result = this.factory.createUnaryMinus(operand);
				break;
			case UNARY_PLUS :
				result = this.factory.createUnaryPlus(operand);
				break;
			case PREFIX_DECREMENT :
				result = this.factory.createPrefixDecrement(operand);
				break;
			case PREFIX_INCREMENT :
				result = this.factory.createPrefixIncrement(operand);
				break;
			case POSTFIX_DECREMENT :
				result = this.factory.createPostfixDecrement(operand);
				break;
			case POSTFIX_INCREMENT :
				result = this.factory.createPostfixIncrement(operand);
				break;
			case LOGICAL_COMPLEMENT :
				result = this.factory.createLogicalComplement(operand);
				break;
			default :
				throw new IllegalStateException(
					"An unary operator must have a unary operator kind");
		}
		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitVariable(final VariableTree node, final Object p) {
		this.counter.inc();
		if (!this.visitingLevel.contains(VisitingLevel.BOUNDING)) {
			final IJCTVariable v = this.factory.createVariable("");
			v.setType(this.rootNode.getType(JCTPrimitiveTypes.VOID));
			return v;
		}

		final IJCTVariable v =
			(IJCTVariable) this.identifiables
				.get(((JCTree.JCVariableDecl) node).sym);

		if (v != null)
			return v;

		IJCTType t = null;

		Tree type = node.getType();

		if (Tree.Kind.PARAMETERIZED_TYPE == type.getKind())
			type = ((ParameterizedTypeTree) type).getType();

		switch (type.getKind()) {
			case ARRAY_TYPE :
			case PRIMITIVE_TYPE :
				t = (IJCTType) type.accept(this, p);
				break;
			case MEMBER_SELECT :
				this.counter.inc();
				t =
					((IJCTClass) ((JCTree.JCFieldAccess) type).sym.accept(
						this,
						p)).createClassType();
				break;
			case IDENTIFIER :
				this.counter.inc();
				t =
					((IJCTClass) ((JCTree.JCIdent) type).sym.accept(this, p))
						.createClassType();
				break;
			default :
				throw new IllegalStateException("variable \"(" + node
						+ ")\" unknown type kind ==> " + type.getKind());
		}

		final IJCTVariable aJCTVariable =
			this.factory.createVariable(node.getName().toString());
		aJCTVariable.setType(t);

		this.identifiables
			.put(((JCTree.JCVariableDecl) node).sym, aJCTVariable);

		if (this.visitingLevel.contains(VisitingLevel.STATEMENTS)
				&& null != node.getInitializer())
			aJCTVariable.setInitialValue((IJCTExpression) node
				.getInitializer()
				.accept(this, p));

		for (final Modifier m : node.getModifiers().getFlags())
			aJCTVariable.addModifier(JCTModifiers.valueOf(m
				.toString()
				.toUpperCase()));

		return this.putSourceCodePosition(aJCTVariable, node);
	}

	public IJCTElement visitWhileLoop(final WhileLoopTree node, final Object p) {
		this.counter.inc();
		final IJCTWhile result =
			this.factory.createWhile((IJCTExpression) node
				.getCondition()
				.accept(this, p));
		result.setBody((IJCTStatement) node.getStatement().accept(this, p));

		if (JCTKind.PARENTHESIS == result.getCondition().getKind()) {
			final IJCTExpression expr =
				((IJCTParenthesis) result.getCondition()).getExpression();
			((IJCTParenthesis) result.getCondition())
				.setExpression(this.factory.createBooleanLiteral(true));
			result.setCondition(expr);
		}

		return this.putSourceCodePosition(result, node);
	}

	public IJCTElement visitWildcard(final WildcardTree node, final Object p) {
		return this.visitOther(node, p);
	}
}
