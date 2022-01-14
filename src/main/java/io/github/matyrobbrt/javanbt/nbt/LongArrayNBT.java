package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LongArrayNBT extends CollectionNBT<LongNBT> {

	public static final NBTType<LongArrayNBT> TYPE = new NBTType<LongArrayNBT>() {

		@Override
		public LongArrayNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(192L);
			int i = pInput.readInt();
			pAccounter.accountBits(64L * i);
			long[] along = new long[i];

			for (int j = 0; j < i; ++j) {
				along[j] = pInput.readLong();
			}

			return new LongArrayNBT(along);
		}

		@Override
		public String getName() { return "LONG[]"; }

		@Override
		public String getPrettyName() { return "TAG_Long_Array"; }
	};
	private long[] data;

	public LongArrayNBT(long[] data) {
		this.data = data;
	}

	public LongArrayNBT(List<Long> dataAsList) {
		this(toArray(dataAsList));
	}

	private static long[] toArray(List<Long> dataAsList) {
		long[] along = new long[dataAsList.size()];
		for (int i = 0; i < dataAsList.size(); ++i) {
			Long olong = dataAsList.get(i);
			along[i] = olong == null ? 0L : olong;
		}
		return along;
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		for (long i : this.data) {
			output.writeLong(i);
		}
	}

	@Override
	public byte getId() { return 12; }

	@Override
	public NBTType<LongArrayNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[L;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				builder.append(',');
			}
			builder.append(this.data[i]).append('L');
		}
		return builder.append(']').toString();
	}

	@Override
	public LongArrayNBT copy() {
		long[] along = new long[this.data.length];
		System.arraycopy(this.data, 0, along, 0, this.data.length);
		return new LongArrayNBT(along);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof LongArrayNBT lanbt && Arrays.equals(this.data, lanbt.data);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.data);
	}

	public long[] getAsLongArray() { return this.data; }

	@Override
	public int size() {
		return this.data.length;
	}

	public void addLong(long value) {
		add(LongNBT.valueOf(value));
	}

	public long getLong(int index) {
		return get(index).getAsLong();
	}

	@Override
	public LongNBT get(int index) {
		return LongNBT.valueOf(this.data[index]);
	}

	@Override
	public LongNBT set(int index, LongNBT longNBT) {
		long i = this.data[index];
		this.data[index] = longNBT.getAsLong();
		return LongNBT.valueOf(i);
	}

	@Override
	public void add(int index, LongNBT longNBT) {
		this.data = insert(index, this.data, longNBT.getAsLong());
	}

	@Override
	public boolean setNBT(int index, NBT nbt) {
		if (nbt instanceof NumberNBT numNBT) {
			this.data[index] = numNBT.getAsLong();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addNBT(int index, NBT nbt) {
		if (nbt instanceof NumberNBT numNBT) {
			this.data = insert(index, this.data, numNBT.getAsLong());
			return true;
		} else {
			return false;
		}
	}

	public static long[] insert(final int index, final long[] array, final long... values) {
		if (array == null) { return null; }
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		final long[] result = new long[array.length + values.length];

		System.arraycopy(values, 0, result, index, values.length);
		if (index > 0) {
			System.arraycopy(array, 0, result, 0, index);
		}
		if (index < array.length) {
			System.arraycopy(array, index, result, index + values.length, array.length - index);
		}
		return result;
	}

	@Override
	public LongNBT remove(int index) {
		long i = this.data[index];
		this.data = remove(this.data, index);
		return LongNBT.valueOf(i);
	}

	public static long[] remove(final long[] array, final int index) {
		return (long[]) remove((Object) array, index);
	}

	@Override
	public byte getElementType() { return 4; }

	@Override
	public void clear() {
		this.data = new long[0];
	}
}
