package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class FullUpdateAll extends BukkitRunnable {

	private final Main main;

	public FullUpdateAll(Main main) {
		this.main = main;
	}

	@Override
	public void run() {
		List<UUID> playerIds = this.main.playerCache.listPlayers();
		playerIds.forEach(playerId -> new UpdatePlayer(this.main.playerCache.getPlayer(playerId)).runTaskAsynchronously(this.main));
	}
}