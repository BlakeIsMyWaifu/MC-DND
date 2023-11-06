package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.API.CharacterData;
import dev.blakeismywaifu.mcdnd.Data.PlayerCache;
import dev.blakeismywaifu.mcdnd.Data.PlayerItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class FullUpdate extends BukkitRunnable {

	@Override
	public void run() {
		Map<UUID, String> players = PlayerCache.getBinds();
		for (Map.Entry<UUID, String> entry : players.entrySet()) {
			Player player = Bukkit.getPlayer(entry.getKey());
			if (player == null || !player.isOnline() || player.getGameMode() != GameMode.ADVENTURE || !player.hasPermission("dnd.player"))
				return;

			new CharacterData(player.getUniqueId(), entry.getValue()).getAndCacheData();
			new PlayerItems(player).updateItems();
		}
	}
}