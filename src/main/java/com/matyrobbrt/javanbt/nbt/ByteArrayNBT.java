package com.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ByteArrayNBT extends CollectionNBT<ByteNBT> {

	public static final NBTType<ByteArrayNBT> TYPE = new NBTType<ByteArrayNBT>() {

		@Override
		public ByteArrayNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(192L);
			int i = pInput.readInt();
			pAccounter.accountBits(8L * i);
			byte[] abyte = new byte[i];
			pInput.readFully(abyte);
			return new ByteArrayNBT(abyte);
		}

		@Override
		public String getName() { return "BYTE[]"; }

		@Override
		public String getPrettyName() { return "TAG_Byte_Array"; }
	};
	private byte[] data;

	public ByteArrayNBT(byte[] pData) {
		this.data = pData;
	}

	public ByteArrayNBT(List<Byte> pDataList) {
		this(toArray(pDataList));
	}

	private static byte[] toArray(List<Byte> pDataList) {
		byte[] abyte = new byte[pDataList.size()];

		for (int i = 0; i < pDataList.size(); ++i) {
			Byte obyte = pDataList.get(i);
			abyte[i] = obyte == null ? 0 : obyte;
		}

		return abyte;
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
		pOutput.writeInt(this.data.length);
		pOutput.write(this.data);
	}

	@Override
	public byte getId() { return 7; }

	@Override
	public NBTType<ByteArrayNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[B;");
		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				builder.append(',');
			}
			builder.append(this.data[i]).append('B');
		}
		return builder.append(']').toString();
	}

	@Override
	public NBT copy() {
		byte[] abyte = new byte[this.data.length];
		System.arraycopy(this.data, 0, abyte, 0, this.data.length);
		return new ByteArrayNBT(abyte);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ByteArrayNBT banbt && Arrays.equals(this.data, banbt.data);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.data);
	}

	public byte[] getAsByteArray() { return this.data; }

	@Override
	public int size() {
		return this.data.length;
	}

	public void addByte(byte value) {
		add(ByteNBT.valueOf(value));
	}

	public byte getByte(int index) {
		return get(index).getAsByte();
	}

	@Override
	public ByteNBT get(int index) {
		return ByteNBT.valueOf(this.data[index]);
	}

	@Override
	public ByteNBT set(int index, ByteNBT nbt) {
		byte b0 = this.data[index];
		this.data[index] = nbt.getAsByte();
		return ByteNBT.valueOf(b0);
	}

	@Override
	public void add(int index, ByteNBT nbt) {
		this.data = insert(index, this.data, nbt.getAsByte());
	}

	@Override
	public boolean setNBT(int pIndex, NBT pTag) {
		if (pTag instanceof NumberNBT numNBT) {
			this.data[pIndex] = numNBT.getAsByte();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addNBT(int index, NBT tag) {
		if (tag instanceof NumberNBT numNBT) {
			this.data = insert(this.data.length, this.data, numNBT.getAsByte());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ByteNBT remove(int p_remove_1_) {
		byte b0 = this.data[p_remove_1_];
		this.data = remove(this.data, p_remove_1_);
		return ByteNBT.valueOf(b0);
	}

	public static byte[] remove(final byte[] array, final int index) {
		return (byte[]) remove((Object) array, index);
	}

	public static byte[] insert(final int index, final byte[] array, final byte... values) {
		if (array == null) { return null; }
		if (index < 0 || index > array.length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + array.length);
		}

		final byte[] result = new byte[array.length + values.length];

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
	public byte getElementType() { return 1; }

	@Override
	public void clear() {
		this.data = new byte[0];
	}
}
