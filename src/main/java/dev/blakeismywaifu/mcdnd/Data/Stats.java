package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Stats {

	private final Map<Stat.StatName, Stat> stats = new LinkedHashMap<>();

	public Stats(JSONObject json) {
		Stat.StatName[] statOrder = {Stat.StatName.STRENGTH, Stat.StatName.DEXTERITY, Stat.StatName.CONSTITUTION, Stat.StatName.INTELLIGENCE, Stat.StatName.WISDOM, Stat.StatName.CHARISMA};
		for (Stat.StatName stat : statOrder) {
			this.stats.put(stat, new Stat(stat, json));
		}
	}

	public Stat getStat(Stat.StatName statName) {
		return stats.get(statName);
	}

	public List<ItemStack> getItems() {
		List<ItemStack> out = new ArrayList<>();
		stats.values().forEach(stat -> out.add(stat.getItem()));
		return out;
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
			String statName = this.statName.toString().toLowerCase();
			item.lore(statName.substring(0, 1).toUpperCase() + statName.substring(1));
			return item.build();
		}

		private int getStatValue(JSONObject json, String key) {
			JSONArray statsArray = json.getJSONArray(key);
			JSONObject statObject = statsArray.getJSONObject(this.statName.index);
			return statObject.isNull("value") ? 0 : statObject.getInt("value");
		}

		public enum StatName {
			STRENGTH("STR", 0),
			DEXTERITY("DEX", 1),
			CONSTITUTION("CON", 2),
			INTELLIGENCE("INT", 3),
			WISDOM("WIS", 4),
			CHARISMA("CHA", 5);

			public final String shortHand;
			public final Integer index;

			StatName(String shortHand, Integer index) {
				this.shortHand = shortHand;
				this.index = index;
			}
		}
	}
}
