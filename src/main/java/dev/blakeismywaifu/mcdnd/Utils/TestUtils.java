package dev.blakeismywaifu.mcdnd.Utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class TestUtils {

	public static boolean isInteger(Object str) {
		try {
			Integer.parseInt((String) str);
		} catch (NumberFormatException | NullPointerException | ClassCastException err) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean isDndPlayer(Player player) {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (player.getGameMode() != GameMode.ADVENTURE) return false;
		//noinspection RedundantIfStatement
		if (!player.hasPermission("dnd.player")) return false;
		return true;
	}
}
