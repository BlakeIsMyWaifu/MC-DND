package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import java.util.HashMap;
import java.util.Map;

public enum Classes {
	ARTIFICER("Artificer"),
	BARBARIAN("Barbarian"),
	BARD("Bard"),
	CLERIC("Cleric"),
	DRUID("Druid"),
	FIGHTER("Fighter"),
	MONK("Monk"),
	PALADIN("Paladin"),
	RANGER("Ranger"),
	ROGUE("Rogue"),
	SORCERER("Sorcerer"),
	WARLOCK("Warlock"),
	WIZARD("Wizard");

	private static final Map<String, Classes> nameMap = new HashMap<>();

	static {
		for (Classes classValue : values()) {
			nameMap.put(classValue.name, classValue);
		}
	}

	public final String name;

	Classes(String name) {
		this.name = name;
	}
	
	public static Classes findClass(String name) {
		return nameMap.get(name);
	}
}
