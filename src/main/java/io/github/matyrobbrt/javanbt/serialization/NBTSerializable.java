package io.github.matyrobbrt.javanbt.serialization;

import io.github.matyrobbrt.javanbt.nbt.NBT;

/**
 * An interface for objects that can be serialized to and from NBT.
 * 
 * @author     matyrobbrt
 *
 * @param  <N> the type of the NBT that this object can be serialized to
 */
public interface NBTSerializable<N extends NBT> {

    /**
     * Serializes this object to NBT.
     * 
     * @return the serialized object
     */
    N serializeNBT();

    /**
     * Deserializes this object from NBT.
     * 
     * @param nbt the nbt to deserialize from
     */
    void deserializeNBT(N nbt);

}
