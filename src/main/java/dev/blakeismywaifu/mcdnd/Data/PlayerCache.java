package dev.blakeismywaifu.mcdnd.Data;

import com.google.gson.JsonObject;
import dev.blakeismywaifu.mcdnd.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

	public UUID uuid;
	public String characterId;
	public JsonObject cache;

	public PlayerCache(UUID uuid) {
		this.uuid = uuid;
	}

	private static void createCache(UUID uuid) {
		if (!Main.cache.containsKey(uuid)) Main.cache.put(uuid, new PlayerCache(uuid));
	}

	public static Map<UUID, String> getBinds() {
		Map<UUID, String> out = new HashMap<>();
		Main.cache.forEach((key, value) -> {
			if (value.characterId == null) return;
			out.put(key, value.characterId);
		});
		return out;
	}

	public static void putBind(UUID uuid, String characterId) {
		createCache(uuid);
		Main.cache.get(uuid).characterId = characterId;
	}

	public static Map<UUID, JsonObject> getCache() {
		Map<UUID, JsonObject> out = new HashMap<>();
		Main.cache.forEach((key, value) -> {
			if (value.cache == null) return;
			out.put(key, value.cache);
		});
		return out;
	}

	public static void putCache(UUID uuid, JsonObject cache) {
		createCache(uuid);
		Main.cache.get(uuid).cache = cache;
	}
}
