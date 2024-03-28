package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Utils.TestPlayer;
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
		if (!new TestPlayer(player).isPlayer) return;

		this.characterData.updateData();
		this.characterData.updateItems();
	}
}
