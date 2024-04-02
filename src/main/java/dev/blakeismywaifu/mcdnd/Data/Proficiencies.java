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

	private final Map<String, String> armourDictionary = new HashMap<>();

	public Proficiencies() {
		this.proficiencies.put(Type.ARMOUR, new HashSet<>());
		this.proficiencies.put(Type.WEAPON, new HashSet<>());
		this.proficiencies.put(Type.TOOL, new HashSet<>());
		this.proficiencies.put(Type.LANGUAGE, new HashSet<>());

		fillDictionary();
	}

	public Boolean isArmourProficient(String baseArmourName) {
		@Nullable String armourType = this.armourDictionary.get(baseArmourName);
		if (armourType == null) {
			Console.warn("Missing baseArmourType: " + baseArmourName);
			return true;
		}
		return this.proficiencies.get(Type.ARMOUR).contains(armourType);
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
		this.proficiencies.get(type).add(name);
	}

	public ItemStack getItem(Type type) {
		ItemBuilder itemBuilder = new ItemBuilder(type.friendlyName);
		this.proficiencies.get(type).forEach(proficiency -> itemBuilder.lore("● " + proficiency));
		return itemBuilder.build();
	}

	private void fillDictionary() {
		String[] armours = {"light-armor", "medium-armor", "heavy-armor", "shields"};
		Arrays.stream(armours).forEach(armour -> this.dictionary.put(armour, Type.ARMOUR));

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
		Arrays.stream(weapons).forEach(weapon -> this.dictionary.put(weapon, Type.WEAPON));

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
				"vehicles-land",
		};
		Arrays.stream(tools).forEach(tool -> this.dictionary.put(tool, Type.TOOL));

		String[] light = {"Studded Leather Armor", "Padded Armor", "Leather Armor"};
		Arrays.stream(light).forEach(s -> this.armourDictionary.put(s, " Light Armor"));
		String[] medium = {"Survival Mantle", "Spiked Armor", "Scale Armor", "Hide Armor", "Half Plate Armor", "Chain Shirt", "Breastplate"};
		Arrays.stream(medium).forEach(s -> this.armourDictionary.put(s, "Medium Armor"));
		String[] heavy = {"Splint Armor", "Ring Armor", "Plate Armor", "Chain Mail"};
		Arrays.stream(heavy).forEach(s -> this.armourDictionary.put(s, "Heavy Armor"));
		this.armourDictionary.put("Shields", "Shields");
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
