package io.github.matyrobbrt.javanbt.converter;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;

public class NBTConverters {

	public static CompoundNBT stringToNBT(String str) {
		return StringToNBTConverter.parseNBT(str);
	}

	public static String nbtToString(NBT nbt) {
		return nbt.getAsString();
	}

}
