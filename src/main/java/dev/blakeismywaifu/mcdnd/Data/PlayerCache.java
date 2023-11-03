package dev.blakeismywaifu.mcdnd.Data;

import dev.blakeismywaifu.mcdnd.Main;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {

	public UUID uuid;
	public String characterId;
	public JSONObject cache;

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

	public static Map<UUID, JSONObject> getCache() {
		Map<UUID, JSONObject> out = new HashMap<>();
		Main.cache.forEach((key, value) -> {
			if (value.cache == null) return;
			out.put(key, value.cache);
		});
		return out;
	}

	public static void putCache(UUID uuid, JSONObject cache) {
		createCache(uuid);
		Main.cache.get(uuid).cache = cache;
	}
}
