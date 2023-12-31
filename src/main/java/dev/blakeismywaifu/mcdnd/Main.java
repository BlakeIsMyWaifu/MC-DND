package dev.blakeismywaifu.mcdnd;

import dev.blakeismywaifu.mcdnd.Commands.Bind;
import dev.blakeismywaifu.mcdnd.Data.PlayerCache;
import dev.blakeismywaifu.mcdnd.Tasks.FullUpdateAll;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

	public PlayerCache playerCache = new PlayerCache();

	@Override
	public void onEnable() {
		Console.info("Plugin Enabled");

		Map<String, CommandExecutor> commands = new HashMap<>();
		commands.put("bind", new Bind(this));
		commands.forEach((name, object) -> {
			PluginCommand pluginCommand = getCommand(name);
			if (pluginCommand != null) pluginCommand.setExecutor(object);
		});

		new FullUpdateAll(this).runTaskTimerAsynchronously(this, 0, 6000);
	}

	@Override
	public void onDisable() {
		Console.info("Plugin Disabled");
	}
}
