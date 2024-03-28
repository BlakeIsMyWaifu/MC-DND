package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Modifiers {

	public Map<ModifierCategory, List<Modifier>> modifiers = new HashMap<>();

	public Modifiers(JSONObject data) {
		JSONObject modifiersJson = (JSONObject) data.get("modifiers");
		for (String modifierType : new String[]{"race", "class", "background", "item", "feat"}) {
			JSONArray modifiers = (JSONArray) modifiersJson.get(modifierType);
			for (Object modifierJson : modifiers) {
				Modifier modifier = new Modifier((JSONObject) modifierJson);
				sortModifier(modifier);
			}
		}
	}

	public void updateData(ModifierCategory modifierCategory, CharacterData characterData) {
		switch (modifierCategory) {
			case STATS:
				modifiers.computeIfAbsent(ModifierCategory.STATS, k -> new ArrayList<>()).forEach(m -> characterData.stats.updateData(m));
				break;
			case SKILLS:
				modifiers.computeIfAbsent(ModifierCategory.SKILLS, k -> new ArrayList<>()).forEach(m -> characterData.skills.updateData(m));
				break;
			case MISCELLANEOUS:
				modifiers.computeIfAbsent(ModifierCategory.MISCELLANEOUS, k -> new ArrayList<>()).forEach(m -> characterData.miscellaneous.updateData(m));
				break;
		}
	}

	private void addModifier(ModifierCategory modifierCategory, Modifier modifier) {
		modifiers.computeIfAbsent(modifierCategory, k -> new ArrayList<>()).add(modifier);
	}

	private void sortModifier(Modifier modifier) {
		if (modifier.type == Modifier.ModifierType.BONUS) sortBonusModifier(modifier);
		if (modifier.type == Modifier.ModifierType.PROFICIENCY) addModifier(ModifierCategory.SKILLS, modifier);
	}

	private void sortBonusModifier(Modifier modifier) {
		switch (modifier.subType) {
			case "speed":
				addModifier(ModifierCategory.MISCELLANEOUS, modifier);
				break;
			case "strength-score":
			case "dexterity-score":
			case "constitution-score":
			case "intelligence-score":
			case "wisdom-score":
			case "charisma-score":
				addModifier(ModifierCategory.STATS, modifier);
				break;
		}
	}

	public enum ModifierCategory {
		STATS,
		SKILLS,
		MISCELLANEOUS
	}
}
