package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

public class Stat {

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
		ItemBuilder item = new ItemBuilder("Modifier: +" + this.modifier);
		item.lore("Total: " + this.total);
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
