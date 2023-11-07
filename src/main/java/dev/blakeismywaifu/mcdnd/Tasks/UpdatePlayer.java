package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdatePlayer extends BukkitRunnable {

	private final CharacterData characterData;

	public UpdatePlayer(CharacterData characterData) {
		this.characterData = characterData;
	}

	@Override
	public void run() {
		Player player = this.characterData.player;
		if (player == null) return;
		if (!player.isOnline()) return;
		if (player.getGameMode() != GameMode.ADVENTURE) return;
		if (!player.hasPermission("dnd.player")) return;

		this.characterData.updateData();
		this.characterData.updateItems();
	}
}
