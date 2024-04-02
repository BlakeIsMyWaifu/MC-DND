package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifier;
import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Defences {

	private final Map<Type, Set<String>> defences = new HashMap<>();

	public Defences() {
		this.defences.put(Type.RESISTANCE, new HashSet<>());
		this.defences.put(Type.IMMUNITY, new HashSet<>());
		this.defences.put(Type.VULNERABILITY, new HashSet<>());
	}

	public void updateData(Modifier modifier) {
		// TODO add vulnerability
		switch (modifier.type) {
			case RESISTANCE:
				addDefence(Type.RESISTANCE, modifier.friendlySubtypeName);
				break;
			case IMMUNITY:
				addDefence(Type.IMMUNITY, modifier.friendlySubtypeName);
				break;
		}
	}

	private void addDefence(Type type, String defence) {
		this.defences.get(type).add(defence);
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder("Defences");
		itemBuilder.lore(test("Resistance", Type.RESISTANCE, NamedTextColor.GREEN));
		itemBuilder.lore(test("Immunity", Type.IMMUNITY, NamedTextColor.GREEN));
		itemBuilder.lore(test("Vulnerability", Type.VULNERABILITY, NamedTextColor.RED));
		return itemBuilder.build();
	}

	private List<Component> test(String label, Type type, NamedTextColor colour) {
		Set<String> defence = this.defences.get(type);
		List<Component> lore = new ArrayList<>();

		lore.add(Component.text(label + ": ", colour));

		if (defence.size() == 0) {
			lore.add(Component.text("none", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true));
		} else {
			defence.forEach(d -> lore.add(Component.text("● " + d, NamedTextColor.GRAY)));
		}

		return lore;
	}

	public enum Type {
		RESISTANCE,
		IMMUNITY,
		VULNERABILITY
	}
}
