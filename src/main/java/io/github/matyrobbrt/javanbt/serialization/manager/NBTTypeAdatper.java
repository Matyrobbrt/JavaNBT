package io.github.matyrobbrt.javanbt.serialization.manager;

import io.github.matyrobbrt.javanbt.nbt.NBT;

@SuppressWarnings("unchecked")
public interface NBTTypeAdatper<T> {

	T fromNBT(final NBT nbt);

	default <Z> Z unsafeFromNBT(final NBT nbt) {
		return (Z) fromNBT(nbt);
	}

	NBT toNBT(final T value);

	default NBT unsafeToNBT(Object object) {
		return toNBT((T) object);
	}

}
