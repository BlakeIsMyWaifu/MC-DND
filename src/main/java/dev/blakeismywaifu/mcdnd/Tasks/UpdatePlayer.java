package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.Data.CharacterSheet;
import dev.blakeismywaifu.mcdnd.Utils.TestUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdatePlayer extends BukkitRunnable {

	private final CharacterSheet characterSheet;

	public UpdatePlayer(CharacterSheet characterSheet) {
		this.characterSheet = characterSheet;
	}

	@Override
	public void run() {
		Player player = this.characterSheet.player;
		if (!TestUtils.isDndPlayer(player)) return;

		this.characterSheet.updateData();
		this.characterSheet.updateItems();
	}
}
