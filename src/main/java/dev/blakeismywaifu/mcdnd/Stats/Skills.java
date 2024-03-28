package dev.blakeismywaifu.mcdnd.Stats;

import dev.blakeismywaifu.mcdnd.Stats.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Skill;
import dev.blakeismywaifu.mcdnd.Stats.Helpers.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Skills {

	public Map<SkillName, Skill> skills = new HashMap<>();

	public Skills(JSONObject json) {
		for (SkillName skillName : SkillName.values()) {
			skills.put(skillName, new Skill(skillName, json));
		}
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Skills: ");
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

			itemBuilder.lore(
					Component.text("● " + skill.skill.name + " +" + skill.modifier, proficiencyColours.get(skill.proficiency))
							.append(vantageAdjustment.get(skill.vantage))
							.append(Component.text(skill.stat.shortHand, NamedTextColor.DARK_GRAY))
			);
		});

		return itemBuilder.build();
	}

	public void updateData(Modifier modifier) {
		if (Skills.SkillName.labelList.contains(modifier.subType)) {
			Skills.SkillName skillName = Skills.SkillName.findSkillName(modifier.subType);
			Skill skill = this.skills.get(skillName);
			skill.proficiency = Skill.Proficiency.PROFICIENT;
		}
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
}
