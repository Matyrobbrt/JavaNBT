package com.matyrobbrt.javanbt.serialization;

import com.matyrobbrt.javanbt.nbt.NBT;

public interface NBTSerializable<N extends NBT> {

	N serializeNBT();

	void deserializeNBT(N nbt);

}
