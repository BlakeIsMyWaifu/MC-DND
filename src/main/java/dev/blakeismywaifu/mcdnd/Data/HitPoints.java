package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.DNDClass;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Stat;
import org.json.simple.JSONObject;

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
			case WIZARD:
			case SORCERER:
				totalHitPoints += 6;
				break;
			case ARTIFICER:
			case BARD:
			case CLERIC:
			case DRUID:
			case MONK:
			case ROGUE:
			case WARLOCK:
				totalHitPoints += 8;
				break;
			case FIGHTER:
			case PALADIN:
			case RANGER:
				totalHitPoints += 10;
				break;
			case BARBARIAN:
				totalHitPoints += 12;
				break;
		}

		List<DNDClass> classes = characterInfo.classes;
		for (int i = 0; i < classes.size(); i++) {
			DNDClass dndClass = classes.get(i);
			totalHitPoints += calculateBonusHitPoints(dndClass, i == 0);
		}

		// TODO add temp health

		this.maxHitPoints = totalHitPoints;

		Long removedHitPoints = (Long) data.get("removedHitPoints");
		this.currentHitPoints = Math.toIntExact(this.maxHitPoints - removedHitPoints);
	}

	private int calculateBonusHitPoints(DNDClass dndClass, boolean isPrimary) {
		int level = isPrimary ? dndClass.level - 1 : dndClass.level;
		int bonusHitPoints = this.constitutionModifier * level;

		switch (dndClass.className) {
			case WIZARD:
			case SORCERER:
				bonusHitPoints += 4 * level;
				break;
			case ARTIFICER:
			case BARD:
			case CLERIC:
			case DRUID:
			case MONK:
			case ROGUE:
			case WARLOCK:
				bonusHitPoints += 5 * level;
				break;
			case FIGHTER:
			case PALADIN:
			case RANGER:
				bonusHitPoints += 6 * level;
				break;
			case BARBARIAN:
				bonusHitPoints += 7 * level;
				break;
		}

		return bonusHitPoints;
	}

	public void updateData(Modifier modifier, CharacterInfo characterInfo) {
		int bonusHitPoints = 0;

		switch (modifier.componentId) {
			case 49: // Tough Feat
				bonusHitPoints += 2 * characterInfo.totalLevel;
				break;
			case 122: // Hill Dwarf Racial
				bonusHitPoints += characterInfo.totalLevel;
				break;
			case 377: // Draconic Sorcerer Subclass
				for (DNDClass dndClass : characterInfo.classes) {
					if (dndClass.className != DNDClass.ClassName.SORCERER) continue;
					bonusHitPoints += dndClass.level;
				}
				break;
		}

		this.maxHitPoints += bonusHitPoints;
		this.currentHitPoints += bonusHitPoints;
	}
}
