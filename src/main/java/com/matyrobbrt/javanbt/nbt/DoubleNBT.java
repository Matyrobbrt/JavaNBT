package com.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleNBT extends NumberNBT {

	public static final DoubleNBT ZERO = new DoubleNBT(0.0D);
	public static final NBTType<DoubleNBT> TYPE = new NBTType<DoubleNBT>() {

		@Override
		public DoubleNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(128L);
			return DoubleNBT.valueOf(pInput.readDouble());
		}

		@Override
		public String getName() { return "DOUBLE"; }

		@Override
		public String getPrettyName() { return "TAG_Double"; }

		@Override
		public boolean isValue() { return true; }
	};
	private final double data;

	private DoubleNBT(double pData) {
		this.data = pData;
	}

	public static DoubleNBT valueOf(double pData) {
		return pData == 0.0D ? ZERO : new DoubleNBT(pData);
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
		pOutput.writeDouble(this.data);
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
}
