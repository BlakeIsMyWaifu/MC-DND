package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Data.Skills;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class Modifiers {

	private final Map<Category, List<Modifier>> modifiers = new HashMap<>();

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

	public List<Modifier> getModifiers(Category category) {
		return modifiers.computeIfAbsent(category, k -> new ArrayList<>());
	}

	private void addModifier(Category category, Modifier modifier) {
		modifiers.computeIfAbsent(category, k -> new ArrayList<>()).add(modifier);
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
				if (modifier.subType.endsWith("saving-throws")) {
					addModifier(Category.SAVING_THROW, modifier);
				} else {
					addModifier(Category.SKILLS, modifier);
				}
				break;
			case PROFICIENCY:
				boolean isSkill = Skills.SkillName.labelList.contains(modifier.subType);
				boolean isSkillStat = modifier.subType.endsWith("-ability-checks");

				boolean isSavingThrow = modifier.subType.endsWith("-saving-throws");

				if (isSkill || isSkillStat) {
					addModifier(Category.SKILLS, modifier);
				} else if (isSavingThrow) {
					addModifier(Category.SAVING_THROW, modifier);
				} else {
					addModifier(Category.PROFICIENCIES, modifier);
				}
				break;
			case LANGUAGE:
				addModifier(Category.PROFICIENCIES, modifier);
				break;
			case RESISTANCE:
			case IMMUNITY:
				addModifier(Category.DEFENCES, modifier);
				break;
			case SET_BASE:
			case SENSE:
				addModifier(Category.SENSES, modifier);
				break;
		}
	}

	private void sortBonusModifier(Modifier modifier) {
		switch (modifier.subType) {
			case "speed" -> addModifier(Category.MISCELLANEOUS, modifier);
			case "strength-score", "dexterity-score", "constitution-score", "intelligence-score", "wisdom-score", "charisma-score" ->
					addModifier(Category.STATS, modifier);
			case "hit-points-per-level" -> addModifier(Category.HITPOINTS, modifier);
		}
	}

	public enum Category {
		STATS,
		SKILLS,
		MISCELLANEOUS,
		HITPOINTS,
		PROFICIENCIES,
		DEFENCES,
		SAVING_THROW,
		SENSES
	}

	public enum Proficiency {
		NOT,
		HALF,
		PROFICIENT,
		EXPERTISE
	}

	public enum Vantage {
		ADVANTAGE,
		NONE,
		DISADVANTAGE,
	}

	public static class Modifier {

		public final @Nullable Integer fixedValue;
		public final String id;
		public final @Nullable Integer entityId;
		public final @Nullable Integer entityTypeId;
		public final ModifierType type;
		public final String subType;
		public final Boolean requiresAttunement;
		public final String friendlyTypeName;
		public final String friendlySubtypeName;
		public final Boolean isGranted;
		public final JSONArray bonusTypes;
		public final Boolean availableToMulticlass;
		public final Integer modifierTypeId;
		public final Integer modifierSubTypeId;
		public final Integer componentId;
		public final Integer componentTypeId;
		// dice
		public final @Nullable String restriction;
		public final @Nullable Integer statId;
		public final @Nullable JSONObject duration;
		public final @Nullable Integer value;

		public Modifier(JSONObject json) {
			this.fixedValue = json.isNull("fixedValue") ? null : json.getInt("fixedValue");
			this.id = json.getString("id");
			this.entityId = json.isNull("entityId") ? null : json.getInt("entityId");
			this.entityTypeId = json.isNull("entityTypeId") ? null : json.getInt("entityTypeId");
			this.subType = json.getString("subType");
			this.restriction = json.isNull("restriction") ? null : json.getString("restriction");
			this.statId = json.isNull("statId") ? null : json.getInt("statId");
			this.requiresAttunement = json.getBoolean("requiresAttunement");
			this.duration = json.isNull("duration") ? null : json.getJSONObject("duration");
			this.friendlyTypeName = json.getString("friendlyTypeName");
			this.friendlySubtypeName = json.getString("friendlySubtypeName");
			this.isGranted = json.getBoolean("isGranted");
			this.bonusTypes = json.getJSONArray("bonusTypes");
			this.value = json.isNull("value") ? null : json.getInt("value");
			this.availableToMulticlass = json.getBoolean("availableToMulticlass");
			this.modifierTypeId = json.getInt("modifierTypeId");
			this.modifierSubTypeId = json.getInt("modifierSubTypeId");
			this.componentId = json.getInt("componentId");
			this.componentTypeId = json.getInt("componentTypeId");

			String type = json.getString("type");
			this.type = ModifierType.findType(type);
		}

		public enum ModifierType {
			BONUS("bonus"),
			PROFICIENCY("proficiency"),
			EXPERTISE("expertise"),
			HALF_PROFICIENCY("half-proficiency"),
			SET_BASE("set-base"),
			SENSE("sense"),
			ADVANTAGE("advantage"),
			SET("set"),
			LANGUAGE("language"),
			RESISTANCE("resistance"),
			IMMUNITY("immunity"),
			_UNKNOWN("unknown");

			private static final Map<String, ModifierType> labelMap = new HashMap<>();

			static {
				for (ModifierType type : values()) {
					labelMap.put(type.label, type);
				}
			}

			private final String label;

			ModifierType(String label) {
				this.label = label;
			}

			public static ModifierType findType(String label) {
				ModifierType type = labelMap.get(label);
				return type != null ? type : ModifierType._UNKNOWN;
			}
		}
	}
}
