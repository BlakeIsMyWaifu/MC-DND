package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.DNDClass;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Stats.Stat;
import org.json.JSONObject;

import java.util.List;

public class HitPoints {

	private final Integer constitutionModifier;
	public Integer maxHitPoints;
	public Integer currentHitPoints;

	public HitPoints(JSONObject data, Stats stats, CharacterInfo characterInfo) {
		this.constitutionModifier = stats.getStat(Stat.StatName.CONSTITUTION).modifier;

		int totalHitPoints = 0;
		totalHitPoints += this.constitutionModifier;

		switch (characterInfo.classes.get(0).className) {
			case WIZARD, SORCERER -> totalHitPoints += 6;
			case ARTIFICER, BARD, CLERIC, DRUID, MONK, ROGUE, WARLOCK -> totalHitPoints += 8;
			case FIGHTER, PALADIN, RANGER -> totalHitPoints += 10;
			case BARBARIAN -> totalHitPoints += 12;
		}

		List<DNDClass> classes = characterInfo.classes;
		for (int i = 0; i < classes.size(); i++) {
			DNDClass dndClass = classes.get(i);
			totalHitPoints += calculateBonusHitPoints(dndClass, i == 0);
		}

		// TODO add temp health

		this.maxHitPoints = totalHitPoints;

		int removedHitPoints = data.getInt("removedHitPoints");
		this.currentHitPoints = Math.toIntExact(this.maxHitPoints - removedHitPoints);
	}

	private int calculateBonusHitPoints(DNDClass dndClass, boolean isPrimary) {
		int level = isPrimary ? dndClass.level - 1 : dndClass.level;
		int bonusHitPoints = this.constitutionModifier * level;

		switch (dndClass.className) {
			case WIZARD, SORCERER -> bonusHitPoints += 4 * level;
			case ARTIFICER, BARD, CLERIC, DRUID, MONK, ROGUE, WARLOCK -> bonusHitPoints += 5 * level;
			case FIGHTER, PALADIN, RANGER -> bonusHitPoints += 6 * level;
			case BARBARIAN -> bonusHitPoints += 7 * level;
		}

		return bonusHitPoints;
	}

	public void updateData(Modifier modifier, CharacterInfo characterInfo) {
		int bonusHitPoints = 0;

		switch (modifier.componentId) {
			// Tough Feat
			case 49 -> bonusHitPoints += 2 * characterInfo.totalLevel;

			// Hill Dwarf Racial
			case 122 -> bonusHitPoints += characterInfo.totalLevel;

			// Draconic Sorcerer Subclass
			case 377 -> {
				for (DNDClass dndClass : characterInfo.classes) {
					if (dndClass.className != DNDClass.ClassName.SORCERER) continue;
					bonusHitPoints += dndClass.level;
				}
			}
		}

		this.maxHitPoints += bonusHitPoints;
		this.currentHitPoints += bonusHitPoints;
	}
}
