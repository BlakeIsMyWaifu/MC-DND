package dev.blakeismywaifu.mcdnd.Data.Books;

import dev.blakeismywaifu.mcdnd.Utils.ItemBuilder;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public abstract class BookBase {

	protected final JSONObject json;
	private final String bookTitle;
	private final List<Category> categories;
	private final Book book;

	protected BookBase(String title, JSONObject json) {
		this.bookTitle = title;
		this.json = json;
		this.categories = generateContent();
		this.book = generateBook();
	}

	public Book getBook() {
		return this.book;
	}

	protected abstract List<Category> generateContent();

	private Book generateBook() {
		Book.Builder bookBuilder = Book.builder()
				.author(Component.text("D&D"))
				.title(Component.text("D&D"));

		bookBuilder.addPage(generateIndexPage());
		this.categories.forEach(category -> category.getPages().forEach(bookBuilder::addPage));

		return bookBuilder.build();
	}

	private Component generateIndexPage() {
		Component page = Component.text(this.bookTitle).decoration(TextDecoration.BOLD, true)
				.appendNewline()
				.appendNewline();

		int nextPage = 2;
		for (Category category : this.categories) {

			Component bullet = Component.text("● ")
					.decoration(TextDecoration.BOLD, true)
					.decoration(TextDecoration.UNDERLINED, false);

			Component text = Component.text(category.getTitle())
					.color(NamedTextColor.DARK_BLUE)
					.decoration(TextDecoration.BOLD, false)
					.decoration(TextDecoration.UNDERLINED, true)
					.clickEvent(ClickEvent.changePage(nextPage))
					.appendNewline()
					.appendNewline();

			page = page.append(bullet.append(text));

			nextPage += category.getPageCount();
		}

		return page;
	}

	public ItemStack getItem() {
		ItemBuilder itemBuilder = new ItemBuilder(this.bookTitle);
		itemBuilder.lore(Component.text("Click to open", NamedTextColor.GREEN));
		return itemBuilder.build();
	}

	protected static class Category {

		private final String title;
		private final List<Component> pages;
		private final Integer pageCount;

		public Category(String title, List<String[]> contents) {
			this.title = title;

			List<Component> pages = new LinkedList<>();

			int lineNumber = 2;
			int currentPage = 0;

			Component out = Component.text(this.title)
					.decoration(TextDecoration.BOLD, true)
					.appendNewline()
					.appendNewline();
			pages.add(out);

			for (String[] content : contents) {
				if (content.length == 0) continue;

				String name = content[0];
				String description = content[1];
				assert name != null;
				assert description != null;

				Component line = Component.text("● " + name)
						.decoration(TextDecoration.BOLD, false)
						.hoverEvent(HoverEvent.showText(Component.text(description)));

				// TODO line size number should be updated once a resource pack has been made
				int extraLineAmount = Math.floorDiv(name.length(), 22);

				lineNumber += 1 + extraLineAmount;
				if (lineNumber > 14) {
					currentPage += 1;
					lineNumber = 1 + extraLineAmount;
				}

				if (pages.size() <= currentPage) {
					pages.add(line.appendNewline());
				} else {
					pages.set(currentPage, pages.get(currentPage).append(line).appendNewline());
				}
			}

			if (lineNumber < 13) {
				Component padding = Component.text("").appendNewline();
				while (lineNumber < 12) {
					padding = padding.appendNewline();
					lineNumber++;
				}

				Component backPadding = Component.text("       ")
						.decoration(TextDecoration.BOLD, false)
						.decoration(TextDecoration.UNDERLINED, false);
				Component backButton = Component.text("Index Page", NamedTextColor.DARK_BLUE)
						.decoration(TextDecoration.BOLD, false)
						.decoration(TextDecoration.UNDERLINED, true)
						.clickEvent(ClickEvent.changePage(0));

				Component section = padding.append(backPadding).append(backButton);
				pages.set(currentPage, pages.get(currentPage).append(section));
			}

			this.pages = pages;
			this.pageCount = currentPage + 1;
		}

		public String getTitle() {
			return title;
		}

		public List<Component> getPages() {
			return pages;
		}

		public Integer getPageCount() {
			return pageCount;
		}
	}
}
