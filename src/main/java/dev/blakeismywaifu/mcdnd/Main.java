package dev.blakeismywaifu.mcdnd;

import dev.blakeismywaifu.mcdnd.Commands.Bind;
import dev.blakeismywaifu.mcdnd.Events.InventoryEvents;
import dev.blakeismywaifu.mcdnd.Tasks.FullUpdateAll;
import dev.blakeismywaifu.mcdnd.Tasks.QuickUpdateAll;
import dev.blakeismywaifu.mcdnd.Utils.Console;
import dev.blakeismywaifu.mcdnd.Utils.PlayerCache;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

	public final PlayerCache playerCache = new PlayerCache();

	@Override
	public void onEnable() {
		Console.info("Plugin Enabled");

		loadCommands();
		loadEvents();
		loadTasks();
	}

	@Override
	public void onDisable() {
		Console.info("Plugin Disabled");
	}

	private void loadCommands() {
		Map<String, CommandExecutor> commands = new HashMap<>();
		commands.put("bind", new Bind(this));
		commands.forEach((name, object) -> {
			PluginCommand pluginCommand = getCommand(name);
			if (pluginCommand != null) pluginCommand.setExecutor(object);
		});
	}

	private void loadEvents() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new InventoryEvents(this), this);
	}

	private void loadTasks() {
		new FullUpdateAll(this).runTaskTimerAsynchronously(this, 0, 6000);
		new QuickUpdateAll(this).runTaskTimerAsynchronously(this, 0, 40);
	}
}
