package dev.blakeismywaifu.mcdnd.Stats.Helpers;

import dev.blakeismywaifu.mcdnd.Stats.Skills;
import dev.blakeismywaifu.mcdnd.Stats.Stat;
import org.json.simple.JSONObject;

public class Skill {

	public Proficiency proficiency;
	public Stat.StatName stat;
	public Skills.SkillName skill;
	public Vantage vantage;
	public Long modifier;

	public Skill(Skills.SkillName skill, JSONObject json) {
		this.skill = skill;
		// TODO update proficiency
		this.proficiency = Proficiency.NOT;
		this.stat = skill.stat;
		// TODO update vantage
		this.vantage = Vantage.NONE;
		// TODO needs stats
		this.modifier = 2L;
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
