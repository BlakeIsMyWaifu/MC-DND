package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Data.Skills;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Modifiers {

	private final Map<ModifierCategory, List<Modifier>> modifiers = new HashMap<>();

	public Modifiers(JSONObject json) {
		JSONObject modifiersJson = json.getJSONObject("modifiers");
		for (String modifierType : new String[]{"race", "class", "background", "item", "feat"}) {
			JSONArray modifiers = modifiersJson.getJSONArray(modifierType);
			modifiers.forEach(modifierJson -> {
				Modifier modifier = new Modifier((JSONObject) modifierJson);
				sortModifier(modifier);
			});
		}
	}

	public List<Modifier> getModifiers(ModifierCategory modifierCategory) {
		return modifiers.computeIfAbsent(modifierCategory, k -> new ArrayList<>());
	}

	private void addModifier(ModifierCategory modifierCategory, Modifier modifier) {
		modifiers.computeIfAbsent(modifierCategory, k -> new ArrayList<>()).add(modifier);
	}

	private void sortModifier(Modifier modifier) {
		switch (modifier.type) {
			case BONUS:
				sortBonusModifier(modifier);
				break;
			case HALF_PROFICIENCY:
				if (!Objects.equals(modifier.subType, "ability-checks")) break;
			case EXPERTISE:
			case ADVANTAGE:
				addModifier(ModifierCategory.SKILLS, modifier);
				break;
			case PROFICIENCY:
				boolean isSkill = Skills.SkillName.labelList.contains(modifier.subType);
				boolean isSkillStat = modifier.subType.endsWith("-ability-checks");
				if (isSkill || isSkillStat) {
					addModifier(ModifierCategory.SKILLS, modifier);
				} else {
					addModifier(ModifierCategory.PROFICIENCIES, modifier);
				}
				break;
			case LANGUAGE:
				addModifier(ModifierCategory.PROFICIENCIES, modifier);
				break;
			case RESISTANCE:
			case IMMUNITY:
				addModifier(ModifierCategory.DEFENCES, modifier);
				break;
		}
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
			case "hit-points-per-level":
				addModifier(ModifierCategory.HITPOINTS, modifier);
				break;
		}
	}

	public enum ModifierCategory {
		STATS,
		SKILLS,
		MISCELLANEOUS,
		HITPOINTS,
		PROFICIENCIES,
		DEFENCES
	}
}
