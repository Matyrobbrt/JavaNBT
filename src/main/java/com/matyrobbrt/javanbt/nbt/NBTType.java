package com.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.IOException;

public interface NBTType<T extends NBT> {

	T load(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;

	default boolean isValue() { return false; }

	String getName();

	String getPrettyName();

	static NBTType<EndNBT> createInvalidNBT(final int id) {
		return new NBTType<EndNBT>() {

			@Override
			public EndNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
				throw new IllegalArgumentException("Invalid tag id: " + id);
			}

			@Override
			public String getName() { return "INVALID[" + id + "]"; }

			@Override
			public String getPrettyName() { return "UNKNOWN_" + id; }
		};
	}
}