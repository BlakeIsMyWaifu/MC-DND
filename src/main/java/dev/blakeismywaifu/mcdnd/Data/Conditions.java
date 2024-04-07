package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Conditions {

	private final Set<Condition> conditions = new LinkedHashSet<>();

	public Conditions(JSONObject json) {
		JSONArray conditionsJson = json.getJSONArray("conditions");
		for (Object conditionObject : conditionsJson) {
			JSONObject conditionJson = (JSONObject) conditionObject;
			this.conditions.add(new Condition(conditionJson));
		}
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Conditions");
		if (this.conditions.size() == 0) {
			itemBuilder.loreNone();
		} else {
			this.conditions.forEach(condition -> {
				String level = " (Level " + condition.level + ")";
				itemBuilder.lore(condition.type.friendlyName + (condition.level == null ? "" : level));
			});
		}
		return itemBuilder.build();
	}

	public static class Condition {

		public final Type type;
		public final @Nullable Integer level;

		public Condition(JSONObject json) {
			int id = json.getInt("id");
			this.type = Type.findType(id);
			this.level = id == 4 ? json.getInt("level") : null;
		}

		public enum Type {
			BLINDED(1, "Blinded"),
			CHARMED(2, "Charmed"),
			DEAFENED(3, "Deafened"),
			EXHAUSTION(4, "Exhaustion"),
			FRIGHTENED(5, "Frightened"),
			GRAPPLED(6, "Grappled"),
			INCAPACITATED(7, "Incapacitated"),
			INVISIBLE(8, "Invisible"),
			PARALYZED(9, "Paralyzed"),
			PETRIFIED(10, "Petrified"),
			POISONED(11, "Poisoned"),
			PRONE(12, "Prone"),
			RESTRAINED(13, "Restrained"),
			STUNNED(14, "Stunned"),
			UNCONSCIOUS(15, "Unconscious");

			private static final Map<Integer, Type> idMap = new HashMap<>();

			static {
				for (Type type : values()) {
					idMap.put(type.id, type);
				}
			}

			private final int id;
			private final String friendlyName;

			Type(int id, String friendlyName) {
				this.id = id;
				this.friendlyName = friendlyName;
			}

			public static Type findType(int id) {
				return idMap.get(id);
			}
		}
	}
}
