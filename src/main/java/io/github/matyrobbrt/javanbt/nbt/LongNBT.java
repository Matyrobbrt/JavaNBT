package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class LongNBT extends NumberNBT {

    public static final NBTType<LongNBT> TYPE = new NBTType<LongNBT>() {

        @Override
        public LongNBT load(DataInput inpyt, int depth, NBTSizeTracker tracker) throws IOException {
            tracker.accountBits(128L);
            return LongNBT.valueOf(inpyt.readLong());
        }

        @Override
        public String getName() {
            return "LONG";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Long";
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public LongNBT fromJson(JsonElement json) {
            return LongNBT.valueOf(json.getAsLong());
        }
    };
    private final long data;

    private LongNBT(long data) {
        this.data = data;
    }

    public static LongNBT valueOf(long data) {
        return new LongNBT(data);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeLong(this.data);
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public NBTType<LongNBT> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.data + "L";
    }

    @Override
    public LongNBT copy() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof LongNBT lnbt && this.data == lnbt.data;
        }
    }

    @Override
    public int hashCode() {
        return (int) (this.data ^ this.data >>> 32);
    }

    @Override
    public long getAsLong() {
        return this.data;
    }

    @Override
    public int getAsInt() {
        return (int) (this.data & -1L);
    }

    @Override
    public short getAsShort() {
        return (short) ((int) (this.data & 65535L));
    }

    @Override
    public byte getAsByte() {
        return (byte) ((int) (this.data & 255L));
    }

    @Override
    public double getAsDouble() {
        return this.data;
    }

    @Override
    public float getAsFloat() {
        return this.data;
    }

    @Override
    public Number getAsNumber() {
        return this.data;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getAsNumber());
    }
}
