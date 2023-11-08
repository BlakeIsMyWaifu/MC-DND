package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Stats.Character;
import dev.blakeismywaifu.mcdnd.Stats.Miscellaneous;
import dev.blakeismywaifu.mcdnd.Stats.Skills;
import dev.blakeismywaifu.mcdnd.Stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CharacterData {

	public final UUID playerId;
	public final String characterId;
	public final Player player;
	private JSONObject json;

	public CharacterData(UUID playerId, String id) {
		this.playerId = playerId;
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		this.json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

	}

	public void updateItems() {
		this.player.getInventory().setItem(9, new Character(this.json).getItem());
		this.player.getInventory().setItem(10, new Miscellaneous(this.json).getItem());
		this.player.getInventory().setItem(11, new Skills(this.json).getItem());
		int statIndex = 12;
		for (ItemStack item : new Stats(this.json).getItems()) {
			this.player.getInventory().setItem(statIndex, item);
			statIndex++;
		}
	}
}
