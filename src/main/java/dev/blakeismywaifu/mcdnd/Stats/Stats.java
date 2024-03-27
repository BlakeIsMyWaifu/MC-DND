package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Stats {

	public final Map<Stat.StatName, Stat> stats = new LinkedHashMap<>();

	public Stats(JSONObject json) {
		Stat.StatName[] statOrder = {Stat.StatName.STRENGTH, Stat.StatName.DEXTERITY, Stat.StatName.CONSTITUTION, Stat.StatName.INTELLIGENCE, Stat.StatName.WISDOM, Stat.StatName.CHARISMA};
		for (Stat.StatName stat : statOrder) {
			this.stats.put(stat, new Stat(stat, json));
		}
	}

	public List<ItemStack> getItems() {
		List<ItemStack> out = new ArrayList<>();
		stats.values().forEach(stat -> out.add(stat.getItem()));
		return out;
	}
}