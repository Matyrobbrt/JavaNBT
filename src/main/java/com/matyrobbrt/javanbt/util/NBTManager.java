package com.matyrobbrt.javanbt.util;

import java.util.HashMap;
import java.util.Map;

import com.matyrobbrt.javanbt.nbt.CompoundNBT;
import com.matyrobbrt.javanbt.serialization.NBTSerializable;

import jakarta.annotation.Nonnull;

public class NBTManager implements NBTSerializable<CompoundNBT> {

	private final Map<String, NBTSerializable<?>> data = new HashMap<>();

	public NBTManager track(@Nonnull String key, @Nonnull NBTSerializable<?> toTrack) {
		data.put(key, toTrack);
		return this;
	}

	@Override
	public CompoundNBT serializeNBT() {
		NBTBuilder builder = new NBTBuilder();
		data.forEach((key, serializable) -> builder.put(key, serializable.serializeNBT()));
		return builder.build();
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		NBTReader reader = NBTReader.of(nbt);
		data.forEach((key, serializable) -> {
			if (reader.contains(key)) {
				reader.load(key, serializable);
			}
		});
	}
}
