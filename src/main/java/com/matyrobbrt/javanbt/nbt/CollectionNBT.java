package com.matyrobbrt.javanbt.nbt;

import java.lang.reflect.Array;
import java.util.AbstractList;

public abstract class CollectionNBT<T extends NBT> extends AbstractList<T> implements NBT {

	public abstract boolean setNBT(int pIndex, NBT nbt);

	public abstract boolean addNBT(int pIndex, NBT nbt);

	public abstract byte getElementType();

	protected static Object remove(final Object array, final int index) {
		final int length = Array.getLength(array);
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}

		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}
}
