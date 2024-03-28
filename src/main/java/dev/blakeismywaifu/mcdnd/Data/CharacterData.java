package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Stats.Character;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Stats.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CharacterData {

	public final String characterId;
	public final Player player;

	public Character character;
	public HitPoints hitPoints;
	public Miscellaneous miscellaneous;
	public Skills skills;
	public Stats stats;

	public CharacterData(UUID playerId, String id) {
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		JSONObject json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

		Modifiers modifiers = new Modifiers(json);

		this.character = new Character(json);

		this.stats = new Stats(json);
		modifiers.updateData(Modifiers.ModifierCategory.STATS, this);

		this.skills = new Skills(json);
		modifiers.updateData(Modifiers.ModifierCategory.SKILLS, this);

		this.hitPoints = new HitPoints(json, this.stats, this.character);

		this.miscellaneous = new Miscellaneous(json, this.stats);
		modifiers.updateData(Modifiers.ModifierCategory.MISCELLANEOUS, this);
	}

	public void updateItems() {
		this.player.getInventory().setItem(9, this.character.getItem());
		this.player.getInventory().setItem(10, this.miscellaneous.getItem());
		this.player.getInventory().setItem(11, this.skills.getItem());
		int statIndex = 12;
		for (ItemStack item : this.stats.getItems()) {
			this.player.getInventory().setItem(statIndex, item);
			statIndex++;
		}
	}
}
