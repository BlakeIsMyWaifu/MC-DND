package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.DNDClass;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CharacterInfo {

	public final String name;
	public final @Nullable String gender;
	public final String race;
	public final List<DNDClass> classes = new ArrayList<>();
	public final Integer totalLevel;

	public CharacterInfo(JSONObject data) {
		this.name = data.getString("name");
		this.gender = data.getString("gender");

		JSONObject raceData = data.getJSONObject("race");
		this.race = raceData.getString("fullName");

		JSONArray classesData = data.getJSONArray("classes");
		for (Object classData : classesData) {
			JSONObject classJson = (JSONObject) classData;
			JSONObject classDefinition = classJson.getJSONObject("definition");
			String className = classDefinition.getString("name");
			int level = classJson.getInt("level");
			JSONObject subclassDefinition = classJson.getJSONObject("subclassDefinition");
			@Nullable String subclassName = subclassDefinition != null ? subclassDefinition.getString("name") : null;
			DNDClass dndClass = new DNDClass(className, level, subclassName);
			classes.add(dndClass);
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
