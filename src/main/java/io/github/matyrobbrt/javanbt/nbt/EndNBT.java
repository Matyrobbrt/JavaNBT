package io.github.matyrobbrt.javanbt.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EndNBT implements NBT {

	public static final NBTType<EndNBT> TYPE = new NBTType<EndNBT>() {

		@Override
		public EndNBT load(DataInput pInput, int pDepth, NBTSizeTracker accounter) {
			accounter.accountBits(64L);
			return EndNBT.INSTANCE;
		}

		@Override
		public String getName() { return "END"; }

		@Override
		public String getPrettyName() { return "TAG_End"; }

		@Override
		public boolean isValue() { return true; }
	};
	public static final EndNBT INSTANCE = new EndNBT();

	private EndNBT() {
	}

	@Override
	public void write(DataOutput pOutput) throws IOException {
	}

	@Override
	public byte getId() { return 0; }

	@Override
	public NBTType<EndNBT> getType() { return TYPE; }

	@Override
	public String toString() {
		return "END";
	}

	@Override
	public EndNBT copy() {
		return this;
	}

}
