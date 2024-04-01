package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Proficiencies {

	private final Map<String, Type> dictionary = new HashMap<>();
	private final Map<Type, Set<String>> proficiencies = new HashMap<>();

	public Proficiencies() {
		proficiencies.put(Type.ARMOUR, new HashSet<>());
		proficiencies.put(Type.WEAPON, new HashSet<>());
		proficiencies.put(Type.TOOL, new HashSet<>());
		proficiencies.put(Type.LANGUAGE, new HashSet<>());

		fillDictionary();
	}

	public void updateData(Modifier modifier) {
		if (modifier.type == Modifier.ModifierType.PROFICIENCY) addProficiency(modifier);
		if (modifier.type == Modifier.ModifierType.LANGUAGE) {
			if (Objects.equals(modifier.subType, "choose-a-language")) return;
			addProficiency(Type.LANGUAGE, modifier.friendlySubtypeName);
		}
	}

	private void addProficiency(Modifier modifier) {
		@Nullable Type proficiencyType = dictionary.get(modifier.subType);
		if (proficiencyType == null) {
			Console.warn("Missing Proficiency: " + modifier.subType);
		} else {
			addProficiency(proficiencyType, modifier.friendlySubtypeName);
		}
	}

	private void addProficiency(Type type, String name) {
		proficiencies.get(type).add(name);
	}

	public ItemStack getItem(Type type) {
		ItemBuilder itemBuilder = new ItemBuilder(type.friendlyName);
		proficiencies.get(type).forEach(proficiency -> itemBuilder.lore("● " + proficiency));
		return itemBuilder.build();
	}

	private void fillDictionary() {
		String[] armours = {
				"light-armor",
				"medium-armor",
				"heavy-armor",
				"shields",
		};
		for (String armour : armours) {
			dictionary.put(armour, Type.ARMOUR);
		}

		String[] weapons = {
				"club",
				"crossbow-hand",
				"crossbow-light",
				"dagger",
				"dart",
				"javelin",
				"longbow",
				"longsword",
				"mace",
				"martial-weapons",
				"quarterstaff",
				"rapier",
				"scimitar",
				"shortbow",
				"shortsword",
				"sickle",
				"simple-weapons",
				"sling",
				"spear",
				"whip",
		};
		for (String weapon : weapons) {
			dictionary.put(weapon, Type.WEAPON);
		}

		String[] tools = {
				"alchemists-supplies",
				"cartographers-tools",
				"cooks-utensils",
				"disguise-kit",
				"dragonchess-set",
				"herbalism-kit",
				"painters-supplies",
				"poisoners-kit",
				"thieves-tools",
				"vehicles-land"
		};
		for (String tool : tools) {
			dictionary.put(tool, Type.TOOL);
		}
	}

	public enum Type {
		ARMOUR("Armour"),
		WEAPON("Weapons"),
		TOOL("Tools"),
		LANGUAGE("Languages");

		private final String friendlyName;

		Type(String friendlyName) {
			this.friendlyName = friendlyName;
		}
	}
}
