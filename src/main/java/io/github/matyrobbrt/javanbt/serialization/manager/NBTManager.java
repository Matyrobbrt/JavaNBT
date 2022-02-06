package io.github.matyrobbrt.javanbt.serialization.manager;

import static io.github.matyrobbrt.javanbt.util.Utils.makeEntry;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.github.matyrobbrt.javanbt.annotation.NBTExposed;
import io.github.matyrobbrt.javanbt.nbt.ByteNBT;
import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.DoubleNBT;
import io.github.matyrobbrt.javanbt.nbt.FloatNBT;
import io.github.matyrobbrt.javanbt.nbt.IntNBT;
import io.github.matyrobbrt.javanbt.nbt.LongNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.nbt.NumberNBT;
import io.github.matyrobbrt.javanbt.nbt.ShortNBT;
import io.github.matyrobbrt.javanbt.nbt.StringNBT;
import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;
import io.github.matyrobbrt.javanbt.serialization.Serializers;
import io.github.matyrobbrt.javanbt.util.Utils;

@SuppressWarnings("unchecked")
public class NBTManager {

	//@formatter:off
	public static final Map<Class<?>, NBTTypeAdatper<?>> DEFAULT_TYPE_ADATPTERS = Map.ofEntries(
			makePrimitiveTypeAdapter(int.class, NumberNBT.class, NumberNBT::getAsInt, IntNBT::valueOf),
			makePrimitiveTypeAdapter(String.class, NBT.class, NBT::getAsString, StringNBT::valueOf),
			makePrimitiveTypeAdapter(byte.class, NumberNBT.class, NumberNBT::getAsByte, ByteNBT::valueOf),
			makePrimitiveTypeAdapter(short.class, NumberNBT.class, NumberNBT::getAsShort, ShortNBT::valueOf),
			makePrimitiveTypeAdapter(boolean.class, ByteNBT.class, n -> n.getAsByte() != 0, ByteNBT::valueOf),
			makePrimitiveTypeAdapter(double.class, NumberNBT.class, NumberNBT::getAsDouble, DoubleNBT::valueOf),
			makePrimitiveTypeAdapter(float.class, NumberNBT.class, NumberNBT::getAsFloat, FloatNBT::valueOf),
			makePrimitiveTypeAdapter(long.class, NumberNBT.class, NumberNBT::getAsLong, LongNBT::valueOf
	));
	
	private static <T, D extends NBT> Entry<Class<T>, NBTTypeAdatper<T>> makePrimitiveTypeAdapter(
			final Class<T> clazz, Class<D> serializationClass, Function<D, T> deserializer, Function<T, D> serializer) {
		return makeEntry(clazz, new NBTTypeAdatper<T>() {

			@Override
			public T fromNBT(NBT nbt) {
				if (serializationClass.isInstance(nbt)) { return deserializer.apply(serializationClass.cast(nbt)); }
				throw new NBTSerializationException("Invalid NBT types. Wanted %s, provided %s."
						.formatted(serializationClass.getSimpleName(), nbt.getClass()));
			}

			@Override
			public NBT toNBT(T value) {
				return serializer.apply(value);
			}
		});
	}
	
	static final Map<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
	
	static List<Field> getFields(final Class<?> clazz) {
		return FIELD_CACHE.computeIfAbsent(clazz, k -> {
			final List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
			fields.removeIf(f -> Modifier.isStatic(f.getModifiers()));
			fields.forEach(Field::trySetAccessible);
			return List.copyOf(fields);
		});
	}
	//@formatter:on

	private final boolean excludeFieldsWithoutAnnotation;

	private final Map<Class<?>, NBTTypeAdatper<?>> typeAdapters = new ConcurrentHashMap<>();

	public NBTManager(boolean excludeFieldsWithoutAnnotation) {
		this.excludeFieldsWithoutAnnotation = excludeFieldsWithoutAnnotation;
	}

	public <N extends NBT> N toNBTUnsafe(final Object object) throws NBTSerializationException {
		return (N) toNBT(object);
	}

	public NBT toNBT(final Object object) throws NBTSerializationException {
		final var clazz = object.getClass();
		if (object instanceof NBTSerializable<?> nbtSer) { return nbtSer.serializeNBT(); }
		if (DEFAULT_TYPE_ADATPTERS.containsKey(clazz)) { return DEFAULT_TYPE_ADATPTERS.get(clazz).unsafeToNBT(object); }
		if (typeAdapters.containsKey(clazz)) { return typeAdapters.get(clazz).unsafeToNBT(object); }
		// No type adapters or serializers? Then we force you into a compound
		final var nbt = new CompoundNBT();
		for (var field : getFields(clazz)) {
			try {
				if (!field.isAnnotationPresent(NBTExposed.class) && excludeFieldsWithoutAnnotation) {
					continue;
				}
				final var serializedName = getSerializationName(field);
				final var fieldSerialized = toNBT(field.get(object));
				nbt.put(serializedName, fieldSerialized);
			} catch (IllegalArgumentException | IllegalAccessException | InaccessibleObjectException e) {
				throw new NBTSerializationException(e);
			}
		}
		return nbt;
	}

	public <T> T fromNBT(final NBT nbt, Class<T> typeClazz) {
		// TODO fix primitives
		if (typeClazz == int.class) { return DEFAULT_TYPE_ADATPTERS.get(Integer.class).unsafeFromNBT(nbt); }
		if (DEFAULT_TYPE_ADATPTERS.containsKey(typeClazz)) {
			return DEFAULT_TYPE_ADATPTERS.get(typeClazz).unsafeFromNBT(nbt);
		}
		if (typeAdapters.containsKey(typeClazz)) { return typeAdapters.get(typeClazz).unsafeFromNBT(nbt); }
		if (Serializers.hasDeserializer(typeClazz)) {
			return Serializers.getDeserializerFor(typeClazz).unsafeFromNBT(nbt);
		}
		// No type adapters or serializers? Assume it's a compound
		if (nbt instanceof CompoundNBT compound) {
			try {
				final var constructor = typeClazz.getDeclaredConstructor();
				constructor.trySetAccessible();
				final T object = constructor.newInstance();
				try {
					for (var field : getFields(typeClazz)) {
						if (!field.isAnnotationPresent(NBTExposed.class) && excludeFieldsWithoutAnnotation) {
							continue;
						}
						final var serializedName = getSerializationName(field);
						final var fieldNBT = compound.get(serializedName);
						final var type = field.getType();
						final var fieldObject = fromNBT(fieldNBT, type);
						field.set(object, fieldObject);
					}
				} catch (IllegalArgumentException | IllegalAccessException | InaccessibleObjectException
						| SecurityException e) {
					throw new NBTSerializationException(e);
				}
				return object;
			} catch (Exception e) {
				throw new NBTSerializationException(e);
			}
		} else {
			throw new NBTSerializationException("How am I supposed to deserialize an object I know nothing about?");
		}
	}

	public static String getSerializationName(final Field field) {
		return field.isAnnotationPresent(NBTExposed.class) ? Utils.supplier(() -> {
			final var ann = field.getAnnotation(NBTExposed.class);
			if (ann.serializationName().isBlank()) {
				return field.getName();
			} else {
				return ann.serializationName();
			}
		}) : field.getName();
	}

	public static final class NBTSerializationException extends RuntimeException {

		private static final long serialVersionUID = 8162953139476014626L;

		public NBTSerializationException(final String msg, final Throwable error) {
			super(msg, error);
		}

		public NBTSerializationException(final Throwable error) {
			super(error);
		}

		public NBTSerializationException(final String msg) {
			super(msg);
		}

	}

}
