package dev.blakeismywaifu.mcdnd.Events;

import dev.blakeismywaifu.mcdnd.Data.CharacterSheet;
import dev.blakeismywaifu.mcdnd.Main;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import dev.blakeismywaifu.mcdnd.Utils.TestUtils;
import net.kyori.adventure.inventory.Book;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InventoryEvents implements Listener {

	private final Main main;

	public InventoryEvents(Main main) {
		this.main = main;
	}

	@EventHandler
	public void dragEvent(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (TestUtils.isNotDNDPlayer(player)) return;
		event.setCancelled(true);
		player.updateInventory();
	}

	@EventHandler
	public void clickEvent(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (TestUtils.isNotDNDPlayer(player)) return;

		handleBookItemClick(player, event);

		event.setCancelled(true);
		player.updateInventory();
	}

	@EventHandler
	public void dropEvent(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (TestUtils.isNotDNDPlayer(player)) return;
		event.setCancelled(true);
		player.updateInventory();
	}

	@EventHandler
	public void swapEvent(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		if (TestUtils.isNotDNDPlayer(player)) return;
		event.setCancelled(true);
		player.updateInventory();
	}

	private void handleBookItemClick(Player player, InventoryClickEvent event) {
		int slot = event.getSlot();
		Range range = new Range(32, 35);
		if (range.contains(slot)) {
			CharacterSheet characterSheet = main.playerCache.getPlayer(player.getUniqueId());
			switch (slot) {
				case 32 -> Console.warn("Missing actions book");
				case 33 -> Console.warn("Missing spells book");
				case 34 -> Console.warn("Missing items book");
				case 35 -> {
					Book book = characterSheet.feats.getBook();
					player.openBook(book);
				}
			}
		}
	}
}
