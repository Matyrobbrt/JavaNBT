package io.github.matyrobbrt.javanbt.serialization;

import java.util.HashMap;
import java.util.Map;

import io.github.matyrobbrt.javanbt.nbt.NBT;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@SuppressWarnings("unchecked")
public class Serializers {

	private static final Map<Class<?>, Deserializer<?, ?>> DESERIALIZERS = new HashMap<>();

	public static <N extends NBT, T> Deserializer<N, T> registerDeserializer(final Class<T> clazz,
			Deserializer<N, T> deserializer) {
		return (Deserializer<N, T>) DESERIALIZERS.computeIfAbsent(clazz, k -> deserializer);
	}

	@Nullable
	public static <T> Deserializer<?, T> getDeserializerFor(@Nonnull Class<T> clazz) {
		return (Deserializer<?, T>) DESERIALIZERS.get(clazz);
	}
}
