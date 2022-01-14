package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;
import jakarta.annotation.Nullable;

public class CompoundNBT implements NBT {

	private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
	public static final NBTType<CompoundNBT> TYPE = new NBTType<>() {

		@Override
		public CompoundNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(384L);
			if (pDepth > 512) {
				throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
			} else {
				Map<String, NBT> map = new HashMap<>();

				byte b0;
				while ((b0 = CompoundNBT.readNamedNBTType(pInput, pAccounter)) != 0) {
					String s = CompoundNBT.readNamedNBTName(pInput, pAccounter);
					pAccounter.accountBits(224 + 16 * s.length());
					pAccounter.accountBits(32); // Forge: 4 extra bytes for the object allocation.
					NBT inbt = CompoundNBT.readNamedNBTData(NBTTypes.getType(b0), s, pInput, pDepth + 1, pAccounter);
					if (map.put(s, inbt) != null) {
						pAccounter.accountBits(288L);
					}
				}

				return new CompoundNBT(map);
			}
		}

		@Override
		public String getName() { return "COMPOUND"; }

		@Override
		public String getPrettyName() { return "TAG_Compound"; }
	};
	private final Map<String, NBT> tags;

	public CompoundNBT(Map<String, NBT> tags) {
		this.tags = tags;
	}

	public CompoundNBT() {
		this(new HashMap<>());
	}

	@Override
	public void write(DataOutput output) throws IOException {
		for (var entry : tags.entrySet()) {
			writeNamedNBT(entry.getKey(), entry.getValue(), output);
		}
		output.writeByte(0);
	}

	public Set<String> getAllKeys() { return tags.keySet(); }

	@Override
	public byte getId() { return 10; }

	@Override
	public NBTType<CompoundNBT> getType() { return TYPE; }

	public int size() {
		return tags.size();
	}

	@Nullable
	public NBT put(String key, NBT value) {
		if (value == null) { throw new IllegalArgumentException("Invalid null NBT value with key " + key); }
		return tags.put(key, value);
	}

	public void put(String key, NBTSerializable<?> serializable) {
		put(key, serializable.serializeNBT());
	}

	public void putByte(String key, byte value) {
		put(key, ByteNBT.valueOf(value));
	}

	public void putShort(String key, short value) {
		put(key, ShortNBT.valueOf(value));
	}

	public void putInt(String key, int value) {
		put(key, IntNBT.valueOf(value));
	}

	public void putLong(String key, long value) {
		put(key, LongNBT.valueOf(value));
	}

	public void putFloat(String key, float value) {
		put(key, FloatNBT.valueOf(value));
	}

	public void putDouble(String key, double value) {
		put(key, DoubleNBT.valueOf(value));
	}

	public void putString(String key, String value) {
		put(key, StringNBT.valueOf(value));
	}

	public void putByteArray(String key, byte[] value) {
		put(key, new ByteArrayNBT(value));
	}

	public void putIntArray(String key, int[] value) {
		put(key, new IntArrayNBT(value));
	}

	public void putIntArray(String key, List<Integer> value) {
		put(key, new IntArrayNBT(value));
	}

	public void putLongArray(String key, long[] value) {
		put(key, new LongArrayNBT(value));
	}

	public void putLongArray(String key, List<Long> value) {
		put(key, new LongArrayNBT(value));
	}

	public void putBoolean(String key, boolean value) {
		put(key, ByteNBT.valueOf(value));
	}

	@Nullable
	public NBT get(String key) {
		return tags.get(key);
	}

	public byte getTagType(String key) {
		NBT inbt = tags.get(key);
		return inbt == null ? 0 : inbt.getId();
	}

	public boolean contains(String key) {
		return tags.containsKey(key);
	}

	public boolean contains(String key, int pTagType) {
		int i = this.getTagType(key);
		if (i == pTagType) {
			return true;
		} else if (pTagType != 99) {
			return false;
		} else {
			return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
		}
	}

	public byte getByte(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsByte(); }
		} catch (ClassCastException classcastexception) {}

		return 0;
	}

	public short getShort(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsShort(); }
		} catch (ClassCastException classcastexception) {}

		return 0;
	}

	public int getInt(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsInt(); }
		} catch (ClassCastException classcastexception) {}

		return 0;
	}

	public long getLong(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsLong(); }
		} catch (ClassCastException classcastexception) {}

		return 0L;
	}

	public float getFloat(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsFloat(); }
		} catch (ClassCastException classcastexception) {}

		return 0.0F;
	}

	public double getDouble(String key) {
		try {
			if (this.contains(key, 99)) { return ((NumberNBT) tags.get(key)).getAsDouble(); }
		} catch (ClassCastException classcastexception) {}

		return 0.0D;
	}

	public String getString(String key) {
		try {
			if (this.contains(key, 8)) { return tags.get(key).getAsString(); }
		} catch (ClassCastException classcastexception) {}

		return "";
	}

	public byte[] getByteArray(String key) {
		try {
			if (this.contains(key, 7)) { return ((ByteArrayNBT) tags.get(key)).getAsByteArray(); }
		} catch (ClassCastException classcastexception) {
			throw new RuntimeException(classcastexception);
		}

		return new byte[0];
	}

	public int[] getIntArray(String key) {
		try {
			if (this.contains(key, 11)) { return ((IntArrayNBT) tags.get(key)).getAsIntArray(); }
		} catch (ClassCastException classcastexception) {
			throw new RuntimeException(classcastexception);
		}

		return new int[0];
	}

	public long[] getLongArray(String key) {
		try {
			if (this.contains(key, 12)) { return ((LongArrayNBT) tags.get(key)).getAsLongArray(); }
		} catch (ClassCastException classcastexception) {
			throw new RuntimeException(classcastexception);
		}

		return new long[0];
	}

	public CompoundNBT getCompound(String key) {
		try {
			if (this.contains(key, 10)) { return (CompoundNBT) tags.get(key); }
		} catch (ClassCastException classcastexception) {
			throw new RuntimeException(classcastexception);
		}

		return new CompoundNBT();
	}

	public ListNBT getList(String key, int pTagType) {
		try {
			if (this.getTagType(key) == 9) {
				ListNBT listnbt = (ListNBT) tags.get(key);
				if (!listnbt.isEmpty() && listnbt.getElementType() != pTagType) { return new ListNBT(); }

				return listnbt;
			}
		} catch (ClassCastException classcastexception) {
			throw new RuntimeException(classcastexception);
		}

		return new ListNBT();
	}

	public boolean getBoolean(String key) {
		return this.getByte(key) != 0;
	}

	public void remove(String key) {
		tags.remove(key);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		Collection<String> keys = tags.keySet();
		for (String str : keys) {
			if (builder.length() != 1) {
				builder.append(',');
			}
			builder.append(handleEscape(str)).append(':').append(tags.get(str));
		}
		return builder.append('}').toString();
	}

	public boolean isEmpty() { return tags.isEmpty(); }

	@Override
	public CompoundNBT copy() {
		Map<String, NBT> map = new HashMap<>();
		tags.forEach((key, nbt) -> map.put(key, nbt.copy()));
		return new CompoundNBT(map);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof CompoundNBT nbt && Objects.equals(tags, nbt.tags);
		}
	}

	@Override
	public int hashCode() {
		return tags.hashCode();
	}

	private static void writeNamedNBT(String name, NBT nbt, DataOutput output) throws IOException {
		output.writeByte(nbt.getId());
		if (nbt.getId() != 0) {
			output.writeUTF(name);
			nbt.write(output);
		}
	}

	private static byte readNamedNBTType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
		sizeTracker.accountBits(8);
		return input.readByte();
	}

	private static String readNamedNBTName(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
		return sizeTracker.readUTF(input.readUTF());
	}

	private static NBT readNamedNBTData(NBTType<?> type, String name, DataInput input, int depth,
			NBTSizeTracker sizeTracker) {
		try {
			return type.load(input, depth, sizeTracker);
		} catch (IOException ioexception) {
			throw new RuntimeException(ioexception);
		}
	}

	/**
	 * Copies all the tags of {@code other} into this tag, then returns itself.
	 *
	 * @see #copy()
	 */
	public CompoundNBT merge(CompoundNBT other) {
		for (String s : other.tags.keySet()) {
			NBT inbt = other.tags.get(s);
			if ((inbt.getId() == 10) && this.contains(s, 10)) {
				CompoundNBT compoundnbt = this.getCompound(s);
				compoundnbt.merge((CompoundNBT) inbt);
			} else {
				this.put(s, inbt.copy());
			}
		}
		return this;
	}

	protected static String handleEscape(String str) {
		return SIMPLE_VALUE.matcher(str).matches() ? str : StringNBT.quoteAndEscape(str);
	}

	/**
	 * @return an unmodifiable containing the entries in this compound
	 */
	protected Map<String, NBT> entries() {
		return Collections.unmodifiableMap(tags);
	}

	/**
	 * @return the entries in this compound
	 */
	public Map<String, NBT> entriesModifiable() {
		return tags;
	}

}
