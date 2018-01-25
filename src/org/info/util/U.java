package org.info.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class U {

	public static String trimStr(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

	public static byte[] toBa8(String str) {
		return str.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * Convert to String UT8
	 *
	 * @param ba
	 * @return
	 */
	public static String toStr8(byte[] ba) {
		return new String(ba, StandardCharsets.UTF_8);
	}

	public static boolean containsLetter(String str) {
		String input = str.toLowerCase();// Make your input toLowerCase.

		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			int value = ch;
			if (value >= 97 && value <= 122) {
				return true;
			}
		}
		return false;
	}

	public static List makeSyncedList() {
		return Collections.synchronizedList(new ArrayList());
	}// ()

}// class
