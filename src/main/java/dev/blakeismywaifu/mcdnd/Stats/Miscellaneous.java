package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Miscellaneous {

	public Integer proficiency;
	public Integer speed;
	public Boolean inspiration;
	public Integer initiative;

	public Miscellaneous(JSONObject json, Stats stats, Character character) {
		this.proficiency = getProficiency(character);
		this.speed = getSpeed(json);
		this.initiative = stats.stats.get(Stat.StatName.DEXTERITY).modifier;
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

	private Integer getProficiency(Character character) {
		Map<Range, Integer> map = new HashMap<>();
		map.put(new Range(1, 4), 2);
		map.put(new Range(5, 8), 3);
		map.put(new Range(9, 12), 4);
		map.put(new Range(13, 16), 5);
		map.put(new Range(17, 20), 6);
		return Range.contains(map, character.totalLevel);
	}

	private Integer getSpeed(JSONObject json) {
		JSONObject raceData = (JSONObject) json.get("race");
		JSONObject weightSpeeds = (JSONObject) raceData.get("weightSpeeds");
		JSONObject normal = (JSONObject) weightSpeeds.get("normal");
		return ((Long) normal.get("walk")).intValue();
	}

	public void updateData(Modifier modifier) {
		if (Objects.equals(modifier.subType, "speed")) {
			assert modifier.value != null;
			this.speed += modifier.value.intValue();
		}
	}
}
