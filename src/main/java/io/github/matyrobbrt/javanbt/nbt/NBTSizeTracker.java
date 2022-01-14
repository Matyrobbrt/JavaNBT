package io.github.matyrobbrt.javanbt.nbt;

public class NBTSizeTracker {

	public static final NBTSizeTracker UNLIMITED = new NBTSizeTracker(0L) {

		@Override
		public void accountBits(long pBits) {
		}
	};
	private final long quota;
	private long usage;

	public NBTSizeTracker(long pQuota) {
		this.quota = pQuota;
	}

	/**
	 * Adds the bits to the current number of read bytes. If the number of bytes is
	 * greater than the stored quota, an exception will occur.
	 * 
	 * @throws RuntimeException if the number of {@code usage} bytes exceed the
	 *                          number of {@code quota} bytes
	 */
	public void accountBits(long bits) {
		this.usage += bits / 8L;
		if (this.usage > this.quota) {
			throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.usage
					+ "bytes where max allowed: " + this.quota);
		}
	}

	/*
	 * UTF8 is not a simple encoding system, each character can be either 1, 2, or 3
	 * bytes. Depending on where it's numerical value falls. We have to count up
	 * each character individually to see the true length of the data.
	 *
	 * Basic concept is that it uses the MSB of each byte as a 'read more' signal.
	 * So it has to shift each 7-bit segment.
	 *
	 * This will accurately count the correct byte length to encode this string,
	 * plus the 2 bytes for it's length prefix.
	 */
	public String readUTF(String data) {
		accountBits(16); // Header length
		if (data == null)
			return data;

		int len = data.length();
		int utflen = 0;

		for (int i = 0; i < len; i++) {
			int c = data.charAt(i);
			if ((c >= 0x0001) && (c <= 0x007F))
				utflen += 1;
			else if (c > 0x07FF)
				utflen += 3;
			else
				utflen += 2;
		}
		accountBits(8 * utflen);

		return data;
	}
}