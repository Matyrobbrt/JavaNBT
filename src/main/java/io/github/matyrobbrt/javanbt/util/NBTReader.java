package io.github.matyrobbrt.javanbt.util;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.ListNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;
import jakarta.annotation.Nonnull;

public class NBTReader {

    private final CompoundNBT nbt;

    private NBTReader(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static NBTReader of(CompoundNBT nbt) {
        return new NBTReader(nbt);
    }

    @SuppressWarnings("unchecked")
    public <NBTTYPE extends NBT> NBTReader load(@Nonnull String key, @Nonnull NBTSerializable<NBTTYPE> object) {
        object.deserializeNBT((NBTTYPE) nbt.get(key));
        return this;
    }

    public byte readByte(String key) {
        return nbt.getByte(key);
    }

    public short readShort(String key) {
        return nbt.getShort(key);
    }

    public int readInt(String key) {
        return nbt.getInt(key);
    }

    public long readLong(String key) {
        return nbt.getLong(key);
    }

    public float readFloat(String key) {
        return nbt.getFloat(key);
    }

    public double readDouble(String key) {
        return nbt.getDouble(key);
    }

    public String readString(String key) {
        return nbt.getString(key);
    }

    public byte[] readByteArray(String key) {
        return nbt.getByteArray(key);
    }

    public int[] readIntArray(String key) {
        return nbt.getIntArray(key);
    }

    public long[] readLongArray(String key) {
        return nbt.getLongArray(key);
    }

    public CompoundNBT readCompound(String key) {
        return nbt.getCompound(key);
    }

    public ListNBT readList(String key, int tagType) {
        return nbt.getList(key, tagType);
    }

    public boolean readBoolean(String key) {
        return nbt.getBoolean(key);
    }

    public boolean contains(String key) {
        return nbt.contains(key);
    }

}
