package io.github.matyrobbrt.javanbt.converter;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;

/**
 * Class holding utility methods for converting to and from NBT.
 * 
 * @author matyrobbrt
 *
 */
public class NBTConverters {

    /**
     * Converts a string to a {@link CompoundNBT}.
     * 
     * @param  str the string to convert
     * @return     the compound
     */
    public static CompoundNBT stringToNBT(String str) {
        return StringToNBTConverter.parseNBT(str);
    }

    /**
     * Converts a {@link NBT} element to a string.
     * 
     * @param  nbt the {@link NBT} to convert
     * @return     the string representation of the NBT.
     */
    public static String nbtToString(NBT nbt) {
        return nbt.getAsString();
    }

}
