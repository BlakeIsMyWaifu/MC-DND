package dev.blakeismywaifu.mcdnd.Utils;

public class Utils {
	public static boolean isInteger(Object str) {
		try {
			Integer.parseInt((String) str);
		} catch (NumberFormatException | NullPointerException | ClassCastException err) {
			return false;
		}
		return true;
	}
}
