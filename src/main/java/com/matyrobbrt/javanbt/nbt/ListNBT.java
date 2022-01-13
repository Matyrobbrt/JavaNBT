package com.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListNBT extends CollectionNBT<NBT> {

	public static final NBTType<ListNBT> TYPE = new NBTType<ListNBT>() {

		@Override
		public ListNBT load(DataInput pInput, int pDepth, NBTSizeTracker pAccounter) throws IOException {
			pAccounter.accountBits(296L);
			if (pDepth > 512) {
				throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
			} else {
				byte b0 = pInput.readByte();
				int i = pInput.readInt();
				if (b0 == 0 && i > 0) {
					throw new RuntimeException("Missing type on ListTag");
				} else {
					pAccounter.accountBits(32L * i);
					NBTType<?> inbttype = NBTTypes.getType(b0);
					List<NBT> list = new ArrayList<>(i);

					for (int j = 0; j < i; ++j) {
						list.add(inbttype.load(pInput, pDepth + 1, pAccounter));
					}

					return new ListNBT(list, b0);
				}
			}
		}

		@Override
		public String getName() { return "LIST"; }

		@Override
		public String getPrettyName() { return "TAG_List"; }
	};

	private final List<NBT> data;
	private byte type;

	private ListNBT(List<NBT> dataList, byte type) {
		this.data = dataList;
		this.type = type;
	}

	public ListNBT() {
		this(new ArrayList<>(), (byte) 0);
	}

	@Override
	public void write(DataOutput output) throws IOException {
		if (this.data.isEmpty()) {
			this.type = 0;
		} else {
			this.type = this.data.get(0).getId();
		}
		output.writeByte(this.type);
		output.writeInt(this.data.size());
		for (NBT nbt : this.data) {
			nbt.write(output);
		}

	}

	@Override
	public byte getId() { return 9; }

	@Override
	public NBTType<ListNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < this.data.size(); ++i) {
			if (i != 0) {
				builder.append(',');
			}
			builder.append(this.data.get(i));
		}
		return builder.append(']').toString();
	}

	private void updateTypeAfterRemove() {
		if (this.data.isEmpty()) {
			this.type = 0;
		}
	}

	@Override
	public NBT remove(int index) {
		NBT nbt = this.data.remove(index);
		this.updateTypeAfterRemove();
		return nbt;
	}

	@Override
	public boolean isEmpty() { return this.data.isEmpty(); }

	public CompoundNBT getCompound(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 10) { return (CompoundNBT) inbt; }
		}

		return new CompoundNBT();
	}

	public ListNBT getList(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 9) { return (ListNBT) inbt; }
		}

		return new ListNBT();
	}

	public short getShort(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 2) { return ((ShortNBT) inbt).getAsShort(); }
		}

		return 0;
	}

	public int getInt(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 3) { return ((IntNBT) inbt).getAsInt(); }
		}

		return 0;
	}

	public int[] getIntArray(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 11) { return ((IntArrayNBT) inbt).getAsIntArray(); }
		}

		return new int[0];
	}

	public double getDouble(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 6) { return ((DoubleNBT) inbt).getAsDouble(); }
		}

		return 0.0D;
	}

	public float getFloat(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			if (inbt.getId() == 5) { return ((FloatNBT) inbt).getAsFloat(); }
		}

		return 0.0F;
	}

	public String getString(int index) {
		if (index >= 0 && index < this.data.size()) {
			NBT inbt = this.data.get(index);
			return inbt.getId() == 8 ? inbt.getAsString() : inbt.toString();
		} else {
			return "";
		}
	}

	@Override
	public int size() {
		return this.data.size();
	}

	@Override
	public NBT get(int index) {
		return this.data.get(index);
	}

	@Override
	public NBT set(int index, NBT nbt) {
		NBT inbt = this.get(index);
		if (!this.setNBT(index, nbt)) {
			throw new UnsupportedOperationException(
					String.format("Trying to add tag of type %d to list of %d", nbt.getId(), this.type));
		} else {
			return inbt;
		}
	}

	@Override
	public void add(int index, NBT nbt) {
		if (!this.addNBT(index, nbt)) {
			throw new UnsupportedOperationException(
					String.format("Trying to add tag of type %d to list of %d", nbt.getId(), this.type));
		}
	}

	@Override
	public boolean setNBT(int index, NBT nbt) {
		if (this.updateType(nbt)) {
			this.data.set(index, nbt);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addNBT(int index, NBT nbt) {
		if (this.updateType(nbt)) {
			this.data.add(index, nbt);
			return true;
		} else {
			return false;
		}
	}

	private boolean updateType(NBT nbt) {
		if (nbt.getId() == 0) {
			return false;
		} else if (this.type == 0) {
			this.type = nbt.getId();
			return true;
		} else {
			return this.type == nbt.getId();
		}
	}

	@Override
	public ListNBT copy() {
		ListNBT list = new ListNBT();
		list.type = this.type;
		forEach(data -> list.add(data.copy()));
		return list;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ListNBT nbtList && Objects.equals(this.data, nbtList.data);
		}
	}

	@Override
	public int hashCode() {
		return this.data.hashCode();
	}

	@Override
	public byte getElementType() { return this.type; }

	@Override
	public void clear() {
		this.data.clear();
		this.type = 0;
	}

}
