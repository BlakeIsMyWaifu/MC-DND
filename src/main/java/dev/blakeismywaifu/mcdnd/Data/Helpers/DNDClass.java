package dev.blakeismywaifu.mcdnd.Data.Helpers;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DNDClass {

	public final ClassName className;
	public final Integer level;
	public final @Nullable String subclass;

	public DNDClass(String className, Integer level, @Nullable String subclass) {
		this.className = ClassName.findClass(className);
		this.level = level;
		this.subclass = subclass;
	}

	public enum ClassName {
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

		private static final Map<String, ClassName> stringMap = new HashMap<>();

		static {
			for (ClassName classValue : values()) {
				stringMap.put(classValue.string, classValue);
			}
		}

		public final String string;

		ClassName(String string) {
			this.string = string;
		}

		public static ClassName findClass(String string) {
			return stringMap.get(string);
		}
	}
}
