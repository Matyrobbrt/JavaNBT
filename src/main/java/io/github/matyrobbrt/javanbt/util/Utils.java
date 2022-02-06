package io.github.matyrobbrt.javanbt.util;

import java.util.Map.Entry;
import java.util.function.Supplier;

public class Utils {

	public static <T> T supplier(final Supplier<T> supplier) {
		return supplier.get();
	}

	public static <K, V> Entry<K, V> makeEntry(final K key, final V value, final boolean immutable) {
		return new Entry$<>(key, value, immutable);
	}

	public static <K, V> Entry<K, V> makeEntry(final K key, final V value) {
		return makeEntry(key, value, true);
	}

	private static final class Entry$<K, V> implements Entry<K, V> {

		private final boolean immutable;
		private final K key;
		private V value;

		private Entry$(K key, V value, boolean immutable) {
			this.key = key;
			this.value = value;
			this.immutable = immutable;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			if (immutable) { return this.value; }
			final var oldValue = this.value;
			this.value = value;
			return oldValue;
		}

	}

}
