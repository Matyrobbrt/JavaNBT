package com.matyrobbrt.javanbt.nbt;

public abstract class NumberNBT implements NBT {

	protected NumberNBT() {
	}

	public abstract long getAsLong();

	public abstract int getAsInt();

	public abstract short getAsShort();

	public abstract byte getAsByte();

	public abstract double getAsDouble();

	public abstract float getAsFloat();

	public abstract Number getAsNumber();
}
