package dev.blakeismywaifu.mcdnd.Data;

import java.util.*;

public class PlayerCache {

	private final Map<UUID, CharacterData> cache = new HashMap<>();

	public List<UUID> listPlayers() {
		return new ArrayList<>(cache.keySet());
	}

	public void addPlayer(UUID playerId, String characterId) {
		CharacterData characterData = new CharacterData(playerId, characterId);
		cache.put(playerId, characterData);
	}

	public void removePlayer(UUID player) {
		this.cache.remove(player);
	}

	public CharacterData getPlayer(UUID playerId) {
		return this.cache.get(playerId);
	}
}
