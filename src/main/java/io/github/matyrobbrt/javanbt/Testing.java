package io.github.matyrobbrt.javanbt;

import java.io.File;
import java.nio.file.Paths;

import io.github.matyrobbrt.javanbt.annotation.NBTExposed;
import io.github.matyrobbrt.javanbt.db.NBTDatabase;
import io.github.matyrobbrt.javanbt.db.NBTDatabaseManager;
import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.serialization.manager.NBTManager;

class Testing {

	private static final NBTManager MANAGER = new NBTManager(false);

	public static void main(String[] args) {
		final var db = NBTDatabaseManager.DEFAULT.computeIfAbsent(TestDB::new, Paths.get("test.dat").toFile());

		final var data = new DataStructure();
		data.no = "yeslkjdlsad";
		data.yes = -12801;

		db.putData("test", MANAGER.toNBT(data));

		db.setDirtyAndSave();
	}

	static final class TestDB extends NBTDatabase {

		private final CompoundNBT nbt = new CompoundNBT();

		private TestDB(File file) {
			super(file, 0);
		}

		@Override
		public void load(CompoundNBT nbt) {
		}

		public void putData(final String key, final NBT nbt) {
			this.nbt.put(key, nbt);
		}

		@Override
		public CompoundNBT save(CompoundNBT nbt) {
			return this.nbt;
		}

		public void setDirtyAndSave() {
			setDirty();
			saveToDisk();
		}

	}

	static final class DataStructure {

		public int yes = 120;

		@NBTExposed(serializationName = "someString")
		public String no = "askajsl";

	}

}
