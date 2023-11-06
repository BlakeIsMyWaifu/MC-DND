package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Character {

	public String name;
	public String gender;
	public String race;
	public String classes;
	public Long level;

	public Character(JSONObject data) {
		this.name = (String) data.get("name");
		this.gender = (String) data.get("gender");

		JSONObject raceData = (JSONObject) data.get("race");
		this.race = (String) raceData.get("fullName");

		// TODO only tracks main class
		JSONArray classData = (JSONArray) data.get("classes");
		JSONObject mainClass = (JSONObject) classData.get(0);
		JSONObject mainClassDefinition = (JSONObject) mainClass.get("definition");
		this.classes = (String) mainClassDefinition.get("name");
		this.level = (Long) mainClass.get("level");
	}

	public ItemStack getItem() {
		List<String> info = new ArrayList<>();
		if (this.gender != null) info.add(this.gender);
		info.add(this.race);
		info.add(this.classes);
		ItemBuilder itemBuilder = new ItemBuilder(this.name);
		itemBuilder.lore(String.join(", ", info));
		itemBuilder.lore("Level " + this.level);
		return itemBuilder.getItem();
	}
}
