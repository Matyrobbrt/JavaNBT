package io.github.matyrobbrt.javanbt.db;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import io.github.matyrobbrt.javanbt.io.NBTTools;
import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import jakarta.annotation.Nullable;

// TODO proper logging
@SuppressWarnings("static-method")
public class NBTDatabaseManager {

	static final Timer TIMER = new Timer();

	public static final NBTDatabaseManager DEFAULT = new NBTDatabaseManager(15 * 60 * 1000L);

	public NBTDatabaseManager(final long saveInterval) {
		if (saveInterval != 0) {
			NBTDatabaseManager.TIMER.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					NBTDatabaseManager.this.save();
				}
			}, 0, saveInterval);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(this::save, "Saving db manager"));
	}

	private final Map<File, NBTDatabase> databases = Collections.synchronizedMap(new HashMap<>());

	public <T extends NBTDatabase> T computeIfAbsent(Function<File, T> creator, File storageFile) {
		T db = this.get(creator, storageFile);
		if (db != null) {
			return db;
		} else {
			T db1 = creator.apply(storageFile);
			this.set(db1);
			return db1;
		}
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends NBTDatabase> T get(Function<File, T> creator, File file) {
		NBTDatabase db = this.databases.get(file);
		if (db == null && !this.databases.containsKey(file)) {
			db = this.readData(creator, file);
			this.databases.put(file, db);
		} else if (db == null) { return null; }
		return (T) db;
	}

	@Nullable
	protected <T extends NBTDatabase> T readData(Function<File, T> creator, File file) {
		try {
			if (file.exists()) {
				T db = creator.apply(file);
				CompoundNBT nbt = readTagFromDisk(file);
				db.load(nbt.getCompound("data"));
				return db;
			}
		} catch (Exception exception) {
			System.err.println("Error loading nbt database " + file + ": " + exception);
		}
		return null;
	}

	public void set(NBTDatabase db) {
		this.databases.put(db.getFile(), db);
	}

	protected CompoundNBT readTagFromDisk(File file) throws IOException {
		try (FileInputStream fis = new FileInputStream(file);
				PushbackInputStream pbis = new PushbackInputStream(fis, 2);) {
			CompoundNBT nbt;
			if (NBTDatabaseManager.isGzip(pbis)) {
				nbt = NBTTools.readCompressed(pbis);
			} else {
				try (DataInputStream dis = new DataInputStream(pbis)) {
					nbt = NBTTools.read(dis);
				}
			}
			return nbt;
		}
	}

	protected static boolean isGzip(PushbackInputStream inputStream) throws IOException {
		byte[] abyte = new byte[2];
		boolean flag = false;
		int i = inputStream.read(abyte, 0, 2);
		if (i == 2) {
			int j = (abyte[1] & 255) << 8 | abyte[0] & 255;
			if (j == 35615) {
				flag = true;
			}
		}
		if (i != 0) {
			inputStream.unread(abyte, 0, i);
		}
		return flag;
	}

	public void save() {
		for (NBTDatabase db : this.databases.values()) {
			if (db != null) {
				db.saveToDisk();
			}
		}

	}
}
