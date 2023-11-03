package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Stats.Character;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class PlayerItems {
	public static void updateItems(Player player) {
		JSONObject json = PlayerCache.getCache().get(player.getUniqueId());

		player.getInventory().setItem(9, Character.item(json));
	}
}
