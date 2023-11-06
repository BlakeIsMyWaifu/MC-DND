package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Stats.Character;
import dev.blakeismywaifu.mcdnd.Stats.Miscellaneous;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class PlayerItems {

	private final Player player;

	public PlayerItems(Player player) {
		this.player = player;
	}

	public void updateItems() {
		JSONObject json = PlayerCache.getCache().get(this.player.getUniqueId());

		this.player.getInventory().setItem(9, new Character(json).getItem());
		this.player.getInventory().setItem(10, new Miscellaneous(json).getItem());
	}
}
