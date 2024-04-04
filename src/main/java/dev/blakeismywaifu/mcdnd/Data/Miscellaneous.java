package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Stats.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Miscellaneous {

	public final Integer proficiency;
	public final Boolean inspiration;
	public final Integer initiative;
	public Integer speed;

	public Miscellaneous(JSONObject json, Stats stats, CharacterInfo characterInfo) {
		this.proficiency = getProficiency(characterInfo);
		this.speed = getSpeed(json);
		this.initiative = stats.getStat(Stat.StatName.DEXTERITY).modifier;
		this.inspiration = json.getBoolean("inspiration");
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

	private Integer getProficiency(CharacterInfo characterInfo) {
		Map<Range, Integer> map = new HashMap<>();
		map.put(new Range(1, 4), 2);
		map.put(new Range(5, 8), 3);
		map.put(new Range(9, 12), 4);
		map.put(new Range(13, 16), 5);
		map.put(new Range(17, 20), 6);
		return Range.contains(map, characterInfo.totalLevel);
	}

	private Integer getSpeed(JSONObject json) {
		JSONObject raceData = json.getJSONObject("race");
		JSONObject weightSpeeds = raceData.getJSONObject("weightSpeeds");
		JSONObject normal = weightSpeeds.getJSONObject("normal");
		return normal.getInt("walk");
	}

	public void updateData(Modifier modifier) {
		if (Objects.equals(modifier.subType, "speed")) {
			assert modifier.value != null;
			this.speed += modifier.value;
		}
	}
}
