package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Utils.Item;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Miscellaneous {

	public Long proficiency;
	public Long speed;
	public Boolean inspiration;
	public Long initiative;
	public Long armour;

	public Miscellaneous(JSONObject json) {
		this.proficiency = getProficiency(json);
		this.speed = getSpeed(json);
		// TODO - need dex stat
		this.initiative = 1L;
		this.inspiration = (Boolean) json.get("inspiration");
	}

	public static ItemStack item(JSONObject json) {
		Miscellaneous data = new Miscellaneous(json);
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.WHITE + "+" + data.proficiency + ChatColor.GRAY + " Proficiency Bonus");
		lore.add(ChatColor.WHITE + "+" + data.initiative + ChatColor.GRAY + " Initiative");
		lore.add(ChatColor.WHITE + data.speed.toString() + ChatColor.GRAY + "ft. Walking Speed");
		lore.add((data.inspiration ? ChatColor.GREEN : ChatColor.RED) + "Inspiration");
		return Item.create(Item.main, "Miscellaneous:", lore, 1);
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
