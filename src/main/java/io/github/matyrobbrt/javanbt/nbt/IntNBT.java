package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntNBT extends NumberNBT {

	public static final NBTType<IntNBT> TYPE = new NBTType<IntNBT>() {

		@Override
		public IntNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(96L);
			return IntNBT.valueOf(pInput.readInt());
		}

		@Override
		public String getName() { return "INT"; }

		@Override
		public String getPrettyName() { return "TAG_Int"; }

		@Override
		public boolean isValue() { return true; }
	};

	private final int data;

	private IntNBT(int pData) {
		this.data = pData;
	}

	public static IntNBT valueOf(int data) {
		return new IntNBT(data);
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
		pOutput.writeInt(this.data);
	}

	@Override
	public byte getId() { return 3; }

	@Override
	public NBTType<IntNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return String.valueOf(this.data);
	}

	@Override
	public IntNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof IntNBT intNBT && this.data == intNBT.data;
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
	public short getAsShort() { return (short) (this.data & '\uffff'); }

	@Override
	public byte getAsByte() { return (byte) (this.data & 255); }

	@Override
	public double getAsDouble() { return this.data; }

	@Override
	public float getAsFloat() { return this.data; }

	@Override
	public Number getAsNumber() { return this.data; }
}
