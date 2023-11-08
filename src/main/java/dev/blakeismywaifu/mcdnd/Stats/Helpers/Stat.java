package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Stat {

	public final StatName statName;
	private final JSONObject json;
	public Integer total;
	public Integer modifier;
	public Long base;
	public Long racial = 0L;
	public Long ability = 0L;
	public Long misc = 0L;
	public Long stacking = 0L;
	public Long set = 0L;
	public Long bonus;
	public Long override;

	public Stat(StatName statName, JSONObject json) {
		this.json = json;
		this.statName = statName;
		this.base = getStatValue("stats");
		this.bonus = getStatValue("bonusStats");
		this.override = getStatValue("overrideStats");

		this.total = Math.toIntExact(this.override == 0L ? this.base : this.override);
		this.modifier = (this.total - 10) / 2;
	}

	public ItemStack getItem() {
		ItemBuilder item = new ItemBuilder("Modifier: +" + this.modifier);
		item.lore("Total: " + this.total);
		String statName = this.statName.toString().toLowerCase();
		item.lore(statName.substring(0, 1).toUpperCase() + statName.substring(1));
		return item.build();
	}

	private Long getStatValue(String key) {
		JSONArray statsArray = (JSONArray) this.json.get(key);
		JSONObject statObject = (JSONObject) statsArray.get(this.statName.index);
		Object value = statObject.get("value");
		if (value == null) return 0L;
		return (Long) statObject.get("value");
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
