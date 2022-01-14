package io.github.matyrobbrt.javanbt.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import io.github.matyrobbrt.javanbt.nbt.CompoundNBT;
import io.github.matyrobbrt.javanbt.nbt.EndNBT;
import io.github.matyrobbrt.javanbt.nbt.NBT;
import io.github.matyrobbrt.javanbt.nbt.NBTSizeTracker;
import io.github.matyrobbrt.javanbt.nbt.NBTTypes;
import jakarta.annotation.Nullable;

public class NBTTools {

	public static CompoundNBT readCompressed(File file) throws IOException {
		CompoundNBT compoundNBT;
		try (InputStream is = new FileInputStream(file)) {
			compoundNBT = readCompressed(is);
		}

		return compoundNBT;
	}

	/**
	 * Reads a compressed compound tag from a GNU zipped file.
	 * 
	 * @see #readCompressed(File)
	 */
	public static CompoundNBT readCompressed(InputStream zippedStream) throws IOException {
		CompoundNBT compoundNBT;
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(zippedStream)))) {
			compoundNBT = read(dis, NBTSizeTracker.UNLIMITED);
		}

		return compoundNBT;
	}

	public static void writeCompressed(CompoundNBT compoundNBT, File file) throws IOException {
		try (OutputStream os = new FileOutputStream(file)) {
			writeCompressed(compoundNBT, os);
		}

	}

	/**
	 * Writes and compresses a compound tag to a GNU zipped file.
	 * 
	 * @see #writeCompressed(CompoundTag, File)
	 */
	public static void writeCompressed(CompoundNBT compoundNBT, OutputStream outputStream) throws IOException {
		try (DataOutputStream dos = new DataOutputStream(
				new BufferedOutputStream(new GZIPOutputStream(outputStream)))) {
			write(compoundNBT, dos);
		}

	}

	public static void write(CompoundNBT compoundNBT, File file) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(file); DataOutputStream dos = new DataOutputStream(fos);) {
			write(compoundNBT, dos);
		}

	}

	@Nullable
	public static CompoundNBT read(File file) throws IOException {
		if (!file.exists()) {
			return null;
		} else {
			CompoundNBT compoundNBT;
			try (FileInputStream fis = new FileInputStream(file); DataInputStream dis = new DataInputStream(fis);) {
				compoundNBT = read(dis, NBTSizeTracker.UNLIMITED);
			}

			return compoundNBT;
		}
	}

	/**
	 * Reads a compound tag from a file. The size of the file can be infinite.
	 */
	public static CompoundNBT read(DataInput input) throws IOException {
		return read(input, NBTSizeTracker.UNLIMITED);
	}

	/**
	 * Reads a compound tag from a file. The size of the file is limited by the
	 * {@code accounter}.
	 * 
	 * @throws RuntimeException if the size of the file is larger than the maximum
	 *                          amount of bytes specified by the {@code accounter}
	 */
	public static CompoundNBT read(DataInput input, NBTSizeTracker sizeAccounter) throws IOException {
		NBT nbt = readUnnamedNBT(input, 0, sizeAccounter);
		if (nbt instanceof CompoundNBT cnbt) {
			return cnbt;
		} else {
			throw new IOException("Root tag cannot be a named compound tag");
		}
	}

	public static void write(CompoundNBT nbtCompound, DataOutput output) throws IOException {
		writeUnnamedNBT(nbtCompound, output);
	}

	private static void writeUnnamedNBT(NBT nbt, DataOutput outpur) throws IOException {
		outpur.writeByte(nbt.getId());
		if (nbt.getId() != 0) {
			outpur.writeUTF("");
			nbt.write(outpur);
		}
	}

	private static NBT readUnnamedNBT(DataInput input, int depth, NBTSizeTracker sizeAccounter) throws IOException {
		byte b0 = input.readByte();
		sizeAccounter.accountBits(8);
		if (b0 == 0) {
			return EndNBT.INSTANCE;
		} else {
			sizeAccounter.readUTF(input.readUTF());
			sizeAccounter.accountBits(32);

			try {
				return NBTTypes.getType(b0).load(input, depth, sizeAccounter);
			} catch (IOException e) {
				throw new NBTIOException(e);
			}
		}
	}

	public static class NBTIOException extends RuntimeException {

		private static final long serialVersionUID = 5965375596453052279L;

		public NBTIOException(final Throwable e) {
			super(e);
		}

	}

}
