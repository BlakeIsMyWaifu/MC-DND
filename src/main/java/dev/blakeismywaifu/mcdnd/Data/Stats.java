package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Stat;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

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
		Stat.StatName stat = null;

		switch (modifier.subType) {
			case "strength-score":
				stat = Stat.StatName.STRENGTH;
				break;
			case "dexterity-score":
				stat = Stat.StatName.DEXTERITY;
				break;
			case "constitution-score":
				stat = Stat.StatName.CONSTITUTION;
				break;
			case "intelligence-score":
				stat = Stat.StatName.INTELLIGENCE;
				break;
			case "wisdom-score":
				stat = Stat.StatName.WISDOM;
				break;
			case "charisma-score":
				stat = Stat.StatName.CHARISMA;
				break;
		}

		assert modifier.value != null;
		this.stats.get(stat).addToTotal(modifier.value);
	}
}
