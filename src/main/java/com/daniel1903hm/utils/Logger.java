package com.daniel1903hm.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.daniel1903hm.events.customEvents.MedicalAssistanceEvent;

public class Logger {
  String FileName;
  boolean needParent = false;

  public Logger(String FileName) {
    this.FileName = FileName;
  }
  
  public Logger(String FileName, boolean needParent) {
    this.FileName = FileName;
    this.needParent = needParent;
  }

  public void log(MedicalAssistanceEvent e, String message, String permission) {
    String resolution = this.needParent ? e.getEventType() : "";
    String parent = "/"+resolution;
    if (permission == null)
      permission = "medicalAssistance.medicine";

    //* Declarar la carpeta de datos
    String dataFolder = Bukkit.getPluginManager().getPlugin("medicalassistance").getDataFolder().getAbsolutePath()
        + "/logs" + parent ;

    Player mainPlayer = e.getEventInvoker();
    Player target = e.getEventTarget();

    //* Hacer el archivo del evento
    File Log = new File(dataFolder, mainPlayer.getName() + "-" + e.getEventType() + ".log");

    //* Conseguir y formatear la fecha
    LocalDateTime dateTime = LocalDateTime.now();
    DateTimeFormatter FormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDate = dateTime.format(FormatObj);

    //* Si no existe la carpeta padre la creamos
    if (!(Log.getParentFile().exists()))
      Log.getParentFile().mkdirs();

    //* Intentamos abrir el archivo como escritura y le añadimos datos
    try (FileWriter writer = new FileWriter(Log, true)) {
      Log.setWritable(true);

      if (!(Log.exists())) {
        Log.createNewFile();
      }

      if (permission != null) {
        if (!mainPlayer.hasPermission(permission)) {
          mainPlayer.sendMessage("You are not allowed to use this command");
          writer.write("User not allowed to use " + e.getEventType() + " command");
          writer.close();
          e.setCancelled(true);
          return;
        }
      }
    
      writer.write("\r\n[ " + formattedDate + " ]["+e.getEventType()+"] "+message);
      writer.close();
    } catch (IOException err) {
      mainPlayer.sendMessage(ChatColor.RED
          + "[Medical Assistance] An error has ocurred logging actions, pls contact an administrator or plugin author to solve this problem");
      Bukkit.getConsoleSender().getServer().broadcast(
          (ChatColor.RED + "[Medical-Assistance-ERROR] An error has ocurred when logging"),
          permission);
      err.printStackTrace();
      return;
    }
  }
}
