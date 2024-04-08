package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.BookViewer;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Inventory;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Utils.Fetch;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.util.UUID;

public class CharacterSheet {

	public final String characterId;
	public final Player player;

	public CharacterInfo characterInfo;
	public Conditions conditions;
	public Defences defences;
	public HitPoints hitPoints;
	public Miscellaneous miscellaneous;
	public Proficiencies proficiencies;
	public SavingThrows savingThrows;
	public Senses senses;
	public Skills skills;
	public Stats stats;

	public Inventory inventory;
	public BookViewer bookViewer;

	public CharacterSheet(UUID playerId, String id) {
		this.characterId = id;
		this.player = Bukkit.getPlayer(playerId);
	}

	public void updateData() {
		JSONObject json = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.characterId).getData();

		Modifiers modifiers = new Modifiers(json);

		// TODO load after and update data accordingly
		this.inventory = new Inventory(json);

		this.characterInfo = new CharacterInfo(json);

		this.stats = new Stats(json);
		modifiers.getModifiers(Modifiers.Category.STATS).forEach(modifier -> this.stats.updateData(modifier));

		this.hitPoints = new HitPoints(json, this.stats, this.characterInfo);
		modifiers.getModifiers(Modifiers.Category.HITPOINTS).forEach(modifier -> this.hitPoints.updateData(modifier, characterInfo));

		this.miscellaneous = new Miscellaneous(json, this.stats, this.characterInfo);
		modifiers.getModifiers(Modifiers.Category.MISCELLANEOUS).forEach(modifier -> this.miscellaneous.updateData(modifier));

		this.proficiencies = new Proficiencies(this.inventory);
		modifiers.getModifiers(Modifiers.Category.PROFICIENCIES).forEach(modifier -> this.proficiencies.updateData(modifier));

		this.skills = new Skills(stats, this.inventory, this.proficiencies);
		modifiers.getModifiers(Modifiers.Category.SKILLS).forEach(modifier -> this.skills.updateData(modifier, this.stats, this.miscellaneous));

		this.defences = new Defences();
		modifiers.getModifiers(Modifiers.Category.DEFENCES).forEach(modifier -> this.defences.updateData(modifier));

		this.conditions = new Conditions(json);

		this.savingThrows = new SavingThrows(this.stats, this.proficiencies);
		modifiers.getModifiers(Modifiers.Category.SAVING_THROW).forEach(modifier -> this.savingThrows.updateDate(modifier, miscellaneous));

		this.senses = new Senses(this.skills);
		modifiers.getModifiers(Modifiers.Category.SENSES).forEach(modifier -> this.senses.updateData(modifier, this.inventory));

		this.bookViewer = new BookViewer();
	}

	public void updateItems() {
		this.player.getInventory().setItem(9, this.characterInfo.getItem());
		this.player.getInventory().setItem(10, this.miscellaneous.getItem());
		this.player.getInventory().setItem(11, this.skills.getItem());
		this.player.getInventory().setItem(18, this.defences.getItem());
		this.player.getInventory().setItem(19, this.conditions.getItem());
		this.player.getInventory().setItem(20, this.savingThrows.getItem());
		this.player.getInventory().setItem(27, this.senses.getItem());
		this.player.getInventory().setItem(28, this.proficiencies.getItem(Proficiencies.Type.ARMOUR));
		this.player.getInventory().setItem(29, this.proficiencies.getItem(Proficiencies.Type.WEAPON));
		this.player.getInventory().setItem(30, this.proficiencies.getItem(Proficiencies.Type.TOOL));
		this.player.getInventory().setItem(31, this.proficiencies.getItem(Proficiencies.Type.LANGUAGE));

		int statIndex = 0;
		for (Stats.Stat.StatName statName : Stats.Stat.StatName.values()) {
			this.player.getInventory().setItem(statIndex + 12, this.stats.getItem(statName));
			this.player.getInventory().setItem(statIndex + 21, this.savingThrows.getItem(statName));
			statIndex++;
		}

		this.player.getInventory().setItem(32, this.bookViewer.getInventoryItem(BookViewer.BookType.ACTIONS));
		this.player.getInventory().setItem(33, this.bookViewer.getInventoryItem(BookViewer.BookType.SPELLS));
		this.player.getInventory().setItem(34, this.bookViewer.getInventoryItem(BookViewer.BookType.ITEMS));
		this.player.getInventory().setItem(35, this.bookViewer.getInventoryItem(BookViewer.BookType.FEATS));
	}
}
