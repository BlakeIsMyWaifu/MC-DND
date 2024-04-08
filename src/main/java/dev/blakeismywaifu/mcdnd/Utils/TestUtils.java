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

	public static boolean isNotDNDPlayer(Player player) {
		if (player == null) return true;
		if (!player.isOnline()) return true;
		if (player.getGameMode() != GameMode.ADVENTURE) return true;
		//noinspection RedundantIfStatement
		if (!player.hasPermission("dnd.player")) return true;
		return false;
	}
}
