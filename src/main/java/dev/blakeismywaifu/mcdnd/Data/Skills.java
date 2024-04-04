package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Inventory;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Stats.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Stream;

public class Skills {

	public final Map<SkillName, Skill> skills = new HashMap<>();

	public Skills(Stats stats, Inventory inventory, Proficiencies proficiencies) {
		for (SkillName skillName : SkillName.values()) {
			skills.put(skillName, new Skill(skillName, stats));
		}

		for (Inventory.Item item : inventory.getItems()) {
			if (item.definition.stealthCheck != null && item.definition.stealthCheck == 2) {
				skills.get(SkillName.STEALTH).vantage = Skill.Vantage.DISADVANTAGE;
			}
			if (item.equipped && Objects.equals(item.definition.filterType, "Armor")) {
				boolean isArmourProficient = proficiencies.isArmourProficient(item.definition.baseArmourName);
				if (!isArmourProficient) {
					skills.get(SkillName.ACROBATICS).vantage = Skill.Vantage.DISADVANTAGE;
					skills.get(SkillName.ATHLETICS).vantage = Skill.Vantage.DISADVANTAGE;
					skills.get(SkillName.SLEIGHT_OF_HAND).vantage = Skill.Vantage.DISADVANTAGE;
					skills.get(SkillName.STEALTH).vantage = Skill.Vantage.DISADVANTAGE;
				}
			}
		}
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Skills");
		itemBuilder.modelData(2);

		Stream.of(SkillName.values()).forEach(skillName -> {
			Skill skill = skills.get(skillName);

			Map<Skill.Proficiency, NamedTextColor> proficiencyColours = new HashMap<>();
			proficiencyColours.put(Skill.Proficiency.NOT, NamedTextColor.GRAY);
			proficiencyColours.put(Skill.Proficiency.HALF, NamedTextColor.YELLOW);
			proficiencyColours.put(Skill.Proficiency.PROFICIENT, NamedTextColor.GOLD);
			proficiencyColours.put(Skill.Proficiency.EXPERTISE, NamedTextColor.LIGHT_PURPLE);

			Map<Skill.Vantage, Component> vantageAdjustment = new HashMap<>();
			vantageAdjustment.put(Skill.Vantage.NONE, Component.text(" "));
			vantageAdjustment.put(Skill.Vantage.ADVANTAGE, Component.text(" A ", NamedTextColor.GREEN));
			vantageAdjustment.put(Skill.Vantage.DISADVANTAGE, Component.text(" D ", NamedTextColor.RED));

			boolean isModifierPositive = skill.modifier >= 0;
			itemBuilder.lore(
					Component.text("● " + skill.skill.name + (isModifierPositive ? " +" : " ") + skill.modifier, proficiencyColours.get(skill.proficiency))
							.append(vantageAdjustment.get(skill.vantage))
							.append(Component.text(skill.stat.shortHand, NamedTextColor.DARK_GRAY))
			);
		});

		return itemBuilder.build();
	}

	public void updateData(Modifier modifier, Stats stats, Miscellaneous miscellaneous) {
		switch (modifier.type) {
			case HALF_PROFICIENCY, PROFICIENCY, EXPERTISE -> proficiencyUpdate(modifier, stats, miscellaneous);
			case ADVANTAGE -> vantageUpdate(modifier, Skill.Vantage.ADVANTAGE);
		}
	}

