package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class FloatNBT extends NumberNBT {

	public static final FloatNBT ZERO = new FloatNBT(0.0F);
	public static final NBTType<FloatNBT> TYPE = new NBTType<FloatNBT>() {

		@Override
		public FloatNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(96L);
			return FloatNBT.valueOf(pInput.readFloat());
		}

		@Override
		public String getName() { return "FLOAT"; }

		@Override
		public String getPrettyName() { return "TAG_Float"; }

		@Override
		public boolean isValue() { return true; }

		@Override
		public FloatNBT fromJson(JsonElement json) {
			return FloatNBT.valueOf(json.getAsFloat());
		}
	};
	private final float data;

	private FloatNBT(float pData) {
		this.data = pData;
	}

	public static FloatNBT valueOf(float pData) {
		return pData == 0.0F ? ZERO : new FloatNBT(pData);
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
		pOutput.writeFloat(this.data);
	}

	@Override
	public byte getId() { return 5; }

	@Override
	public NBTType<FloatNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return this.data + "f";
	}

	@Override
	public FloatNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof FloatNBT fnbt && this.data == fnbt.data;
		}
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(this.data);
	}

	@Override
	public long getAsLong() { return (long) this.data; }

	@Override
	public int getAsInt() { return (int) Math.floor(data); }

	@Override
	public short getAsShort() { return (short) ((int) Math.floor(data) & '\uffff'); }

	@Override
	public byte getAsByte() { return (byte) ((int) Math.floor(data) & 255); }

	@Override
	public double getAsDouble() { return this.data; }

	@Override
	public float getAsFloat() { return this.data; }

	@Override
	public Number getAsNumber() { return this.data; }

	@Override
	public JsonElement toJson() {
		return new JsonPrimitive(getAsNumber());
	}
}
