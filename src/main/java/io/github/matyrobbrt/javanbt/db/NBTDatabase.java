package io.github.matyrobbrt.javanbt.db;

import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import io.github.matyrobbrt.javanbt.io.NBTTools;
import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.serialization.NBTSerializable;

public abstract class NBTDatabase implements NBTSerializable<CompoundNBT> {

	private final File file;
	private boolean dirty;

	protected NBTDatabase(final File file, final long saveInterval) {
		this.file = file;

		if (saveInterval != 0) {
			NBTDatabaseManager.TIMER.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					NBTDatabase.this.saveToDisk();
				}
			}, 0, saveInterval);
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		return save(new CompoundNBT());
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		load(nbt);
	}

	public void saveToDisk() {
		if (isDirty()) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.put("data", save(new CompoundNBT()));
			try {
				NBTTools.writeCompressed(nbt, file);
			} catch (IOException ioexception) {
				System.err.println(String.format("Error while trying to save nbt database %s: %s", this, ioexception));
			}
			setDirty(false);
		}
	}

	public abstract void load(CompoundNBT nbt);

	public abstract CompoundNBT save(CompoundNBT nbt);

	public File getFile() { return file; }

	public boolean isDirty() { return this.dirty; }

	public void setDirty() {
		this.setDirty(true);
	}

	public void setDirty(boolean pDirty) { this.dirty = pDirty; }

}
