package dev.blakeismywaifu.mcdnd.Utils;

import dev.blakeismywaifu.mcdnd.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;

public class ItemBuilder {

	private final ArrayList<String> lore = new ArrayList<>();
	private final ArrayList<Component> loreComponent = new ArrayList<>();
	private ItemStack item;

	public ItemBuilder(String name) {
		create(Material.PHANTOM_MEMBRANE, name);
	}

	public ItemBuilder(String name, Material material) {
		create(material, name);
	}

	public ItemStack build() {
		return this.item;
	}

	private void create(Material material, String name) {
		this.item = new ItemStack(material);
		displayName(name, NamedTextColor.WHITE);
	}

	public void displayName(String name, NamedTextColor colour) {
		ItemMeta meta = this.item.getItemMeta();
		Component formattedName = Component.text(name, colour).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
		meta.displayName(formattedName);
		this.item.setItemMeta(meta);
	}

	public void lore(List<Component> lore) {
		ItemMeta meta = this.item.getItemMeta();
		List<Component> formattedLore = new ArrayList<>();
		lore.forEach(line -> formattedLore.add(line.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
		meta.lore(formattedLore);
		this.item.setItemMeta(meta);
	}

	public void lore(Component lore) {
		this.loreComponent.add(lore);
		lore(this.loreComponent);
	}

	public void lore(String lore) {
		this.loreComponent.add(Component.text(lore, NamedTextColor.GRAY));
		lore(this.loreComponent);
	}

	public void enchantments(Map<Enchantment, Integer> enchantments) {
		ItemMeta meta = this.item.getItemMeta();
		Iterator<Map.Entry<Enchantment, Integer>> it = enchantments.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Enchantment, Integer> pair = it.next();
			Enchantment enchantment = pair.getKey();
			Integer level = pair.getValue();
			meta.addEnchant(enchantment, level, true);
			it.remove();
		}
		this.item.setItemMeta(meta);
	}

	public void attributes(Map<Attribute, AttributeModifier> attributes) {
		ItemMeta meta = this.item.getItemMeta();
		Iterator<Map.Entry<Attribute, AttributeModifier>> it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Attribute, AttributeModifier> pair = it.next();
			Attribute attribute = pair.getKey();
			AttributeModifier modifier = pair.getValue();
			meta.addAttributeModifier(attribute, modifier);
			it.remove();
		}
		this.item.setItemMeta(meta);
	}

	public void unbreakable() {
		ItemMeta meta = this.item.getItemMeta();
		meta.setUnbreakable(true);
		this.item.setItemMeta(meta);
	}

	public void colour(int colour) {
		LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
		meta.setColor(Color.fromRGB(colour));
		this.item.setItemMeta(meta);
	}

	public void recipe(String recipeName, Map<Character, ItemStack> ingredients, String... pattern) {
		Plugin plugin = Main.getPlugin(Main.class);
		NamespacedKey namespacedKey = new NamespacedKey(plugin, recipeName);

		ShapedRecipe recipe = new ShapedRecipe(namespacedKey, this.item);
		recipe.shape(pattern);

		Iterator<Map.Entry<Character, ItemStack>> ingredient = ingredients.entrySet().iterator();
		while (ingredient.hasNext()) {
			Map.Entry<Character, ItemStack> pair = ingredient.next();
			recipe.setIngredient(pair.getKey(), pair.getValue());
			ingredient.remove();
		}

		getServer().addRecipe(recipe);
	}

	public void modelData(Integer modelData) {
		ItemMeta meta = this.item.getItemMeta();
		meta.setCustomModelData(modelData);
		this.item.setItemMeta(meta);
	}
}
