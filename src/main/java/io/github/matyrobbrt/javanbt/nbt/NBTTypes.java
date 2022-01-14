package io.github.matyrobbrt.javanbt.nbt;

import java.util.ArrayList;
import java.util.List;

public class NBTTypes {

	private static final List<NBTType<?>> TAG_TYPES = new ArrayList<>();

	static {
		TAG_TYPES.add(EndNBT.TYPE); // Type ID 0
		TAG_TYPES.add(ByteNBT.TYPE); // Type ID 1
		TAG_TYPES.add(ShortNBT.TYPE); // Type ID 2
		TAG_TYPES.add(IntNBT.TYPE); // Type ID 3
		TAG_TYPES.add(LongNBT.TYPE); // Type ID 4
		TAG_TYPES.add(FloatNBT.TYPE); // Type ID 5
		TAG_TYPES.add(DoubleNBT.TYPE); // Type ID 6

		TAG_TYPES.add(ByteArrayNBT.TYPE); // Type ID 7
		TAG_TYPES.add(StringNBT.TYPE); // Type ID 8
		TAG_TYPES.add(ListNBT.TYPE); // Type ID 9
		TAG_TYPES.add(CompoundNBT.TYPE); // Type ID 10
		TAG_TYPES.add(IntArrayNBT.TYPE); // Type ID 11
		TAG_TYPES.add(LongArrayNBT.TYPE); // Type ID 12
	}

	public static NBTType<?> getType(int id) {
		return id >= 0 && id < TAG_TYPES.size() ? TAG_TYPES.get(id) : NBTType.createInvalidNBT(id);
	}

	public static int registerType(NBTType<?> nbtType) {
		TAG_TYPES.add(nbtType);
		return TAG_TYPES.size() - 1;
	}

}
