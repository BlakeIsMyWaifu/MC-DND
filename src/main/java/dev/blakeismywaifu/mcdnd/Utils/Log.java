package dev.blakeismywaifu.mcdnd.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class Log {

	private static final String prefix = "[dnd] ";

	public static void info(final String message) {
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + message);
	}

	public static void warn(final String message) {
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + "[WARN]" + message);
	}

	public static void error(final String message) {
		Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + "[ERROR]" + message);
	}
}
