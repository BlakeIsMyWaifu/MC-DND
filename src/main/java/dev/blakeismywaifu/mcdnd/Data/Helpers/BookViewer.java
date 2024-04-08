package dev.blakeismywaifu.mcdnd.Data.Helpers;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BookViewer {

	private final Map<BookType, ItemStack> books = new HashMap<>();

	public BookViewer() {
		Component tempPage = Component.text("Temp Book", NamedTextColor.BLUE);
		List<Component> tempBook = new LinkedList<>();
		tempBook.add(tempPage);
		this.books.put(BookType.ACTIONS, generateBook(tempBook));
		this.books.put(BookType.SPELLS, generateBook(tempBook));
		this.books.put(BookType.ITEMS, generateBook(tempBook));
		this.books.put(BookType.FEATS, generateBook(tempBook));
	}

	private ItemStack generateBook(List<Component> components) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		bookMeta.setAuthor("D&D");
		bookMeta.setTitle("D&D");
		components.forEach(bookMeta::addPages);
		book.setItemMeta(bookMeta);
		return book;
	}

	public ItemStack getBook(int slot) {
		BookType bookType = BookType.findBookFromSlot(slot);
		return this.books.get(bookType);
	}

	public ItemStack getInventoryItem(BookType bookType) {
		ItemBuilder itemBuilder = new ItemBuilder(bookType.friendlyName);
		itemBuilder.lore(Component.text("Click to open", NamedTextColor.GREEN));
		return itemBuilder.build();
	}

	public enum BookType {
		ACTIONS(32, "Actions"),
		SPELLS(33, "Spells"),
		ITEMS(34, "Items"),
		FEATS(35, "Feats");

		private static final Map<Integer, BookType> slotMap = new HashMap<>();

		static {
			for (BookType book : values()) {
				slotMap.put(book.slot, book);
			}
		}

		private final int slot;
		private final String friendlyName;

		BookType(int slot, String friendlyName) {
			this.slot = slot;
			this.friendlyName = friendlyName;
		}

		public static BookType findBookFromSlot(int slot) {
			return slotMap.get(slot);
		}
	}
}
