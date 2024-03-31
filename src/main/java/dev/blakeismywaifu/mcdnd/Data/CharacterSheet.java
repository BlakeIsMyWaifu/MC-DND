package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Utils.Fetch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CharacterSheet {

	public final String characterId;
	public final Player player;

	public CharacterInfo characterInfo;
	public HitPoints hitPoints;
	public Miscellaneous miscellaneous;
	public Skills skills;
	public Stats stats;

	public CharacterSheet(UUID playerId, String id) {
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		JSONObject json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

		Modifiers modifiers = new Modifiers(json);

		this.characterInfo = new CharacterInfo(json);

		this.stats = new Stats(json);
		modifiers.getModifiers(Modifiers.ModifierCategory.STATS).forEach(modifier -> stats.updateData(modifier));

		this.hitPoints = new HitPoints(json, stats, characterInfo);
		modifiers.getModifiers(Modifiers.ModifierCategory.HITPOINTS).forEach(modifier -> hitPoints.updateData(modifier, characterInfo));

		this.miscellaneous = new Miscellaneous(json, stats, characterInfo);
		modifiers.getModifiers(Modifiers.ModifierCategory.MISCELLANEOUS).forEach(modifier -> miscellaneous.updateData(modifier));

		this.skills = new Skills(stats);
		modifiers.getModifiers(Modifiers.ModifierCategory.SKILLS).forEach(modifier -> skills.updateData(modifier, stats, miscellaneous));
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
	}
}
