package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Inventory;
import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Senses {

	private final Integer perception;
	private final Integer investigation;
	private final Integer insight;
	private final Map<Vision, Integer[]> visions = new HashMap<>();

	public Senses(Skills skills) {
		this.perception = 10 + skills.getSkill(Skills.SkillName.PERCEPTION).modifier;
		this.investigation = 10 + skills.getSkill(Skills.SkillName.INVESTIGATION).modifier;
		this.insight = 10 + skills.getSkill(Skills.SkillName.INSIGHT).modifier;

		this.visions.put(Vision.BLINDSIGHT, new Integer[]{0, 0});
		this.visions.put(Vision.DARKVISION, new Integer[]{0, 0});
		this.visions.put(Vision.TRUESIGHT, new Integer[]{0, 0});
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Senses");
		itemBuilder.lore(senseDisplay("Perception", this.perception));
		itemBuilder.lore(senseDisplay("Investigation", this.investigation));
		itemBuilder.lore(senseDisplay("Insight", this.insight));
		itemBuilder.lore("Additional Senses:");
		if (this.visions.size() == 0) {
			itemBuilder.loreNone();
		} else {
			this.visions.forEach((vision, levels) -> {
				int totalLevel = levels[0] + levels[1];
				if (totalLevel == 0) return;
				itemBuilder.lore(senseDisplay("● " + vision.friendlyName, totalLevel).append(Component.text("ft.", NamedTextColor.GRAY)));
			});
		}
		return itemBuilder.build();
	}

	private Component senseDisplay(String text, int value) {
		Component a = Component.text(text + ": ", NamedTextColor.GRAY);
		Component b = Component.text(value, NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true);
		return a.append(b);
	}

	public void updateData(Modifier modifier, Inventory inventory) {
		if (modifier.requiresAttunement) {
			Inventory.Item item = inventory.getItemFromId(modifier.componentId);
			if (item == null) {
				Console.warn("Item with componentId " + modifier.componentId + " is not found");
				return;
			}
			if (!item.isAttuned) return;
		}

		Vision vision = Vision.valueOf(modifier.subType.toUpperCase());
		if (modifier.type == Modifier.ModifierType.SET_BASE) {
			this.visions.get(vision)[0] = modifier.value;
		} else if (modifier.type == Modifier.ModifierType.SENSE) {
			this.visions.get(vision)[1] += modifier.value;
		}
	}

	private enum Vision {
		BLINDSIGHT("Blindsight"),
		DARKVISION("Darkvision"),
		TRUESIGHT("Truesight");

		private final String friendlyName;

		Vision(String friendlyName) {
			this.friendlyName = friendlyName;
		}
	}
}
