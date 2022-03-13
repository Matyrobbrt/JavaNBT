package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ShortNBT extends NumberNBT {

    public static final NBTType<ShortNBT> TYPE = new NBTType<ShortNBT>() {

        @Override
        public ShortNBT load(DataInput input, int depth, NBTSizeTracker tracker) throws IOException {
            tracker.accountBits(80L);
            return ShortNBT.valueOf(input.readShort());
        }

        @Override
        public String getName() {
            return "SHORT";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Short";
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public ShortNBT fromJson(JsonElement json) {
            return ShortNBT.valueOf(json.getAsShort());
        }
    };
    private final short data;

    private ShortNBT(short data) {
        this.data = data;
    }

    private static final Map<Short, ShortNBT> CACHE = new ConcurrentHashMap<>();

    public static ShortNBT valueOf(short data) {
        return CACHE.computeIfAbsent(data, ShortNBT::new);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeShort(this.data);
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    public NBTType<ShortNBT> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.data + "s";
    }

    @Override
    public ShortNBT copy() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof ShortNBT snbt && this.data == snbt.data;
        }
    }

    @Override
    public int hashCode() {
        return this.data;
    }

    @Override
    public long getAsLong() {
        return this.data;
    }

    @Override
    public int getAsInt() {
        return this.data;
    }

    @Override
    public short getAsShort() {
        return this.data;
    }

    @Override
    public byte getAsByte() {
        return (byte) (this.data & 255);
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
