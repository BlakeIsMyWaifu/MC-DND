package dev.blakeismywaifu.mcdnd.Utils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class TestPlayer {

	public boolean isPlayer = false;

	public TestPlayer(Player player) {
		if (player == null) return;
		if (!player.isOnline()) return;
		if (player.getGameMode() != GameMode.ADVENTURE) return;
		if (!player.hasPermission("dnd.player")) return;
		this.isPlayer = true;
	}
}
