package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Data.Stats.Stat;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SavingThrows {

	private final Map<Stat.StatName, SavingThrowData> savingThrows = new HashMap<>();
	private final Map<String, Modifiers.Vantage> additionModifiers = new TreeMap<>();

	public SavingThrows(Stats stats, Proficiencies proficiencies) {
		for (Stat.StatName statName : Stat.StatName.values()) {
			Stat stat = stats.getStat(statName);
			SavingThrowData savingThrowData = new SavingThrowData(stat);
			this.savingThrows.put(statName, savingThrowData);
		}

		if (proficiencies.isNotArmourProficient()) {
			additionModifiers.put("D - Strength Saving Throws", Modifiers.Vantage.DISADVANTAGE);
			additionModifiers.put("D - Dexterity Saving Throws", Modifiers.Vantage.DISADVANTAGE);
		}
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Saving Throw");
		if (this.additionModifiers.size() == 0) {
			itemBuilder.lore(Component.text("none", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
		} else {
			this.additionModifiers.forEach((additionModifier, vantage) -> {
				NamedTextColor colour = vantage == Modifiers.Vantage.ADVANTAGE ? NamedTextColor.GREEN : NamedTextColor.RED;
				itemBuilder.lore(Component.text(additionModifier, colour));
			});
		}
		return itemBuilder.build();
	}

	public ItemStack getItem(Stat.StatName statName) {
		SavingThrowData savingThrowData = this.savingThrows.get(statName);
		ItemBuilder itemBuilder = new ItemBuilder();
		NamedTextColor displayNameColour = savingThrowData.proficiency == Modifiers.Proficiency.PROFICIENT ? NamedTextColor.GOLD : NamedTextColor.WHITE;
		itemBuilder.displayName("Saving Throw +" + savingThrowData.modifier, displayNameColour);
		itemBuilder.lore(statName.friendlyName);
		return itemBuilder.build();
	}

	public void updateDate(Modifier modifier, Miscellaneous miscellaneous) {
		if (modifier.type == Modifier.ModifierType.PROFICIENCY) updateDataProficiency(modifier, miscellaneous);
		else if (modifier.type == Modifier.ModifierType.ADVANTAGE) updateDataAdvantage(modifier);
	}

	private void updateDataProficiency(Modifier modifier, Miscellaneous miscellaneous) {
		switch (modifier.subType) {
			case "strength-saving-throws" ->
					this.savingThrows.get(Stat.StatName.STRENGTH).addProficiency(miscellaneous);
			case "dexterity-saving-throws" ->
					this.savingThrows.get(Stat.StatName.DEXTERITY).addProficiency(miscellaneous);
			case "constitution-saving-throws" ->
					this.savingThrows.get(Stat.StatName.CONSTITUTION).addProficiency(miscellaneous);
			case "intelligence-saving-throws" ->
					this.savingThrows.get(Stat.StatName.INTELLIGENCE).addProficiency(miscellaneous);
			case "wisdom-saving-throws" -> this.savingThrows.get(Stat.StatName.WISDOM).addProficiency(miscellaneous);
			case "charisma-saving-throws" ->
					this.savingThrows.get(Stat.StatName.CHARISMA).addProficiency(miscellaneous);
		}
	}

	private void updateDataAdvantage(Modifier modifier) {
		String text = Objects.equals(modifier.subType, "saving-throws") ? modifier.restriction : modifier.friendlySubtypeName;
		this.additionModifiers.put("A - " + text, Modifiers.Vantage.ADVANTAGE);
	}

	private static class SavingThrowData {

		public final Stat.StatName statName;
		public Integer modifier;
		public Modifiers.Proficiency proficiency = Modifiers.Proficiency.NOT;

		public SavingThrowData(Stat stat) {
			this.statName = stat.statName;
			this.modifier = stat.modifier;
		}

		public void addProficiency(Miscellaneous miscellaneous) {
			this.modifier += miscellaneous.proficiency;
			this.proficiency = Modifiers.Proficiency.PROFICIENT;
		}
	}
}
