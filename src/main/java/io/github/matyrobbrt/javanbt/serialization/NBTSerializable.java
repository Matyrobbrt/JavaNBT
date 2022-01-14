package io.github.matyrobbrt.javanbt.serialization;

import io.github.matyrobbrt.javanbt.nbt.NBT;

public interface NBTSerializable<N extends NBT> {

	N serializeNBT();

	void deserializeNBT(N nbt);

}
