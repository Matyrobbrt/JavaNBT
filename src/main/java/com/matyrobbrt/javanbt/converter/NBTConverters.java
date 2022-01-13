package com.matyrobbrt.javanbt.converter;

import com.matyrobbrt.javanbt.nbt.CompoundNBT;
import com.matyrobbrt.javanbt.nbt.NBT;

public class NBTConverters {

	public static CompoundNBT stringToNBT(String str) {
		return StringToNBTConverter.parseNBT(str);
	}

	public static String nbtToString(NBT nbt) {
		return nbt.getAsString();
	}

}
