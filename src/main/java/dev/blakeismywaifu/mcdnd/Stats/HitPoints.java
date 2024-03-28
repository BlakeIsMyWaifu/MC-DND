package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.DNDClass;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import org.json.simple.JSONObject;

import java.util.List;

public class HitPoints {

	private final Integer constitutionModifier;
	public Integer maxHitPoints;
	public Integer currentHitPoints;

	public HitPoints(JSONObject data, Stats stats, Character character) {
		this.constitutionModifier = stats.stats.get(Stat.StatName.CONSTITUTION).modifier;

		int totalHitPoints = 0;
		totalHitPoints += this.constitutionModifier;

		switch (character.classes.get(0).className) {
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

		List<DNDClass> classes = character.classes;
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

	public void updateData(Modifier modifier, CharacterData characterData) {
		int bonusHitPoints = 0;

		switch (modifier.componentId.intValue()) {
			case 49: // Tough Feat
				bonusHitPoints += 2 * characterData.character.totalLevel;
				break;
			case 122: // Hill Dwarf Racial
				bonusHitPoints += characterData.character.totalLevel;
				break;
			case 377: // Draconic Sorcerer Subclass
				for (DNDClass dndClass : characterData.character.classes) {
					if (dndClass.className != DNDClass.ClassName.SORCERER) continue;
					bonusHitPoints += dndClass.level;
				}
				break;
		}

		this.maxHitPoints += bonusHitPoints;
		this.currentHitPoints += bonusHitPoints;
	}
}
