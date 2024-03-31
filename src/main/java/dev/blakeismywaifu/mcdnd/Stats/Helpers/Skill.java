package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import dev.blakeismywaifu.mcdnd.Stats.Skills;
import dev.blakeismywaifu.mcdnd.Stats.Stats;

public class Skill {

	public Proficiency proficiency = Proficiency.NOT;
	public Stat.StatName stat;
	public Skills.SkillName skill;
	public Vantage vantage = Vantage.NONE;
	public Integer modifier;

	public Skill(Skills.SkillName skill, Stats stats) {
		this.skill = skill;
		this.stat = skill.stat;
		// TODO update vantage
		this.modifier = stats.stats.get(skill.stat).modifier;
	}

	public enum Proficiency {
		NOT,
		HALF,
		PROFICIENT,
		EXPERTISE
	}

	public enum Vantage {
		ADVANTAGE,
		NONE,
		DISADVANTAGE,
	}
}
