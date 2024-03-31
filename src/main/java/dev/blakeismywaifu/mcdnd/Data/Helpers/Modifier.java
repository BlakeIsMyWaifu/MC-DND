package dev.blakeismywaifu.mcdnd.Data.Helpers;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Modifier {

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
	//	public dice
	public final @Nullable String restriction;
	public final @Nullable Integer statId;
	public final @Nullable JSONObject duration;
	public final @Nullable Integer value;

	public Modifier(JSONObject json) {
		Object fixedValue = json.get("fixedValue");
		this.fixedValue = fixedValue != null ? ((Long) fixedValue).intValue() : null;

		this.id = (String) json.get("id");

		Object entityId = json.get("entityId");
		this.entityId = entityId != null ? ((Long) entityId).intValue() : null;

		Object entityTypeId = json.get("entityTypeId");
		this.entityTypeId = entityTypeId != null ? ((Long) entityTypeId).intValue() : null;

		String type = (String) json.get("type");
		this.type = ModifierType.findType(type);

		this.subType = (String) json.get("subType");

		Object restriction = json.get("restriction");
		this.restriction = restriction != null ? (String) restriction : null;

		Object statId = json.get("statId");
		this.statId = statId != null ? ((Long) statId).intValue() : null;

		this.requiresAttunement = (Boolean) json.get("requiresAttunement");

		Object duration = json.get("duration");
		this.duration = duration != null ? (JSONObject) duration : null;

		this.friendlyTypeName = (String) json.get("friendlyTypeName");
		this.friendlySubtypeName = (String) json.get("friendlySubtypeName");
		this.isGranted = (Boolean) json.get("isGranted");
		this.bonusTypes = (JSONArray) json.get("bonusTypes");

		Object value = json.get("value");
		this.value = value != null ? ((Long) value).intValue() : null;

		this.availableToMulticlass = (Boolean) json.get("availableToMulticlass");
		this.modifierTypeId = ((Long) json.get("modifierTypeId")).intValue();
		this.modifierSubTypeId = ((Long) json.get("modifierSubTypeId")).intValue();
		this.componentId = ((Long) json.get("componentId")).intValue();
		this.componentTypeId = ((Long) json.get("componentTypeId")).intValue();
	}

	public enum ModifierType {
		BONUS("bonus"),
		PROFICIENCY("proficiency"),
		EXPERTISE("expertise"),
		HALF_PROFICIENCY("half-proficiency"),
		SET_BASE("set-base"),
		ADVANTAGE("advantage"),
		SET("set"),
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
