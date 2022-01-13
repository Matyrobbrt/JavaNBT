package com.matyrobbrt.javanbt.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.matyrobbrt.javanbt.nbt.ByteNBT;
import com.matyrobbrt.javanbt.nbt.CompoundNBT;
import com.matyrobbrt.javanbt.nbt.DoubleNBT;
import com.matyrobbrt.javanbt.nbt.FloatNBT;
import com.matyrobbrt.javanbt.nbt.IntNBT;
import com.matyrobbrt.javanbt.nbt.LongNBT;
import com.matyrobbrt.javanbt.nbt.NBT;
import com.matyrobbrt.javanbt.nbt.ShortNBT;
import com.matyrobbrt.javanbt.nbt.StringNBT;
import com.matyrobbrt.javanbt.serialization.NBTSerializable;

public class NBTBuilder {

	private final Map<String, NBT> tags;

	public NBTBuilder() {
		tags = new HashMap<>();
	}

	private NBTBuilder(CompoundNBT tag) {
		tags = tag.entriesModifiable();
	}

	public static NBTBuilder of(CompoundNBT otherTag) {
		return new NBTBuilder(otherTag);
	}

	/**
	 * Stores a new NBTTagString with the given string value into the map with the
	 * given string key.
	 */
	public NBTBuilder putString(String key, String value) {
		return put(key, StringNBT.valueOf(value));
	}

	public NBTBuilder putByte(String pKey, byte pValue) {
		return put(pKey, ByteNBT.valueOf(pValue));
	}

	/**
	 * Stores a new NBTTagShort with the given short value into the map with the
	 * given string key.
	 */
	public NBTBuilder putShort(String pKey, short pValue) {
		return put(pKey, ShortNBT.valueOf(pValue));
	}

	/**
	 * Stores a new NBTTagInt with the given integer value into the map with the
	 * given string key.
	 */
	public NBTBuilder putInt(String pKey, int pValue) {
		return put(pKey, IntNBT.valueOf(pValue));
	}

	/**
	 * Stores a new NBTTagLong with the given long value into the map with the given
	 * string key.
	 */
	public NBTBuilder putLong(String pKey, long pValue) {
		return put(pKey, LongNBT.valueOf(pValue));
	}

	/**
	 * Stores a new NBTTagFloat with the given float value into the map with the
	 * given string key.
	 */
	public NBTBuilder putFloat(String pKey, float pValue) {
		return put(pKey, FloatNBT.valueOf(pValue));
	}

	/**
	 * Stores a new NBTTagDouble with the given double value into the map with the
	 * given string key.
	 */
	public NBTBuilder putDouble(String pKey, double pValue) {
		return put(pKey, DoubleNBT.valueOf(pValue));
	}

	public NBTBuilder putStringUUID(String key, UUID value) {
		return putString(key, value.toString());
	}

	public NBTBuilder put(String key, NBT value) {
		tags.put(key, value);
		return this;
	}

	/**
	 * Stores a NBT Serializable value by serializing it when it is inserted
	 */
	public NBTBuilder put(String key, NBTSerializable<?> value) {
		tags.put(key, value.serializeNBT());
		return this;
	}

	public CompoundNBT build() {
		return new CompoundNBT(tags);
	}

}
