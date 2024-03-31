package dev.blakeismywaifu.mcdnd.Data.Helpers;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

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
