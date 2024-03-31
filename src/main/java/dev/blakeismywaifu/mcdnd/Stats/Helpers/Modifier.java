package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Modifier {

	public @Nullable Long fixedValue;
	public String id;
	public @Nullable Long entityId;
	public @Nullable Long entityTypeId;
	public ModifierType type;
	public String subType;
	//	public dice
	public @Nullable String restriction;
	public @Nullable Long statId;
	public Boolean requiresAttunement;
	public @Nullable JSONObject duration;
	public String friendlyTypeName;
	public String friendlySubtypeName;
	public Boolean isGranted;
	public JSONArray bonusTypes;
	public @Nullable Long value;
	public Boolean availableToMulticlass;
	public Long modifierTypeId;
	public Long modifierSubTypeId;
	public Long componentId;
	public Long componentTypeId;

	public Modifier(JSONObject json) {
		Object fixedValue = json.get("fixedValue");
		if (fixedValue != null) this.fixedValue = (Long) fixedValue;

		this.id = (String) json.get("id");

		Object entityId = json.get("entityId");
		if (entityId != null) this.entityId = (Long) entityId;

		Object entityTypeId = json.get("entityTypeId");
		if (entityTypeId != null) this.entityTypeId = (Long) entityTypeId;

		String type = (String) json.get("type");
		this.type = ModifierType.findType(type);

		this.subType = (String) json.get("subType");

		Object restriction = json.get("restriction");
		if (restriction != null) this.restriction = (String) restriction;

		Object statId = json.get("statId");
		if (statId != null) this.statId = (Long) statId;

		this.requiresAttunement = (Boolean) json.get("requiresAttunement");

		Object duration = json.get("duration");
		if (duration != null) this.duration = (JSONObject) duration;

		this.friendlyTypeName = (String) json.get("friendlyTypeName");
		this.friendlySubtypeName = (String) json.get("friendlySubtypeName");
		this.isGranted = (Boolean) json.get("isGranted");
		this.bonusTypes = (JSONArray) json.get("bonusTypes");

		Object value = json.get("value");
		if (value != null) this.value = (Long) value;

		this.availableToMulticlass = (Boolean) json.get("availableToMulticlass");
		this.modifierTypeId = (long) json.get("modifierTypeId");
		this.modifierSubTypeId = (long) json.get("modifierSubTypeId");
		this.componentId = (long) json.get("componentId");
		this.componentTypeId = (long) json.get("componentTypeId");
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
