package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import org.json.simple.JSONObject;

public class HitPoints {

	public Integer maxHitPoints;
	public Integer currentHitPoints;

	public HitPoints(JSONObject data, CharacterData characterData) {
		int constitutionModifier = characterData.stats.stats.get(Stat.StatName.CONSTITUTION).modifier;
		int baseHitPoints = constitutionModifier;
		switch (characterData.character.classes) {
			case WIZARD:
			case SORCERER:
				baseHitPoints += 6;
				break;
			case ARTIFICER:
			case BARD:
			case CLERIC:
			case DRUID:
			case MONK:
			case ROGUE:
			case WARLOCK:
				baseHitPoints += 8;
				break;
			case FIGHTER:
			case PALADIN:
			case RANGER:
				baseHitPoints += 10;
				break;
			case BARBARIAN:
				baseHitPoints += 12;
				break;
		}

		int level = characterData.character.level - 1;
		int bonusHitPoints = constitutionModifier * level;
		switch (characterData.character.classes) {
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

		// TODO add multi-classing
		// TODO add temp health

		this.maxHitPoints = baseHitPoints + bonusHitPoints;

		Long removedHitPoints = (Long) data.get("removedHitPoints");
		this.currentHitPoints = Math.toIntExact(this.maxHitPoints - removedHitPoints);
	}
}
