package com.daniel1903hm;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.daniel1903hm.AutoComplete.MainCommandAutocomplete;
import com.daniel1903hm.commands.MainCommand;
import com.daniel1903hm.events.EventsListener;

public class Main extends JavaPlugin {
    @Override
		public void onEnable() {
			Log("Plugin Enabled");
			//Register Command Executors
			registerCommands();
			//Register Event Listeners
			registerListeners();

		}

		@Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }
	
		public void registerCommands() {
			PluginCommand MainCommand = this.getCommand("medicalAssistance");
			MainCommand.setExecutor(new MainCommand());
			MainCommand.setTabCompleter(new MainCommandAutocomplete());
		}
		
		public void registerListeners() {
			Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
		}
		
		public void Log(String msg) {
			getServer().getConsoleSender().sendMessage("[Medical-Assistance] " + msg);
		}

}
