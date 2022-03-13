package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataOutput;
import java.io.IOException;

import com.google.gson.JsonElement;

public interface NBT {

    /**
     * Write the actual data contents of the tag, implemented in NBT extension
     * classes
     */
    void write(DataOutput output) throws IOException;

    @Override
    String toString();

    /**
     * Gets the type byte for the tag.
     */
    byte getId();

    NBTType<?> getType();

    /**
     * Creates a clone of the tag.
     */
    NBT copy();

    default String getAsString() {
        return this.toString();
    }

    JsonElement toJson();

}
