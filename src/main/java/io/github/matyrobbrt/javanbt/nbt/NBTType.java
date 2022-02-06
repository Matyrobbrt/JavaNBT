package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.IOException;

import com.google.gson.JsonElement;

public interface NBTType<T extends NBT> {

	T load(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;

	default boolean isValue() { return false; }

	String getName();

	String getPrettyName();

	static NBTType<EndNBT> createInvalidNBT(final int id) {
		return new NBTType<EndNBT>() {

			@Override
			public EndNBT load(DataInput input, int depth, NBTSizeTracker tracker) throws IOException {
				throw new IllegalArgumentException("Invalid tag id: " + id);
			}

			@Override
			public String getName() { return "INVALID[" + id + "]"; }

			@Override
			public String getPrettyName() { return "UNKNOWN_" + id; }

			@Override
			public EndNBT fromJson(JsonElement json) {
				throw new IllegalArgumentException("Invalid tag id: " + id);
			}
		};
	}

	T fromJson(JsonElement json);
}