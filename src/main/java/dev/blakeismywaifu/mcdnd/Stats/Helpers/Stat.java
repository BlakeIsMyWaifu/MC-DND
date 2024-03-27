package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Stat {

	public final StatName statName;
	public Integer modifier;
	public Integer base;
	public Integer bonus;
	public Integer override;
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
		ItemBuilder item = new ItemBuilder("Modifier: +" + this.modifier);
		item.lore("Total: " + this.total);
		String statName = this.statName.toString().toLowerCase();
		item.lore(statName.substring(0, 1).toUpperCase() + statName.substring(1));
		return item.build();
	}

	private Integer getStatValue(JSONObject json, String key) {
		JSONArray statsArray = (JSONArray) json.get(key);
		JSONObject statObject = (JSONObject) statsArray.get(this.statName.index);
		Object value = statObject.get("value");
		if (value == null) return 0;
		return Math.toIntExact((Long) statObject.get("value"));
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
