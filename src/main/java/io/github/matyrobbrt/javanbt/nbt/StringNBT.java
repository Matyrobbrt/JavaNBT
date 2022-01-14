package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class StringNBT implements NBT {

	public static final NBTType<StringNBT> TYPE = new NBTType<StringNBT>() {

		@Override
		public StringNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(288L);
			String s = pInput.readUTF();
			pAccounter.readUTF(s);
			return StringNBT.valueOf(s);
		}

		@Override
		public String getName() { return "STRING"; }

		@Override
		public String getPrettyName() { return "TAG_String"; }

		@Override
		public boolean isValue() { return true; }

		@Override
		public StringNBT fromJson(JsonElement json) {
			return StringNBT.valueOf(json.getAsString());
		}
	};
	private static final StringNBT EMPTY = new StringNBT("");
	private final String data;

	private StringNBT(String data) {
		Objects.requireNonNull(data, "Null string not allowed");
		this.data = data;
	}

	public static StringNBT valueOf(String data) {
		return data.isEmpty() ? EMPTY : new StringNBT(data);
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(this.data);
	}

	@Override
	public byte getId() { return 8; }

	@Override
	public NBTType<StringNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return quoteAndEscape(this.data);
	}

	@Override
	public StringNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof StringNBT snbt && Objects.equals(this.data, snbt.data);
		}
	}

	@Override
	public int hashCode() {
		return this.data.hashCode();
	}

	@Override
	public String getAsString() { return this.data; }

	public static String quoteAndEscape(String text) {
		StringBuilder builder = new StringBuilder(" ");
		char c0 = 0;
		for (int i = 0; i < text.length(); ++i) {
			char c1 = text.charAt(i);
			if (c1 == '\\') {
				builder.append('\\');
			} else if (c1 == '"' || c1 == '\'') {
				if (c0 == 0) {
					c0 = (char) (c1 == '"' ? 39 : 34);
				}

				if (c0 == c1) {
					builder.append('\\');
				}
			}
			builder.append(c1);
		}
		if (c0 == 0) {
			c0 = '"';
		}
		builder.setCharAt(0, c0);
		builder.append(c0);
		return builder.toString();
	}

	@Override
	public JsonElement toJson() {
		return new JsonPrimitive(getAsString());
	}
}
