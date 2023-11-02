package dev.blakeismywaifu.mcdnd.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class CommandResponse {

	public static void success(final CommandSender sender, final String message) {
		TextComponent textComponent = Component.text("\n" + message, NamedTextColor.GREEN);
		sender.sendMessage(textComponent);
	}

	public static void error(final CommandSender sender, final String message) {
		TextComponent textComponent = Component.text("\n" + message, NamedTextColor.RED);
		sender.sendMessage(textComponent);
	}

	public static void send(final CommandSender sender, final TextComponent message) {
		TextComponent textComponent = Component.text("\n").append(message);
		sender.sendMessage(textComponent);
	}
}
