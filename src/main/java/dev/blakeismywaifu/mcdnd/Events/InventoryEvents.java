package dev.blakeismywaifu.mcdnd.Events;

import dev.blakeismywaifu.mcdnd.Data.CharacterSheet;
import dev.blakeismywaifu.mcdnd.Main;
import dev.blakeismywaifu.mcdnd.Utils.Range;
import dev.blakeismywaifu.mcdnd.Utils.TestUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

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

		int slot = event.getSlot();
		Range range = new Range(32, 35);
		if (range.contains(slot)) {
			CharacterSheet characterSheet = main.playerCache.getPlayer(player.getUniqueId());
			ItemStack book = characterSheet.bookViewer.getBook(slot);
			player.openBook(book);
		}

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
}
