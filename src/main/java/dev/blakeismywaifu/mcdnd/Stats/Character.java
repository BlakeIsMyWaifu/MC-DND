package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Utils.Item;
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

		JSONArray classData = (JSONArray) data.get("classes");
		JSONObject mainClass = (JSONObject) classData.get(0);
		JSONObject mainClassDefinition = (JSONObject) mainClass.get("definition");
		this.classes = (String) mainClassDefinition.get("name");
		this.level = (Long) mainClass.get("level");
	}

	public static ItemStack item(JSONObject json) {
		Character data = new Character(json);
		List<String> info = new ArrayList<>();
		if (data.gender != null) info.add(data.gender);
		info.add(data.race);
		info.add(data.classes);
		ArrayList<String> lore = new ArrayList<>();
		lore.add(String.join(", ", info));
		lore.add("Level " + data.level);
		return Item.create(Item.main, data.name, lore, null);
	}
}
