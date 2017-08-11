package me.ckhd.opengame.online.util.gionee;

import java.util.UUID;

public class CamelUtility {
	public static final int SizeOfUUID = 16;
	private static final int SizeOfLong = 8;
	private static final int BitsOfByte = 8;
	private static final int MBLShift = (SizeOfLong - 1) * BitsOfByte;

	private static final char[] HEX_CHAR_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String uuidToString(UUID uuid) {
		long[] ll = {uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()};
		StringBuilder str = new StringBuilder(SizeOfUUID * 2);
		for (int m = 0; m < ll.length; ++m) {
			for (int i = MBLShift; i > 0; i -= BitsOfByte)
				formatAsHex((byte) (ll[m] >>> i), str);
			formatAsHex((byte) (ll[m]), str);
		}
		return str.toString();
	}

	public static void formatAsHex(byte b, StringBuilder s) {
		s.append(HEX_CHAR_TABLE[(b >>> 4) & 0x0F]);
		s.append(HEX_CHAR_TABLE[b & 0x0F]);
	}
}
