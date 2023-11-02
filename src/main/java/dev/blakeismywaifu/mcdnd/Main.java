package dev.blakeismywaifu.mcdnd;

import dev.blakeismywaifu.mcdnd.Utils.Log;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		Log.info("Plugin Enabled");
	}

	@Override
	public void onDisable() {
		Log.info("Plugin Disabled");
	}
}
