package io.github.matyrobbrt.javanbt.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.github.matyrobbrt.javanbt.converter.StringReader.SyntaxException;
import io.github.matyrobbrt.javanbt.nbt.ByteArrayNBT;
import io.github.matyrobbrt.javanbt.nbt.ByteNBT;
import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.DoubleNBT;
import io.github.matyrobbrt.javanbt.nbt.FloatNBT;
import io.github.matyrobbrt.javanbt.nbt.IntArrayNBT;
import io.github.matyrobbrt.javanbt.nbt.IntNBT;
import io.github.matyrobbrt.javanbt.nbt.ListNBT;
import io.github.matyrobbrt.javanbt.nbt.LongArrayNBT;
import io.github.matyrobbrt.javanbt.nbt.LongNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.nbt.NBTType;
import io.github.matyrobbrt.javanbt.nbt.NumberNBT;
import io.github.matyrobbrt.javanbt.nbt.ShortNBT;
import io.github.matyrobbrt.javanbt.nbt.StringNBT;

// TODO better expections
public class StringToNBTConverter {

    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern
        .compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN = Pattern
        .compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN = Pattern
        .compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    private final StringReader reader;

    public static CompoundNBT parseNBT(String pText) {
        return (new StringToNBTConverter(new StringReader(pText))).readSingleStruct();
    }

    CompoundNBT readSingleStruct() {
        CompoundNBT compoundnbt = null;
        try {
            compoundnbt = this.readStruct();
        } catch (SyntaxException e) {
            throw new SyntaxException.Runtime(e);
        }
        this.reader.skipWhitespace();
        if (this.reader.canRead()) {
            throw new SyntaxException.Runtime("Trailing data");
        } else {
            return compoundnbt;
        }
    }

    public StringToNBTConverter(StringReader pReader) {
        this.reader = pReader;
    }

