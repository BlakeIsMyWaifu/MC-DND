package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Inventory;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Utils.Fetch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.UUID;

public class CharacterSheet {

	public final String characterId;
	public final Player player;

	public CharacterInfo characterInfo;
	public HitPoints hitPoints;
	public Miscellaneous miscellaneous;
	public Proficiencies proficiencies;
	public Skills skills;
	public Stats stats;

	public Inventory inventory;

	public CharacterSheet(UUID playerId, String id) {
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		JSONObject json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

		Modifiers modifiers = new Modifiers(json);
		this.inventory = new Inventory(json);

		this.characterInfo = new CharacterInfo(json);

		this.stats = new Stats(json);
		modifiers.getModifiers(Modifiers.ModifierCategory.STATS).forEach(modifier -> this.stats.updateData(modifier));

		this.hitPoints = new HitPoints(json, this.stats, this.characterInfo);
		modifiers.getModifiers(Modifiers.ModifierCategory.HITPOINTS).forEach(modifier -> this.hitPoints.updateData(modifier, characterInfo));

		this.miscellaneous = new Miscellaneous(json, this.stats, this.characterInfo);
		modifiers.getModifiers(Modifiers.ModifierCategory.MISCELLANEOUS).forEach(modifier -> this.miscellaneous.updateData(modifier));

		this.proficiencies = new Proficiencies();
		modifiers.getModifiers(Modifiers.ModifierCategory.PROFICIENCIES).forEach(modifier -> this.proficiencies.updateData(modifier));

		this.skills = new Skills(stats, this.inventory, this.proficiencies);
		modifiers.getModifiers(Modifiers.ModifierCategory.SKILLS).forEach(modifier -> this.skills.updateData(modifier, this.stats, this.miscellaneous));
	}

	public void updateItems() {
		this.player.getInventory().setItem(9, this.characterInfo.getItem());
		this.player.getInventory().setItem(10, this.miscellaneous.getItem());
		this.player.getInventory().setItem(11, this.skills.getItem());
		int statIndex = 12;
		for (ItemStack item : this.stats.getItems()) {
			this.player.getInventory().setItem(statIndex, item);
			statIndex++;
		}
		this.player.getInventory().setItem(28, this.proficiencies.getItem(Proficiencies.Type.ARMOUR));
		this.player.getInventory().setItem(29, this.proficiencies.getItem(Proficiencies.Type.WEAPON));
		this.player.getInventory().setItem(30, this.proficiencies.getItem(Proficiencies.Type.TOOL));
		this.player.getInventory().setItem(31, this.proficiencies.getItem(Proficiencies.Type.LANGUAGE));
	}
}
