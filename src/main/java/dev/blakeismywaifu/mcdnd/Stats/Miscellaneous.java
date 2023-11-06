package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifiers;
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
	public Long initiative;

	public Miscellaneous(JSONObject json) {
		this.proficiency = getProficiency(json);
		this.speed = getSpeed(json);
		// TODO - need dex stat
		this.initiative = 1L;
		this.inspiration = (Boolean) json.get("inspiration");
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Miscellaneous");
		itemBuilder.lore(loreLine("+" + this.proficiency, " Proficiency Bonus"));
		itemBuilder.lore(loreLine("+" + this.initiative, " Initiative"));
		itemBuilder.lore(loreLine(this.speed.toString(), "ft. Walking Speed"));
		itemBuilder.lore(Component.text("Inspiration", this.inspiration ? NamedTextColor.GREEN : NamedTextColor.RED));
		itemBuilder.modelData(1);
		return itemBuilder.getItem();
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
		Long baseSpeed = (long) normal.get("walk");
		Integer bonusSpeed = Modifiers.findBonusValues(json, "speed");
		return (baseSpeed + bonusSpeed);
	}
}
