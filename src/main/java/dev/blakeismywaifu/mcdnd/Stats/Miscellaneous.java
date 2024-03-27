package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Miscellaneous {

	public Long proficiency;
	public Long speed;
	public Boolean inspiration;
	public Integer initiative;

	public Miscellaneous(JSONObject json, CharacterData characterData) {
		this.proficiency = getProficiency(json);
		this.speed = getSpeed(json);
		this.initiative = characterData.stats.stats.get(Stat.StatName.DEXTERITY).modifier;
		this.inspiration = (Boolean) json.get("inspiration");
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Miscellaneous");
		itemBuilder.lore(loreLine("+" + this.proficiency, " Proficiency Bonus"));
		itemBuilder.lore(loreLine("+" + this.initiative, " Initiative"));
		itemBuilder.lore(loreLine(this.speed.toString(), "ft. Walking Speed"));
		itemBuilder.lore(Component.text("Inspiration", this.inspiration ? NamedTextColor.GREEN : NamedTextColor.RED));
		itemBuilder.modelData(1);
		return itemBuilder.build();
	}

	private Component loreLine(String stat, String description) {
		return Component.text(stat, NamedTextColor.WHITE).append(Component.text(description, NamedTextColor.GRAY));
	}

	private Long getProficiency(JSONObject json) {
		Long level = new Character(json).level;
		Map<Range, Integer> map = new HashMap<>();
		map.put(new Range(1, 4), 2);
		map.put(new Range(5, 8), 3);
		map.put(new Range(9, 12), 4);
		map.put(new Range(13, 16), 5);
		map.put(new Range(17, 20), 6);
		return Long.valueOf(Range.contains(map, Math.toIntExact(level)));
	}

	private Long getSpeed(JSONObject json) {
		JSONObject raceData = (JSONObject) json.get("race");
		JSONObject weightSpeeds = (JSONObject) raceData.get("weightSpeeds");
		JSONObject normal = (JSONObject) weightSpeeds.get("normal");
		return (Long) normal.get("walk");
	}
}
