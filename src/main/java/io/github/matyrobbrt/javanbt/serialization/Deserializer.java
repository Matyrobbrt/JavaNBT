package io.github.matyrobbrt.javanbt.serialization;

import io.github.matyrobbrt.javanbt.nbt.NBT;

@FunctionalInterface
public interface Deserializer<N extends NBT, T> {

	T fromNBT(N nbt);

	@SuppressWarnings("unchecked")
	default <Z> Z unsafeFromNBT(NBT nbt) {
		return (Z) fromNBT((N) nbt);
	};

}
