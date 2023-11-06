package dev.blakeismywaifu.mcdnd.API;

import dev.blakeismywaifu.mcdnd.Data.PlayerCache;
import org.json.simple.JSONObject;

import java.util.UUID;

public class CharacterData {

	private final UUID playerId;
	private final String id;

	public CharacterData(UUID playerId, String id) {
		this.playerId = playerId;
		this.id = id;
	}

	public void getAndCacheData() {
		JSONObject data = new Fetch("https://character-service.dndbeyond.com/character/v5/character/" + this.id).getData();
		PlayerCache.putCache(this.playerId, data);
	}
}
