package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Stats {

	private final Map<Stat.StatName, Stat> stats = new LinkedHashMap<>();

	public Stats(JSONObject json) {
		for (Stat.StatName stat : Stat.StatName.values()) {
			this.stats.put(stat, new Stat(stat, json));
		}
	}

	public Stat getStat(Stat.StatName statName) {
		return stats.get(statName);
	}

	public ItemStack getItem(Stat.StatName statName) {
		return this.stats.get(statName).getItem();
	}

	public void updateData(Modifier modifier) {
		Stat.StatName stat = switch (modifier.subType) {
			case "strength-score" -> Stat.StatName.STRENGTH;
			case "dexterity-score" -> Stat.StatName.DEXTERITY;
			case "constitution-score" -> Stat.StatName.CONSTITUTION;
			case "intelligence-score" -> Stat.StatName.INTELLIGENCE;
			case "wisdom-score" -> Stat.StatName.WISDOM;
			case "charisma-score" -> Stat.StatName.CHARISMA;
			default -> null;
		};

		if (modifier.value == null) {
			Console.warn("Modifier " + modifier.subType + " has a null value");
			return;
		}
		this.stats.get(stat).addToTotal(modifier.value);
	}

	public static class Stat {

		public final StatName statName;
		public final Integer base;
		public final Integer bonus;
		public final Integer override;
		public Integer modifier;
		public Integer total = 0;

		public Stat(StatName statName, JSONObject json) {
			this.statName = statName;
			this.base = getStatValue(json, "stats");
			// ? no idea what bonus is
			this.bonus = getStatValue(json, "bonusStats");
			this.override = getStatValue(json, "overrideStats");

			addToTotal(this.base);
		}

		public void addToTotal(Integer value) {
			this.total += value;
			if (this.override != 0) {
				this.total = this.override;
			}
			this.modifier = (this.total - 10) / 2;
		}

		public ItemStack getItem() {
			ItemBuilder item = new ItemBuilder("Modifier +" + this.modifier);
			item.lore("Total " + this.total);
			item.lore(this.statName.friendlyName);
			return item.build();
		}

		private int getStatValue(JSONObject json, String key) {
			JSONArray statsArray = json.getJSONArray(key);
			JSONObject statObject = statsArray.getJSONObject(this.statName.index);
			return statObject.isNull("value") ? 0 : statObject.getInt("value");
		}

		public enum StatName {
			STRENGTH(0, "STR", "Strength"),
			DEXTERITY(1, "DEX", "Dexterity"),
			CONSTITUTION(2, "CON", "Constitution"),
			INTELLIGENCE(3, "INT", "Intelligence"),
			WISDOM(4, "WIS", "Wisdom"),
			CHARISMA(5, "CHA", "Charisma");

			public final String shortHand;
			public final Integer index;
			public final String friendlyName;

			StatName(Integer index, String shortHand, String friendlyName) {
				this.index = index;
				this.shortHand = shortHand;
				this.friendlyName = friendlyName;
			}
		}
	}
}
