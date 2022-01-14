package io.github.matyrobbrt.javanbt.converter;

public class StringReader {

	private static final char SYNTAX_ESCAPE = '\\';
	private static final char SYNTAX_DOUBLE_QUOTE = '"';
	private static final char SYNTAX_SINGLE_QUOTE = '\'';

	private final String string;
	private int cursor;

	public StringReader(final StringReader other) {
		this.string = other.string;
		this.cursor = other.cursor;
	}

	public StringReader(final String string) {
		this.string = string;
	}

	public String getString() { return string; }

	public void setCursor(final int cursor) { this.cursor = cursor; }

	public int getRemainingLength() { return string.length() - cursor; }

	public int getTotalLength() { return string.length(); }

	public int getCursor() { return cursor; }

	public String getRead() { return string.substring(0, cursor); }

	public String getRemaining() { return string.substring(cursor); }

	public boolean canRead(final int length) {
		return cursor + length <= string.length();
	}

	public boolean canRead() {
		return canRead(1);
	}

	public char peek() {
		return string.charAt(cursor);
	}

	public char peek(final int offset) {
		return string.charAt(cursor + offset);
	}

	public char read() {
		return string.charAt(cursor++);
	}

	public void skip() {
		cursor++;
	}

	public static boolean isAllowedNumber(final char c) {
		return c >= '0' && c <= '9' || c == '.' || c == '-';
	}

	public static boolean isQuotedStringStart(char c) {
		return c == SYNTAX_DOUBLE_QUOTE || c == SYNTAX_SINGLE_QUOTE;
	}

	public void skipWhitespace() {
		while (canRead() && Character.isWhitespace(peek())) {
			skip();
		}
	}

	public int readInt() throws SyntaxException {
		final int start = cursor;
		while (canRead() && isAllowedNumber(peek())) {
			skip();
		}
		final String number = string.substring(start, cursor);
		if (number.isEmpty()) { throw new SyntaxException("Int expected"); }
		try {
			return Integer.parseInt(number);
		} catch (final NumberFormatException ex) {
			cursor = start;
			throw new SyntaxException("Invalid int " + number);
		}
	}

	public long readLong() throws SyntaxException {
		final int start = cursor;
		while (canRead() && isAllowedNumber(peek())) {
			skip();
		}
		final String number = string.substring(start, cursor);
		if (number.isEmpty()) { throw new SyntaxException("Long expected"); }
		try {
			return Long.parseLong(number);
		} catch (final NumberFormatException ex) {
			cursor = start;
			throw new SyntaxException("Invalid long " + number);
		}
	}

	public double readDouble() throws SyntaxException {
		final int start = cursor;
		while (canRead() && isAllowedNumber(peek())) {
			skip();
		}
		final String number = string.substring(start, cursor);
		if (number.isEmpty()) { throw new SyntaxException("Double expected"); }
		try {
			return Double.parseDouble(number);
		} catch (final NumberFormatException ex) {
			cursor = start;
			throw new SyntaxException("Invalid double " + number);
		}
	}

	public float readFloat() throws SyntaxException {
		final int start = cursor;
		while (canRead() && isAllowedNumber(peek())) {
			skip();
		}
		final String number = string.substring(start, cursor);
		if (number.isEmpty()) { throw new SyntaxException("Float expected"); }
		try {
			return Float.parseFloat(number);
		} catch (final NumberFormatException ex) {
			cursor = start;
			throw new SyntaxException("Invalid float " + number);
		}
	}

	public static boolean isAllowedInUnquotedString(final char c) {
		return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.'
				|| c == '+';
	}

	public String readUnquotedString() {
		final int start = cursor;
		while (canRead() && isAllowedInUnquotedString(peek())) {
			skip();
		}
		return string.substring(start, cursor);
	}

	public String readQuotedString() throws SyntaxException {
		if (!canRead()) { return ""; }
		final char next = peek();
		if (!isQuotedStringStart(next)) { throw new SyntaxException("Expected start of quote"); }
		skip();
		return readStringUntil(next);
	}

	public String readStringUntil(char terminator) throws SyntaxException {
		final StringBuilder result = new StringBuilder();
		boolean escaped = false;
		while (canRead()) {
			final char c = read();
			if (escaped) {
				if (c == terminator || c == SYNTAX_ESCAPE) {
					result.append(c);
					escaped = false;
				} else {
					setCursor(getCursor() - 1);
					throw new SyntaxException("Invalid escape");
				}
			} else if (c == SYNTAX_ESCAPE) {
				escaped = true;
			} else if (c == terminator) {
				return result.toString();
			} else {
				result.append(c);
			}
		}

		throw new SyntaxException("Expected end of quote");
	}

	public String readString() throws SyntaxException {
		if (!canRead()) { return ""; }
		final char next = peek();
		if (isQuotedStringStart(next)) {
			skip();
			return readStringUntil(next);
		}
		return readUnquotedString();
	}

	public boolean readBoolean() throws SyntaxException {
		final int start = cursor;
		final String value = readString();
		if (value.isEmpty()) { throw new SyntaxException("Boolean expected"); }

		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		} else {
			cursor = start;
			throw new SyntaxException("Invalid boolean " + value);
		}
	}

	public void expect(final char c) throws SyntaxException {
		if (!canRead() || peek() != c) { throw new SyntaxException("Expected symbol"); }
		skip();
	}

	public static class SyntaxException extends Exception {

		private static final long serialVersionUID = 3240739312854738944L;

		public SyntaxException(String text, Throwable e) {
			super(text, e);
		}

		public SyntaxException(String text) {
			super(text);
		}
		
		public static class Runtime extends RuntimeException {

			private static final long serialVersionUID = -3912622787886440420L;

			public Runtime(String text, Throwable e) {
				super(text, e);
			}

			public Runtime(final Throwable e) {
				super(e);
			}

			public Runtime(String text) {
				super(text);
			}
			
		}

	}

}
