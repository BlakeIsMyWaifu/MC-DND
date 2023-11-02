package dev.blakeismywaifu.mcdnd.Commands;

import dev.blakeismywaifu.mcdnd.Data.PlayerCache;
import dev.blakeismywaifu.mcdnd.Utils.CommandResponse;
import dev.blakeismywaifu.mcdnd.Utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Bind implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		// Error from empty arguments
		if (args.length == 0) {
			TextComponent textComponent = Component
					.text("Must specify a dndbeyond character id and a player\nor run ", NamedTextColor.RED)
					.append(Component.text("/bind view", NamedTextColor.WHITE))
					.append(Component.text(" to view binds", NamedTextColor.RED));
			CommandResponse.send(sender, textComponent);
			return false;
		}

		// View sub-command
		if (args[0].equalsIgnoreCase("view")) {
			Map<UUID, String> binds = PlayerCache.getBinds();

			ComponentBuilder<TextComponent, TextComponent.Builder> viewMessage = Component.text("\n[" + binds.size() + "] Bound Players: ", NamedTextColor.GREEN).toBuilder();
			binds.forEach((key, value) -> {
				HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text(value));
				String playerName = Objects.requireNonNull(Bukkit.getPlayer(key)).getName();
				Component name = Component.text(playerName + ", ").hoverEvent(hoverEvent);
				viewMessage.append(name);
			});
			sender.sendMessage(viewMessage);

			return true;
		}

		// Get player
		Player player = null;
		if (sender instanceof Player) player = (Player) sender;
		if (args.length >= 2) player = Bukkit.getPlayer(args[1]);

		// Error from unknown player
		if (player == null) {
			CommandResponse.error(sender, "Unknown Player");
			return false;
		}

		// Delete sub-command
		if (args[0].equalsIgnoreCase("delete")) {
			if (!PlayerCache.getBinds().containsKey(player.getUniqueId())) {
				CommandResponse.error(sender, "That player doesn't have a binding");
				return false;
			}

			PlayerCache.putBind(player.getUniqueId(), null);
			PlayerCache.putCache(player.getUniqueId(), null);

			player.getInventory().clear();
			Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
			player.setHealth(20);

			CommandResponse.success(sender, player.getName() + " has their binding remove");
			return true;
		}

		// Error from invalid character id
		if (!Utils.isInteger(args[0])) {
			CommandResponse.error(sender, "Character id must be an integer");
			return false;
		}

		PlayerCache.putBind(player.getUniqueId(), args[0]);

		CommandResponse.success(sender, "Successfully bound: " + player.getName() + " to " + args[0]);

		return true;
	}
}
