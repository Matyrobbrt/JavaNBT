package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class IntArrayNBT extends CollectionNBT<IntNBT> {

	public static final NBTType<IntArrayNBT> TYPE = new NBTType<IntArrayNBT>() {

		@Override
		public IntArrayNBT load(DataInput input, int depth, NBTSizeTracker tracker) throws IOException {
			tracker.accountBits(192L);
			int i = input.readInt();
			tracker.accountBits(32L * i);
			int[] ints = new int[i];

			for (int index = 0; index < i; ++index) {
				ints[index] = input.readInt();
			}

			return new IntArrayNBT(ints);
		}

		@Override
		public String getName() { return "INT[]"; }

		@Override
		public String getPrettyName() { return "TAG_Int_Array"; }

		@Override
		public IntArrayNBT fromJson(JsonElement json) {
			final var bytes = new ArrayList<Integer>();
			if (json.isJsonArray()) {
				json.getAsJsonArray().forEach(e -> bytes.add(e.getAsInt()));
			}
			return new IntArrayNBT(bytes);
		}
	};
	private int[] data;

	public IntArrayNBT(int[] data) {
		this.data = data;
	}

	public IntArrayNBT(List<Integer> data) {
		this(toArray(data));
	}

	private static int[] toArray(List<Integer> data) {
		int[] aint = new int[data.size()];
		for (int i = 0; i < data.size(); ++i) {
			Integer integer = data.get(i);
			aint[i] = integer == null ? 0 : integer;
		}
		return aint;
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);
		for (int i : this.data) {
			output.writeInt(i);
		}
	}

	@Override
	public byte getId() { return 11; }

	@Override
	public NBTType<IntArrayNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[I;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				builder.append(',');
			}
			builder.append(this.data[i]);
		}
		return builder.append(']').toString();
	}

	@Override
	public IntArrayNBT copy() {
		int[] aint = new int[this.data.length];
		System.arraycopy(this.data, 0, aint, 0, this.data.length);
		return new IntArrayNBT(aint);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof IntArrayNBT intANBT && Arrays.equals(this.data, intANBT.data);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.data);
	}

	public int[] getAsIntArray() { return this.data; }

	@Override
	public int size() {
		return this.data.length;
	}

	public void addInt(int value) {
		add(IntNBT.valueOf(value));
	}

	public int getInt(int index) {
		return get(index).getAsInt();
	}

	@Override
	public IntNBT get(int index) {
		return IntNBT.valueOf(this.data[index]);
	}

	@Override
	public IntNBT set(int index, IntNBT intNBT) {
		int i = this.data[index];
		this.data[index] = intNBT.getAsInt();
		return IntNBT.valueOf(i);
	}

	@Override
	public void add(int index, IntNBT intNBT) {
		this.data = insert(index, data, intNBT.getAsInt());
	}

	private static int[] insert(final int index, final int[] array, final int... values) {
		if (array == null) { return null; }
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}
		final int[] result = new int[array.length + values.length];
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
	public boolean setNBT(int index, NBT nbt) {
		if (nbt instanceof NumberNBT numNBT) {
			this.data[index] = numNBT.getAsInt();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addNBT(int index, NBT nbt) {
		if (nbt instanceof NumberNBT numNBT) {
			this.data = insert(index, data, numNBT.getAsInt());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public IntNBT remove(int index) {
		int i = this.data[index];
		this.data = remove(this.data, index);
		return IntNBT.valueOf(i);
	}

	private static int[] remove(final int[] array, final int index) {
		return (int[]) remove((Object) array, index);
	}

	@Override
	public byte getElementType() { return 3; }

	@Override
	public void clear() {
		this.data = new int[0];
	}

	@Override
	public JsonElement toJson() {
		JsonArray array = new JsonArray();
		forEach(nbt -> array.add(nbt.getAsNumber()));
		return array;
	}
}
