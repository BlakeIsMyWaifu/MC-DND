package dev.blakeismywaifu.mcdnd.Data.Books;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.json.JSONObject;

import java.util.*;

public abstract class BookBase {

	protected final JSONObject json;
	private final String bookTitle;
	private final Book book;

	protected BookBase(String title, JSONObject json) {
		this.bookTitle = title;
		this.json = json;
		this.book = generateBook();
	}

	public Book getBook() {
		return this.book;
	}

	protected abstract Map<String, List<String>> generateContent();

	private Book generateBook() {
		Map<String, List<String>> content = generateContent();
		Map<String, Pair<List<Component>, Integer>> infoPages = generateInfoPages(content);

		List<Component> pages = new LinkedList<>();
		pages.add(generateIndexPage(infoPages));
		infoPages.forEach((a, b) -> pages.addAll(b.getValue0()));

		Book.Builder bookBuilder = Book.builder()
				.author(Component.text("D&D"))
				.title(Component.text("D&D"));
		pages.forEach(bookBuilder::addPage);
		return bookBuilder.build();
	}

	private Component generateIndexPage(Map<String, Pair<List<Component>, Integer>> content) {
		Component page = Component.text(this.bookTitle).decoration(TextDecoration.BOLD, true)
				.appendNewline()
				.appendNewline();

		int nextPage = 2;
		for (Map.Entry<String, Pair<List<Component>, Integer>> entry : content.entrySet()) {
			String link = entry.getKey();

			Component bullet = Component.text("● ")
					.decoration(TextDecoration.BOLD, true)
					.decoration(TextDecoration.UNDERLINED, false);
			Component text = Component.text(link)
					.color(NamedTextColor.DARK_BLUE)
					.decoration(TextDecoration.BOLD, false)
					.decoration(TextDecoration.UNDERLINED, true)
					.clickEvent(ClickEvent.changePage(nextPage))
					.appendNewline()
					.appendNewline();
			page = page.append(bullet.append(text));

			int pagesAmount = entry.getValue().getValue1();
			nextPage += pagesAmount;
		}

		return page;
	}

	private Map<String, Pair<List<Component>, Integer>> generateInfoPages(Map<String, List<String>> contents) {
		Map<String, Pair<List<Component>, Integer>> out = new LinkedHashMap<>();
		contents.forEach((key, value) -> {
			value.removeIf(Objects::isNull);
			out.put(key, generateCategoryPages(key, value));
		});
		return out;
	}

	private Pair<List<Component>, Integer> generateCategoryPages(String title, List<String> contents) {
		List<Component> pages = new LinkedList<>();

		int lineNumber = 3;
		int currentPage = 0;

		Component out = Component.text(title)
				.decoration(TextDecoration.BOLD, true)
				.appendNewline()
				.appendNewline();
		pages.add(out);

		for (String content : contents) {
			Component line = Component.text("● " + content).decoration(TextDecoration.BOLD, false);

			// TODO line size number should be updated once a resource pack has been made
			int extraLineAmount = Math.floorDiv(content.length(), 22);

			lineNumber += 1 + extraLineAmount;
			if (lineNumber > 14) {
				currentPage += 1;
				lineNumber = 0;
			}

			if (pages.size() <= currentPage) {
				pages.add(line.appendNewline());
			} else {
				pages.set(currentPage, pages.get(currentPage).append(line).appendNewline());
			}
		}

		return new Pair<>(pages, currentPage + 1);
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder(this.bookTitle);
		itemBuilder.lore(Component.text("Click to open", NamedTextColor.GREEN));
		return itemBuilder.build();
	}
}
