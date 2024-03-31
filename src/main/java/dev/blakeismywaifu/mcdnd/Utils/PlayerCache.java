package dev.blakeismywaifu.mcdnd.Utils;

import dev.blakeismywaifu.mcdnd.Data.CharacterSheet;

import java.util.*;

public class PlayerCache {

	private final Map<UUID, CharacterSheet> cache = new HashMap<>();

	public List<UUID> listPlayers() {
		return new ArrayList<>(cache.keySet());
	}

	public void addPlayer(UUID playerId, String characterId) {
		CharacterSheet characterSheet = new CharacterSheet(playerId, characterId);
		cache.put(playerId, characterSheet);
	}

	public void removePlayer(UUID player) {
		this.cache.remove(player);
	}

	public CharacterSheet getPlayer(UUID playerId) {
		return this.cache.get(playerId);
	}
}