    protected String readKey() throws SyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw new SyntaxException.Runtime("Key expected");
        } else {
            return this.reader.readString();
        }
    }

    protected NBT readTypedValue() throws SyntaxException {
        this.reader.skipWhitespace();
        int i = this.reader.getCursor();
        if (StringReader.isQuotedStringStart(this.reader.peek())) {
            return StringNBT.valueOf(this.reader.readQuotedString());
        } else {
            String s = this.reader.readUnquotedString();
            if (s.isEmpty()) {
                this.reader.setCursor(i);
                throw new SyntaxException("Expected value");
            } else {
                return type(s);
            }
        }
    }

    public static NBT type(String value) {
        try {
            if (FLOAT_PATTERN.matcher(value).matches()) {
                return FloatNBT.valueOf(Float.parseFloat(value.substring(0, value.length() - 1)));
            }

            if (BYTE_PATTERN.matcher(value).matches()) {
                return ByteNBT.valueOf(Byte.parseByte(value.substring(0, value.length() - 1)));
            }

            if (LONG_PATTERN.matcher(value).matches()) {
                return LongNBT.valueOf(Long.parseLong(value.substring(0, value.length() - 1)));
            }

            if (SHORT_PATTERN.matcher(value).matches()) {
                return ShortNBT.valueOf(Short.parseShort(value.substring(0, value.length() - 1)));
            }

            if (INT_PATTERN.matcher(value).matches()) { return IntNBT.valueOf(Integer.parseInt(value)); }

            if (DOUBLE_PATTERN.matcher(value).matches()) {
                return DoubleNBT.valueOf(Double.parseDouble(value.substring(0, value.length() - 1)));
            }

            if (DOUBLE_PATTERN_NOSUFFIX.matcher(value).matches()) {
                return DoubleNBT.valueOf(Double.parseDouble(value));
            }

            if ("true".equalsIgnoreCase(value)) { return ByteNBT.ONE; }

            if ("false".equalsIgnoreCase(value)) { return ByteNBT.ZERO; }
        } catch (NumberFormatException numberformatexception) {}

        return StringNBT.valueOf(value);
    }

    public NBT readValue() throws SyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw new SyntaxException("Expected value");
        } else {
            char c0 = this.reader.peek();
            if (c0 == '{') {
                return this.readStruct();
            } else {
                return c0 == '[' ? this.readList() : this.readTypedValue();
            }
        }
    }

    protected NBT readList() throws SyntaxException {
        return this.reader.canRead(3) && !StringReader.isQuotedStringStart(this.reader.peek(1))
            && this.reader.peek(2) == ';' ? this.readArrayTag() : this.readListTag();
    }

    public CompoundNBT readStruct() throws SyntaxException {
        this.expect('{');
        CompoundNBT compoundnbt = new CompoundNBT();
        this.reader.skipWhitespace();

        while (this.reader.canRead() && this.reader.peek() != '}') {
            int i = this.reader.getCursor();
            String s = this.readKey();
            if (s.isEmpty()) {
                this.reader.setCursor(i);
                throw new SyntaxException("Expected key");
            }

            this.expect(':');
            compoundnbt.put(s, this.readValue());
            if (!this.hasElementSeparator()) {
                break;
            }

            if (!this.reader.canRead()) { throw new SyntaxException("Expected key"); }
        }

        this.expect('}');
        return compoundnbt;
    }

    private NBT readListTag() throws SyntaxException {
        this.expect('[');
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw new SyntaxException("Expected value");
        } else {
            ListNBT nbtList = new ListNBT();
            NBTType<?> nbtType = null;

            while (this.reader.peek() != ']') {
                int i = this.reader.getCursor();
                NBT inbt = this.readValue();
                NBTType<?> nbtType1 = inbt.getType();
                if (nbtType == null) {
                    nbtType = nbtType1;
                } else if (nbtType1 != nbtType) {
                    this.reader.setCursor(i);
                    throw new SyntaxException(
                        "Mixed lists: " + nbtType.getPrettyName() + " and " + nbtType1.getPrettyName());
                }

                nbtList.add(inbt);
                if (!this.hasElementSeparator()) {
                    break;
                }

                if (!this.reader.canRead()) { throw new SyntaxException("Expected value"); }
            }

            this.expect(']');
            return nbtList;
        }
    }

    private NBT readArrayTag() throws SyntaxException {
        this.expect('[');
        int i = this.reader.getCursor();
        char c0 = this.reader.read();
        this.reader.read();
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw new SyntaxException("Expected value");
        } else if (c0 == 'B') {
            return new ByteArrayNBT(this.readArray(ByteArrayNBT.TYPE, ByteNBT.TYPE));
        } else if (c0 == 'L') {
            return new LongArrayNBT(this.readArray(LongArrayNBT.TYPE, LongNBT.TYPE));
        } else if (c0 == 'I') {
            return new IntArrayNBT(this.readArray(IntArrayNBT.TYPE, IntNBT.TYPE));
        } else {
            this.reader.setCursor(i);
            throw new SyntaxException("Invalid array " + c0);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Number> List<T> readArray(NBTType<?> arrayType, NBTType<?> elementType) throws SyntaxException {
        List<T> list = new ArrayList<>();

        while (true) {
            if (this.reader.peek() != ']') {
                int i = this.reader.getCursor();
                NBT nbt = this.readValue();
                NBTType<?> nbtType = nbt.getType();
                if (nbtType != elementType) {
                    this.reader.setCursor(i);
                    throw new SyntaxException(
                        "Mixed arrays: " + nbtType.getPrettyName() + " and " + arrayType.getPrettyName());
                }

                if (elementType == ByteNBT.TYPE) {
                    list.add((T) (Byte) ((NumberNBT) nbt).getAsByte());
                } else if (elementType == LongNBT.TYPE) {
                    list.add((T) (Long) ((NumberNBT) nbt).getAsLong());
                } else {
                    list.add((T) (Integer) ((NumberNBT) nbt).getAsInt());
                }

                if (this.hasElementSeparator()) {
                    if (!this.reader.canRead()) { throw new SyntaxException("Value expected!"); }
                    continue;
                }
            }

            this.expect(']');
            return list;
        }
    }

    private boolean hasElementSeparator() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == ',') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    private void expect(char pExpected) throws SyntaxException {
        this.reader.skipWhitespace();
        this.reader.expect(pExpected);
    }

}
