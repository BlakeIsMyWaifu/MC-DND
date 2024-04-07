package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Data.Helpers.Modifiers.Modifier;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventory {

	private final List<Item> items = new ArrayList<>();

	public Inventory(JSONObject json) {
		JSONArray inventoryJson = json.getJSONArray("inventory");
		inventoryJson.forEach(item -> items.add(new Item((JSONObject) item)));
	}

	public List<Item> getItems() {
		return items;
	}

	public @Nullable Item getItemFromId(Integer itemId) {
		return this.items.stream().filter(item -> Objects.equals(item.definition.id, itemId)).findFirst().orElse(null);
	}

	public static class Item {

		public final Integer id;
		public final Integer entityTypeId;
		public final ItemDefinition definition;
		public final Integer definitionId;
		public final Integer definitionTypeId;
		// displayAsAttack
		public final Integer quantity;
		public final Boolean isAttuned;
		public final Boolean equipped;
		public final @Nullable Integer equippedEntityTypeId;
		public final @Nullable Integer equippedEntityId;
		public final Integer chargesUsed;
		// limitedUse
		public final Integer containerEntityId;
		public final Integer containerEntityTypeId;
		public final String containerDefinitionKey;
		// currency

		public Item(JSONObject json) {
			this.id = json.getInt("id");
			this.entityTypeId = json.getInt("entityTypeId");
			JSONObject definition = json.getJSONObject("definition");
			this.definition = new ItemDefinition(definition);
			this.definitionId = json.getInt("definitionId");
			this.definitionTypeId = json.getInt("definitionTypeId");
			this.quantity = json.getInt("quantity");
			this.isAttuned = json.getBoolean("isAttuned");
			this.equipped = json.getBoolean("equipped");
			this.equippedEntityTypeId = json.isNull("equippedEntityTypeId") ? null : json.getInt("equippedEntityTypeId");
			this.equippedEntityId = json.isNull("equippedEntityId") ? null : json.getInt("equippedEntityId");
			this.chargesUsed = json.getInt("chargesUsed");
			this.containerEntityId = json.getInt("containerEntityId");
			this.containerEntityTypeId = json.getInt("containerEntityTypeId");
			this.containerDefinitionKey = json.getString("containerDefinitionKey");
		}
	}

	public static class ItemDefinition {

		public final Integer id;
		public final Integer baseTypeId;
		public final Integer entityTypeId;
		public final String definitionKey;
		public final Boolean canEquip;
		public final Boolean magic;
		public final String name;
		public final @Nullable String snippet;
		public final Integer weight;
		public final Integer weightMultiplier;
		public final @Nullable String capacity;
		public final Integer capacityWeight;
		public final @Nullable String type;
		public final String description;
		public final Boolean canAttune;
		public final @Nullable String attunementDescription;
		public final String rarity;
		public final Boolean isHomebrew;
		// version
		// sourceId
		// sourcePageNumber
		public final Boolean stackable;
		public final Integer bundleSize;
		public final @Nullable String avatarUrl;
		public final @Nullable String largeAvatarUrl;
		public final String filterType;
		public final @Nullable Float cost;
		public final Boolean isPack;
		public final JSONArray tags;
		public final List<Modifier> grantedModifiers = new ArrayList<>();
		public final String subType;
		public final Boolean isConsumable;
		public final JSONArray weaponBehaviors;
		public final @Nullable Integer baseItemId;
		public final @Nullable String baseArmourName;
		public final @Nullable Integer strengthRequirement;
		public final @Nullable Integer armourClass;
		public final @Nullable Integer stealthCheck;
		public final @Nullable JSONObject damage;
		public final @Nullable String damageType;
		// fixedDamage
		// properties
		public final @Nullable Integer attackType;
		public final @Nullable Integer categoryId;
		public final @Nullable Integer range;
		public final @Nullable Integer longRange;
		public final Boolean isMonkWeapon;
		public final @Nullable Integer levelInfusionGranted;
		public final JSONArray sources;
		public final @Nullable Integer armourTypeId;
		public final @Nullable Integer gearTypeId;
		public final @Nullable Integer groupedId;
		public final Boolean canBeAddedToInventory;
		public final Boolean isContainer;
		public final Boolean isCustomItem;

		public ItemDefinition(JSONObject json) {
			this.id = json.getInt("id");
			this.baseTypeId = json.getInt("baseTypeId");
			this.entityTypeId = json.getInt("entityTypeId");
			this.definitionKey = json.getString("definitionKey");
			this.canEquip = json.getBoolean("canEquip");
			this.magic = json.getBoolean("magic");
			this.name = json.getString("name");
			this.snippet = json.isNull("snippet") ? null : json.getString("snippet");
			this.weight = json.getInt("weight");
			this.weightMultiplier = json.getInt("weightMultiplier");
			this.capacity = json.isNull("capacity") ? null : json.getString("capacity");
			this.capacityWeight = json.getInt("capacityWeight");
			this.type = json.isNull("type") ? null : json.getString("type");
			this.description = json.getString("description");
			this.canAttune = json.getBoolean("canAttune");
			this.attunementDescription = json.isNull("attunementDescription") ? null : json.getString("attunementDescription");
			this.rarity = json.getString("rarity");
			this.isHomebrew = json.getBoolean("isHomebrew");
			this.stackable = json.getBoolean("stackable");
			this.bundleSize = json.getInt("bundleSize");
			this.avatarUrl = json.isNull("avatarUrl") ? null : json.getString("avatarUrl");
			this.largeAvatarUrl = json.isNull("largeAvatarUrl") ? null : json.getString("largeAvatarUrl");
			this.filterType = json.getString("filterType");
			this.cost = json.isNull("cost") ? null : json.getFloat("cost");
			this.isPack = json.getBoolean("isPack");
			this.tags = json.getJSONArray("tags");
			JSONArray grantedModifiers = json.getJSONArray("grantedModifiers");
			grantedModifiers.forEach(modifier -> this.grantedModifiers.add(new Modifier((JSONObject) modifier)));
			this.subType = json.isNull("subType") ? null : json.getString("subType");
			this.isConsumable = json.getBoolean("isConsumable");
			this.weaponBehaviors = json.getJSONArray("weaponBehaviors");
			this.baseItemId = json.isNull("baseItemId") ? null : json.getInt("baseItemId");
			this.baseArmourName = json.isNull("baseArmorName") ? null : json.getString("baseArmorName");
			this.strengthRequirement = json.isNull("strengthRequirement") ? null : json.getInt("strengthRequirement");
			this.armourClass = json.isNull("armorClass") ? null : json.getInt("armorClass");
			this.stealthCheck = json.isNull("stealthCheck") ? null : json.getInt("stealthCheck");
			this.damage = json.isNull("damage") ? null : json.getJSONObject("damage");
			this.damageType = json.isNull("damageType") ? null : json.getString("damageType");
			this.attackType = json.isNull("attackType") ? null : json.getInt("attackType");
			this.categoryId = json.isNull("categoryId") ? null : json.getInt("categoryId");
			this.range = json.isNull("range") ? null : json.getInt("range");
			this.longRange = json.isNull("longRange") ? null : json.getInt("longRange");
			this.isMonkWeapon = json.getBoolean("isMonkWeapon");
			this.levelInfusionGranted = json.isNull("levelInfusionGranted") ? null : json.getInt("levelInfusionGranted");
			this.sources = json.getJSONArray("sources");
			this.armourTypeId = json.isNull("armorTypeId") ? null : json.getInt("armorTypeId");
			this.gearTypeId = json.isNull("gearTypeId") ? null : json.getInt("gearTypeId");
			this.groupedId = json.isNull("groupedId") ? null : json.getInt("groupedId");
			this.canBeAddedToInventory = json.getBoolean("canBeAddedToInventory");
			this.isContainer = json.getBoolean("isContainer");
			this.isCustomItem = json.getBoolean("isCustomItem");
		}
	}
}
