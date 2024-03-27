package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Stats.Character;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Stats.Miscellaneous;
import dev.blakeismywaifu.mcdnd.Stats.Skills;
import dev.blakeismywaifu.mcdnd.Stats.Stats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CharacterData {

	public final String characterId;
	public final Player player;

	public Character character;
	public Miscellaneous miscellaneous;
	public Skills skills;
	public Stats stats;

	public CharacterData(UUID playerId, String id) {
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		JSONObject json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

		this.character = new Character(json);
		this.skills = new Skills(json);
		this.stats = new Stats(json);
		// ! miscellaneous must be loaded after stats
		this.miscellaneous = new Miscellaneous(json, this);

		readModifiers((JSONObject) json.get("modifiers"));
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

	private void readModifiers(JSONObject json) {
		String[] modifierTypes = {"race", "class", "background", "item", "feat"};
		for (String modifierType : modifierTypes) {
			JSONArray modifiers = (JSONArray) json.get(modifierType);
			for (Object modifierJson : modifiers) {
				Modifier modifier = new Modifier((JSONObject) modifierJson);
				modifier.useModifier(this);
			}
		}
	}
}
