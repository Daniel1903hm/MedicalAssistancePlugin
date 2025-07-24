package com.daniel1903hm.Managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
  public List<String> medics = new ArrayList<>();

    private final File file;
    private final FileConfiguration config;

    public Config() {
      File pluginFolder = Bukkit.getPluginManager().getPlugin("medicalassistance").getDataFolder();
      if (!pluginFolder.exists())
        pluginFolder.mkdirs();
        
         file = new File(pluginFolder, "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
                config = YamlConfiguration.loadConfiguration(file);
                writeDefaults();
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear config.yml", e);
            }
        } else {
            config = YamlConfiguration.loadConfiguration(file);
        }
      
    }


  public void writeDefaults() {
    config.set("medics", List.of());
    config.set("medicine-needs-perms", true);
    save();
  }

  public List<String> getMedics() {
    List<String> medicsList = config.getStringList("medics");
    return medicsList;
  }

  public void addMedic(Player p) {
    medics = getMedics();
    medics.add(p.getUniqueId().toString());
    config.set("medics", medics);
    save();
  }

  public void save() {
    try {
        config.save(file);
      } catch (IOException e) {
        e.printStackTrace();
      }
  }


  public boolean getBoolean(String path) {
    if(!config.contains(path)){
      Bukkit.getServer().getConsoleSender().sendMessage("Incorrect path on getBoolean");
    }
    
    return config.getBoolean(path);
  }


  public void removeMedic(Player p) {
      medics.forEach(u -> {
        if (u.equals(p.getUniqueId().toString()))
          medics.remove(p.getUniqueId().toString());
      });
      config.set("medics", medics);
      save();
    }
  }
