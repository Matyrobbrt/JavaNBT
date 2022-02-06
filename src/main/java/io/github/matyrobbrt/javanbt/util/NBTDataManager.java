package io.github.matyrobbrt.javanbt.util;

import java.util.HashMap;
import java.util.Map;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;
import jakarta.annotation.Nonnull;

public class NBTDataManager implements NBTSerializable<CompoundNBT> {

	private final Map<String, NBTSerializable<?>> data = new HashMap<>();

	public NBTDataManager track(@Nonnull String key, @Nonnull NBTSerializable<?> toTrack) {
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
