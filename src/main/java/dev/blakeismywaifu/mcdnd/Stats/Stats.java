package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Stats {

	public final List<Stat> stats = new ArrayList<>();

	public Stats(JSONObject json) {
		this.stats.add(new Stat(Stat.StatName.STRENGTH, json));
		this.stats.add(new Stat(Stat.StatName.DEXTERITY, json));
		this.stats.add(new Stat(Stat.StatName.CONSTITUTION, json));
		this.stats.add(new Stat(Stat.StatName.INTELLIGENCE, json));
		this.stats.add(new Stat(Stat.StatName.WISDOM, json));
		this.stats.add(new Stat(Stat.StatName.CHARISMA, json));
	}

	public List<ItemStack> getItems() {
		List<ItemStack> out = new ArrayList<>();
		stats.forEach(stat -> out.add(stat.getItem()));
		return out;
	}
}
