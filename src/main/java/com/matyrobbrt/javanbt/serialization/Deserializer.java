package com.matyrobbrt.javanbt.serialization;

import com.matyrobbrt.javanbt.nbt.NBT;

@FunctionalInterface
public interface Deserializer<N extends NBT, T> {

	T fromNBT(N nbt);

}
