package dev.blakeismywaifu.mcdnd.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

public final class Console {

	private static final String prefix = "[dnd] ";

	public static void info(final String message) {
		Bukkit.getConsoleSender().sendMessage(Component.text(prefix, NamedTextColor.WHITE).append(Component.text(message, NamedTextColor.WHITE)));
	}

	public static void success(final String message) {
		Bukkit.getConsoleSender().sendMessage(Component.text(prefix, NamedTextColor.WHITE).append(Component.text(message, NamedTextColor.GREEN)));
	}

	public static void warn(final String message) {
		Bukkit.getConsoleSender().sendMessage(Component.text(prefix + "[WARN] ", NamedTextColor.WHITE).append(Component.text(message, NamedTextColor.YELLOW)));
	}

	public static void error(final String message) {
		Bukkit.getConsoleSender().sendMessage(Component.text(prefix + "[ERROR] ", NamedTextColor.WHITE).append(Component.text(message, NamedTextColor.RED)));
	}
}
