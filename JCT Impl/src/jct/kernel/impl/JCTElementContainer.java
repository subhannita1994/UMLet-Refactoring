/**
 * @author Mathieu Lemoine
 * @created 2008-09-30 (火)
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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import jct.kernel.IJCTElement;
import jct.kernel.IJCTElementContainer;
import jct.kernel.IJCTRootNode;
import jct.kernel.JCTKind;
import jct.util.ListOfElements;
import jct.util.collection.ListOfUnique;
import jct.util.reference.NotNullableReference;
import jct.util.reference.NullableReference;
import jct.util.reference.StrongReference;

/**
 * This class represents the composite role of the composite DP
 * It is strongly recommended that the implementations of this interface implements also {@link jct.util.IJCTContainer}
 * 
 * Default implementation for {@link jct.kernel.IJCTElementContainer}
 *
 * @author Mathieu Lemoine
 */
abstract class JCTElementContainer<Element extends IJCTElement> extends
		JCTElement<Element> implements IJCTElementContainer<Element> {
	/**
	 * elements of this element container
	 */
	protected final Collection<Element> elements;

	/**
	 * Identifiable contructor
	 */
	JCTElementContainer(
		final IJCTRootNode aRootNode,
		final String name,
		final Collection<Element> elements) {
		super(aRootNode, name);
		this.elements = elements;
	}

	/**
	 * Non-Identifiable constructor
	 */
	JCTElementContainer(
		final IJCTRootNode aRootNode,
		final Collection<Element> elements) {
		super(aRootNode);
		this.elements = elements;
	}

	/**
	 * Identifiable contructor with backpatch enclosed elements
	 */
	JCTElementContainer(final IJCTRootNode aRootNode, final String name) {
		this(aRootNode, name, null);
	}

	/**
	 * Non-Identifiable constructor with backpatch enclosed elements
	 */
	JCTElementContainer(final IJCTRootNode aRootNode) {
		this(aRootNode, (Collection<Element>) null);
	}

	/**
	 * Returns an unmodifiable Collection of enclosed elements.
	 */
	@Override
	public Collection<Element> getEnclosedElements() {
		return Collections
			.unmodifiableCollection(null == this.elements ? new HashSet<Element>()
					: this.elements);
	}

	/**
	 * Returns the List of elements potentialy designated by a next path part on this kind.
	 */
	protected <T extends IJCTElement> ListOfElements<T> seeNextPathStep(
		final JCTKind aKind) {
		final ListOfElements<T> result = new ListOfElements<T>();
		for (final Element e : this.getEnclosedElements())
			if (null != e && aKind == e.getKind())
				result.add((T) e);
		return result;
	}

	/**
	 * Discards the Cached Path Part Builder Index stored in each enclosed element after e
	 */
	private void discardEnclosedElementsCachedPathPartBuilderIndex(
		final JCTElement e) {
		final Iterator<Element> it = this.getEnclosedElements().iterator();
		while (it.hasNext() && e != it.next())
			;
		while (it.hasNext()) {
			final Element elem = it.next();
			if (null != elem && elem instanceof JCTElement)
				((JCTElement) elem).discardCachedPathPartBuilderIndex();
		}
	}

	/**
	 * Backpatch the (Enclosed) Elements field
	 */
	protected void backpatchElements(final Collection<Element> elements) {
		try {
			final Field rootField =
				JCTElementContainer.class.getDeclaredField("elements");
			rootField.setAccessible(true);
			rootField.set(this, elements);
			rootField.setAccessible(false);
		}
		catch (final NoSuchFieldException ex) {
			throw new LinkageError(ex.toString());
		}
		catch (final IllegalAccessException ex) {
			throw new LinkageError(ex.toString());
		}
	}

	protected <E extends Element> List<E> createInternalList() {
		return this.new InternalList<E>();
	}

	protected <E extends Element> List<E> createInternalList(
		final Collection<? extends E> c) {
		return this.new InternalList<E>(c);
	}

	protected class InternalList<E extends Element> extends ListOfUnique<E>
			implements Serializable {
		private static final long serialVersionUID = -2225408481133433783L;

		protected InternalList() {
		}

		protected InternalList(final Collection<? extends E> c) {
			super(c);
		}

		@Override
		public void add(final int i, final E e) {
			final int old_index = this.indexOf(e);

			if (i == old_index)
				return;

			if (-1 == old_index)
				((JCTElement) e)
					.updateEnclosingElement(JCTElementContainer.this);
			else {
				final int max = i > old_index ? i : old_index;
				final ListIterator<E> it =
					this.listIterator(i < old_index ? i : old_index);
				while (it.hasNext() && it.nextIndex() <= max) {
					final E elem = it.next();
					if (elem instanceof JCTElement)
						((JCTElement) elem).discardCachedPathPartBuilderIndex();
				}
			}

			super.add(i, e);

			if (-1 == old_index)
				JCTElementContainer.this
					.discardEnclosedElementsCachedPathPartBuilderIndex((JCTElement) e);
		}

		@Override
		public E remove(final int i) {
			final JCTElement e = (JCTElement) this.get(i);
			JCTElementContainer.this
				.discardEnclosedElementsCachedPathPartBuilderIndex(e);
			e.updateEnclosingElement(null);
			return super.remove(i);
		}

		@Override
		public E set(final int i, final E e) {
			final E old = this.get(i);

			if (e != old) {
				((JCTElement) old).updateEnclosingElement(null);
				((JCTElement) e)
					.updateEnclosingElement(JCTElementContainer.this);
				return super.set(i, e);
			}

			return e;
		}
	}

	protected <E extends Element> Set<E> createInternalSet() {
		return this.new InternalSet<E>();
	}

	protected <E extends Element> Set<E> createInternalSet(
		final Collection<? extends E> c) {
		return this.new InternalSet<E>(c);
	}

	protected class InternalSet<E extends Element> extends AbstractSet<E>
			implements Serializable {
		private static final long serialVersionUID = -1401830580503719064L;

		private final Set<E> set;

		private void preRemoveClean(final E e) {
			JCTElementContainer.this
				.discardEnclosedElementsCachedPathPartBuilderIndex((JCTElement) e);
			((JCTElement) e).updateEnclosingElement(null);
		}

		protected InternalSet() {
			this.set = new HashSet<E>();
		}

		protected InternalSet(final Collection<? extends E> c) {
			this.set = new HashSet<E>(c);
		}

		@Override
		public int size() {
			return this.set.size();
		}

		@Override
		public Iterator<E> iterator() {
			return this.new InternalIterator(this.set.iterator());
		}

		@Override
		public boolean add(final E e) {
			if (this.contains(e))
				return false;

			((JCTElement) e).updateEnclosingElement(JCTElementContainer.this);
			return this.set.add(e);
		}

		public boolean remove(final E e) {
			if (!this.contains(e))
				return false;

			this.preRemoveClean(e);
			return this.set.remove(e);
		}

		public boolean contains(final E e) {
			return this.set.contains(e);
		}

		protected class InternalIterator implements Iterator<E>, Serializable {
			private final Iterator<E> it;
			private E last;

			protected E getLast() {
				return this.last;
			}

			public InternalIterator(final Iterator<E> it) {
				this.it = it;
			}

			public boolean hasNext() {
				return this.it.hasNext();
			}

			public E next() {
				return this.last = this.it.next();
			}

			public void remove() {
				InternalSet.this.preRemoveClean(this.last);
				this.it.remove();
			}
		}
	}

	protected <E extends Element> NullableReference<E> createNullableInternalReference() {
		return this.new NullableInternalReference<E>();
	}

	protected <E extends Element> NullableReference<E> createNullableInternalReference(
		final Collection<? extends E> c) {
		return this.new NullableInternalReference<E>(c);
	}

	protected <E extends Element> NullableReference<E> createNullableInternalReference(
		final E element) {
		return this.new NullableInternalReference<E>(element);
	}

	protected class NullableInternalReference<E extends Element> extends
			StrongReference<E> implements NullableReference<E>, Serializable {
		private static final long serialVersionUID = -5438887130354559416L;

		protected NullableInternalReference() {
		}

		protected NullableInternalReference(final Collection<? extends E> c) {
			super(c);
		}

		protected NullableInternalReference(final E element) {
			super(element);
		}

		@Override
		public E set(final E e) {
			if (this.get() == e)
				return e;

			final E old = super.set(e);
			try {
				if (null != e)
					((JCTElement) e)
						.updateEnclosingElement(JCTElementContainer.this);

				if (null != old)
					((JCTElement) old).updateEnclosingElement(null);

			}
			catch (final RuntimeException ex) {
				super.set(old);
				throw ex;
			}

			return old;
		}
	}

	protected <E extends Element> NotNullableReference<E> createInternalReference(
		final E element) {
		return this.new InternalReference<E>(element);
	}

	protected class InternalReference<E extends Element> extends
			StrongReference<E> implements NotNullableReference<E>, Serializable {
		private static final long serialVersionUID = 1009922541030252910L;

		protected InternalReference(final E element) {
			super(element);
			((JCTElement) this.get())
				.updateEnclosingElement(JCTElementContainer.this);
		}

		@Override
		public E set(final E e) {
			if (this.get() == e)
				return e;

			final E old = super.set(e);

			try {
				((JCTElement) e)
					.updateEnclosingElement(JCTElementContainer.this);
				((JCTElement) old).updateEnclosingElement(null);
			}
			catch (final RuntimeException ex) {
				super.set(old);
				throw ex;
			}

			return super.set(e);
		}
	}

	private static final long serialVersionUID = -3260402134579560937L;

}
