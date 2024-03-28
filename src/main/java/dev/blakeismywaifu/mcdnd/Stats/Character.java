package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.DNDClass;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Character {

	public final String name;
	public final @Nullable String gender;
	public final String race;
	public final List<DNDClass> classes = new ArrayList<>();
	public final Integer totalLevel;

	public Character(JSONObject data) {
		this.name = (String) data.get("name");
		this.gender = (String) data.get("gender");

		JSONObject raceData = (JSONObject) data.get("race");
		this.race = (String) raceData.get("fullName");

		JSONArray classesData = (JSONArray) data.get("classes");
		for (Object classData : classesData) {
			JSONObject classJson = (JSONObject) classData;
			JSONObject classDefinition = (JSONObject) classJson.get("definition");
			String className = (String) classDefinition.get("name");
			Long levelLong = (Long) classJson.get("level");
			JSONObject subclassDefinition = (JSONObject) classJson.get("subclassDefinition");
			@Nullable String subclassName = subclassDefinition != null ? (String) subclassDefinition.get("name") : null;
			classes.add(new DNDClass(className, levelLong.intValue(), subclassName));
		}

		this.totalLevel = this.classes.stream().mapToInt(dndClass -> dndClass.level).sum();
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder(this.name);
		this.classes.forEach(dndClass -> {
			String subclass = dndClass.subclass != null ? dndClass.subclass : "no subclass";
			itemBuilder.lore(dndClass.className.string + " " + dndClass.level + " (" + subclass + ")");
		});
		String gender = this.gender != null ? this.gender : "No Gender";
		itemBuilder.lore(gender + ", " + this.race);
		return itemBuilder.build();
	}
}
