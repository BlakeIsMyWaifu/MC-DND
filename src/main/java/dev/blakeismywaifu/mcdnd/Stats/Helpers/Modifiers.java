package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Objects;

public class Modifiers {

	public @Nullable Long fixedValue;
	public String id;
	public @Nullable Long entityId;
	public @Nullable Long entityTypeId;
	public Type type;
	public String subType;
	//	public dice
	public @Nullable String restriction;
	public @Nullable Long statId;
	public Boolean requiresAttunement;
	public @Nullable Long duration;
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

	public Modifiers(JSONObject json) {
		Object fixedValue = json.get("fixedValue");
		if (fixedValue != null) this.fixedValue = (Long) fixedValue;

		this.id = (String) json.get("id");

		Object entityId = json.get("entityId");
		if (entityId != null) this.entityId = (Long) entityId;

		Object entityTypeId = json.get("entityTypeId");
		if (entityTypeId != null) this.entityTypeId = (Long) entityTypeId;

		String type = (String) json.get("type");
		this.type = Objects.equals(type, "bonus") ? Type.BONUS : Type.PROFICIENCY;

		this.subType = (String) json.get("subType");

		Object restriction = json.get("restriction");
		if (restriction != null) this.restriction = (String) restriction;

		Object statId = json.get("statId");
		if (statId != null) this.statId = (Long) statId;

		this.requiresAttunement = (Boolean) json.get("requiresAttunement");

		Object duration = json.get("duration");
		if (duration != null) this.duration = (Long) duration;

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

	public static Integer findBonusValues(JSONObject json, String subType) {
		JSONObject allModifiers = (JSONObject) json.get("modifiers");
		long out = 0L;
		JSONArray feat = (JSONArray) allModifiers.get("feat");
		for (Object value : feat) {
			Modifiers modifiers = new Modifiers((JSONObject) value);
			if (Objects.equals(modifiers.subType, subType)) {
				if (modifiers.value == null) continue;
				out += modifiers.value;
			}
		}
		return Math.toIntExact(out);
	}

	enum Type {
		BONUS,
		PROFICIENCY
	}
}
