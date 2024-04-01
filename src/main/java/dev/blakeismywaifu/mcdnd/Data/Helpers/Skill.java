package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Data.Skills;
import dev.blakeismywaifu.mcdnd.Data.Stats;

public class Skill {

	public final Stat.StatName stat;
	public final Skills.SkillName skill;
	public Proficiency proficiency = Proficiency.NOT;
	public Vantage vantage = Vantage.NONE;
	public Integer modifier;

	public Skill(Skills.SkillName skill, Stats stats) {
		this.skill = skill;
		this.stat = skill.stat;
		// TODO add restrictions to vantage
		// TODO add disadvantage when not proficient with armour worn
		this.modifier = stats.getStat(skill.stat).modifier;
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
