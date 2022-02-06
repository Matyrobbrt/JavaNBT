package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class DoubleNBT extends NumberNBT {

	public static final DoubleNBT ZERO = new DoubleNBT(0.0D);
	public static final NBTType<DoubleNBT> TYPE = new NBTType<DoubleNBT>() {

		@Override
		public DoubleNBT load(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
			sizeTracker.accountBits(128L);
			return DoubleNBT.valueOf(input.readDouble());
		}

		@Override
		public String getName() { return "DOUBLE"; }

		@Override
		public String getPrettyName() { return "TAG_Double"; }

		@Override
		public boolean isValue() { return true; }

		@Override
		public DoubleNBT fromJson(JsonElement json) {
			return DoubleNBT.valueOf(json.getAsDouble());
		}
	};
	private final double data;

	private DoubleNBT(double data) {
		this.data = data;
	}

	public static DoubleNBT valueOf(double data) {
		return data == 0.0D ? ZERO : new DoubleNBT(data);
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeDouble(this.data);
	}

	@Override
	public byte getId() { return 6; }

	@Override
	public NBTType<DoubleNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return this.data + "d";
	}

	@Override
	public DoubleNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof DoubleNBT dnbt && this.data == dnbt.data;
		}
	}

	@Override
	public int hashCode() {
		long i = Double.doubleToLongBits(this.data);
		return (int) (i ^ i >>> 32);
	}

	@Override
	public long getAsLong() { return (long) Math.floor(this.data); }

	@Override
	public int getAsInt() { return (int) Math.floor(data); }

	@Override
	public short getAsShort() { return (short) ((int) Math.floor(data) & '\uffff'); }

	@Override
	public byte getAsByte() { return (byte) ((int) Math.floor(data) & 255); }

	@Override
	public double getAsDouble() { return this.data; }

	@Override
	public float getAsFloat() { return (float) this.data; }

	@Override
	public Number getAsNumber() { return this.data; }

	@Override
	public JsonElement toJson() {
		return new JsonPrimitive(getAsNumber());
	}
}
