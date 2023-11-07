package dev.blakeismywaifu.mcdnd.Commands;

import dev.blakeismywaifu.mcdnd.Data.CharacterData;
import dev.blakeismywaifu.mcdnd.Data.PlayerCache;
import dev.blakeismywaifu.mcdnd.Main;
import dev.blakeismywaifu.mcdnd.Tasks.UpdatePlayer;
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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Bind implements CommandExecutor {

	private final Main main;
	private final PlayerCache playerCache;

	public Bind(Main main) {
		this.main = main;
		this.playerCache = main.playerCache;
	}

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
			List<UUID> playerIds = this.playerCache.listPlayers();

			ComponentBuilder<TextComponent, TextComponent.Builder> viewMessage = Component.text("\n[" + playerIds.size() + "] Bound Players: ", NamedTextColor.GREEN).toBuilder();
			playerIds.forEach(playerId -> {
				CharacterData characterData = this.playerCache.getPlayer(playerId);
				HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text(characterData.characterId));
				String playerName = Objects.requireNonNull(Bukkit.getPlayer(playerId)).getName();
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
			if (!this.playerCache.listPlayers().contains(player.getUniqueId())) {
				CommandResponse.error(sender, "That player doesn't have a binding");
				return false;
			}

			this.playerCache.removePlayer(player.getUniqueId());

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

		this.playerCache.addPlayer(player.getUniqueId(), args[0]);
		new UpdatePlayer(this.playerCache.getPlayer(player.getUniqueId())).runTaskAsynchronously(this.main);

		CommandResponse.success(sender, "Successfully bound: " + player.getName() + " to " + args[0]);

		return true;
	}
}
