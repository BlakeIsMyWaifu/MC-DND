package dev.blakeismywaifu.mcdnd.Tasks;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Main;
import dev.blakeismywaifu.mcdnd.Utils.TestPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class QuickUpdateAll extends BukkitRunnable {

	private final Main main;

	public QuickUpdateAll(Main main) {
		this.main = main;
	}

	@Override
	public void run() {
		List<UUID> playerIds = this.main.playerCache.listPlayers();
		playerIds.forEach(playerId -> {
			Player player = Bukkit.getPlayer(playerId);
			if (!new TestPlayer(player).isPlayer) return;
			assert player != null;

			CharacterData characterData = this.main.playerCache.getPlayer(playerId);

			player.setSaturation(20f);
			player.setFoodLevel(20);

			if (characterData.hitPoints.maxHitPoints == null) return;
			player.sendActionBar(Component.text("Health: " + characterData.hitPoints.currentHitPoints + " / " + characterData.hitPoints.maxHitPoints, NamedTextColor.RED));
		});
	}
}
