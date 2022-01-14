package io.github.matyrobbrt.javanbt.util;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;
import jakarta.annotation.Nonnull;

public class NBTReader {

	private final CompoundNBT nbt;

	private NBTReader(CompoundNBT nbt) {
		this.nbt = nbt;
	}

	public static NBTReader of(CompoundNBT nbt) {
		return new NBTReader(nbt);
	}

	@SuppressWarnings("unchecked")
	public <NBTTYPE extends NBT> NBTReader load(@Nonnull String key, @Nonnull NBTSerializable<NBTTYPE> object) {
		object.deserializeNBT((NBTTYPE) nbt.get(key));
		return this;
	}

	public boolean contains(String key) {
		return nbt.contains(key);
	}

}