	private void proficiencyUpdate(Modifier modifier, Stats stats, Miscellaneous miscellaneous) {
		if (SkillName.labelList.contains(modifier.subType)) {
			SkillName skillName = SkillName.findSkillName(modifier.subType);
			Skill skill = this.skills.get(skillName);
			Integer statModifier = stats.getStat(skill.stat).modifier;

			switch (modifier.type) {
				case HALF_PROFICIENCY -> {
					if (skill.proficiency != Skill.Proficiency.NOT) break;
					skill.proficiency = Skill.Proficiency.HALF;
					skill.modifier = statModifier + Math.floorDiv(miscellaneous.proficiency, 2);
				}
				case PROFICIENCY -> {
					if (skill.proficiency == Skill.Proficiency.EXPERTISE) break;
					skill.proficiency = Skill.Proficiency.PROFICIENT;
					skill.modifier = statModifier + miscellaneous.proficiency;
				}
				case EXPERTISE -> {
					skill.proficiency = Skill.Proficiency.EXPERTISE;
					skill.modifier = statModifier + (2 * miscellaneous.proficiency);
				}
			}
		} else if (Objects.equals(modifier.subType, "ability-checks")) {
			for (SkillName skillName : SkillName.values()) {
				Skill skill = this.skills.get(skillName);
				if (skill.proficiency != Skill.Proficiency.NOT) return;
				skill.proficiency = Skill.Proficiency.HALF;
				skill.modifier = stats.getStat(skill.stat).modifier + Math.floorDiv(miscellaneous.proficiency, 2);
			}
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void vantageUpdate(Modifier modifier, Skill.Vantage vantage) {
		if (modifier.subType.endsWith("-ability-checks")) {
			switch (modifier.subType) {
				case "strength-ability-checks" -> updateStatVantage(Stat.StatName.STRENGTH, vantage);
				case "dexterity-ability-checks" -> updateStatVantage(Stat.StatName.DEXTERITY, vantage);
				case "constitution-ability-checks" -> updateStatVantage(Stat.StatName.CONSTITUTION, vantage);
				case "intelligence-ability-checks" -> updateStatVantage(Stat.StatName.INTELLIGENCE, vantage);
				case "wisdom-ability-checks" -> updateStatVantage(Stat.StatName.WISDOM, vantage);
				case "charisma-ability-checks" -> updateStatVantage(Stat.StatName.CHARISMA, vantage);
			}
		} else if (SkillName.labelList.contains(modifier.subType)) {
			SkillName skillName = SkillName.findSkillName(modifier.subType);
			Skill skill = this.skills.get(skillName);
			skill.vantage = vantage;
		}
	}

	private void updateStatVantage(Stat.StatName statName, Skill.Vantage vantage) {
		skills.values().stream().filter(skill -> skill.stat == statName).forEach(skill -> skill.vantage = vantage);
	}

	public enum SkillName {
		ACROBATICS("acrobatics", "Acrobatics", Stat.StatName.DEXTERITY),
		ANIMAL_HANDLING("animal-handling", "Animal Handling", Stat.StatName.WISDOM),
		ARCANA("arcana", "Arcana", Stat.StatName.INTELLIGENCE),
		ATHLETICS("athletics", "Athletics", Stat.StatName.STRENGTH),
		DECEPTION("deception", "Deception", Stat.StatName.CHARISMA),
		HISTORY("history", "History", Stat.StatName.INTELLIGENCE),
		INSIGHT("insight", "Insight", Stat.StatName.WISDOM),
		INTIMIDATION("intimidation", "Intimidation", Stat.StatName.CHARISMA),
		INVESTIGATION("investigation", "Investigation", Stat.StatName.INTELLIGENCE),
		MEDICINE("medicine", "Medicine", Stat.StatName.WISDOM),
		NATURE("nature", "Nature", Stat.StatName.INTELLIGENCE),
		PERCEPTION("perception", "Perception", Stat.StatName.WISDOM),
		PERFORMANCE("performance", "Performance", Stat.StatName.CHARISMA),
		PERSUASION("persuasion", "Persuasion", Stat.StatName.CHARISMA),
		RELIGION("religion", "Religion", Stat.StatName.INTELLIGENCE),
		SLEIGHT_OF_HAND("sleight-of-hand", "Sleight of Hand", Stat.StatName.DEXTERITY),
		STEALTH("stealth", "Stealth", Stat.StatName.DEXTERITY),
		SURVIVAL("survival", "Survival", Stat.StatName.WISDOM);

		public static final List<String> labelList = new ArrayList<>();
		private static final Map<String, SkillName> labelMap = new HashMap<>();

		static {
			for (SkillName skillName : values()) {
				labelMap.put(skillName.label, skillName);
				labelList.add(skillName.label);
			}
		}

		public final String name;
		public final Stat.StatName stat;
		public final String label;

		SkillName(String label, String name, Stat.StatName stat) {
			this.name = name;
			this.label = label;
			this.stat = stat;
		}

		public static SkillName findSkillName(String label) {
			return labelMap.get(label);
		}
	}

	public static class Skill {

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
}
