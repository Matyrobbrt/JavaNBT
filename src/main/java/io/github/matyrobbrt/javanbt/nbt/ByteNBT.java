package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ByteNBT extends NumberNBT {

	public static final NBTType<ByteNBT> TYPE = new NBTType<ByteNBT>() {

		@Override
		public ByteNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(72L);
			return ByteNBT.valueOf(pInput.readByte());
		}

		@Override
		public String getName() { return "BYTE"; }

		@Override
		public String getPrettyName() { return "TAG_Byte"; }

		@Override
		public boolean isValue() { return true; }

		@Override
		public ByteNBT fromJson(JsonElement json) {
			return ByteNBT.valueOf(json.getAsByte());
		}
	};

	public static final ByteNBT ZERO = valueOf((byte) 0);
	public static final ByteNBT ONE = valueOf((byte) 1);
	private final byte data;

	private ByteNBT(byte pData) {
		this.data = pData;
	}

	public static ByteNBT valueOf(byte pData) {
		return ByteNBT.Cache.CACHE[128 + pData];
	}

	public static ByteNBT valueOf(boolean pData) {
		return pData ? ONE : ZERO;
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
		pOutput.writeByte(this.data);
	}

	@Override
	public byte getId() { return 1; }

	@Override
	public NBTType<ByteNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return this.data + "b";
	}

	@Override
	public ByteNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ByteNBT otherNBT && this.data == otherNBT.data;
		}
	}

	@Override
	public int hashCode() {
		return this.data;
	}

	@Override
	public long getAsLong() { return this.data; }

	@Override
	public int getAsInt() { return this.data; }

	@Override
	public short getAsShort() { return this.data; }

	@Override
	public byte getAsByte() { return this.data; }

	@Override
	public double getAsDouble() { return this.data; }

	@Override
	public float getAsFloat() { return this.data; }

	@Override
	public Number getAsNumber() { return this.data; }

	static class Cache {

		private static final ByteNBT[] CACHE = new ByteNBT[256];

		static {
			for (int i = 0; i < CACHE.length; ++i) {
				CACHE[i] = new ByteNBT((byte) (i - 128));
			}

		}
	}

	@Override
	public JsonElement toJson() {
		return new JsonPrimitive(getAsNumber());
	}
}
