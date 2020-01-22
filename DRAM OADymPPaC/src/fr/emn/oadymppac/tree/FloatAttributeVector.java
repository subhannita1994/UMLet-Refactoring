/*
 * $Id: FloatAttributeVector.java,v 1.1 2004/03/01 15:37:17 guehene Exp $
 *
 * Copyright (c) 2001-2002 Jean-Daniel Fekete, Mohammad Ghoniem and
 * Ecole des Mines de Nantes.  All rights reserved.
 *
 * This software is proprietary information of Jean-Daniel Fekete and
 * Ecole des Mines de Nantes.  You shall use it only in accordance
 * with the terms of the license agreement you accepted when
 * downloading this software.  The license is available in the file
 * licence.txt and at the following URL:
 * http://www.emn.fr/fekete/oadymppac/licence.html
 */

package fr.emn.oadymppac.tree;

public class FloatAttributeVector extends AttributeVector {
	static public FloatAttributeVector find(
		final String name,
		final AttributedTree tree) {
		FloatAttributeVector vec =
			(FloatAttributeVector) tree.getAttributeVector(name);
		if (vec == null) {
			vec = new FloatAttributeVector(name, tree.capacity());
			tree.addAttributeVector(vec);
		}
		return vec;
	}

	static public FloatAttributeVector findSum(
		final String name,
		final AttributedTree tree) {
		final String sumName = name + "_sum";
		FloatAttributeVector vec =
			(FloatAttributeVector) tree.getAttributeVector(sumName);
		if (vec == null) {
			tree.visit(new SumWeightVisitor(tree, name));
			vec = (FloatAttributeVector) tree.getAttributeVector(sumName);
		}
		return vec;
	}
	protected float[] attribute;

	public FloatAttributeVector(final FloatAttributeVector other) {
		super(other);
		this.attribute = new float[other.capacity()];
		System.arraycopy(other.attribute, 0, this.attribute, 0, this.size);
	}

	public FloatAttributeVector(final String name, final int capacity) {
		super(name, capacity);
		this.attribute = new float[capacity];
	}

	public int capacity() {
		return this.attribute.length;
	}

	public final float getAttribute(final int index) {
		return this.attribute[index];
	}

	public Class getAttributeClass() {
		return Float.class;
	}

	public Object getValue(final int index) {
		return new Float(this.getAttribute(index));
	}
	public void reserve(final int sz) {
		if (this.attribute.length < sz) {
			final float[] newattribute = new float[sz];
			System.arraycopy(this.attribute, 0, newattribute, 0, this.size);
			this.attribute = newattribute;
		}
	}
	public final void setAttribute(final int index, final float val) {
		this.attribute[index] = val;
	}

}
