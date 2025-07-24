package com.daniel1903hm.AutoComplete;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class MainCommandAutocomplete implements TabCompleter{

  @Override
  public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
    if (args.length == 1) {
      List<String> subcommands = new ArrayList<>();

      // Agregamos los comandos según permisos
      if (sender.hasPermission("medicalassistance.admin")) {
        subcommands.add("medics");
      }
      if (sender.hasPermission("medicalassistance.medicine")) {
        subcommands.add("giveMedicine");
      }

      subcommands.add("listMedics"); // Comando sin permisos

      // Autocompletado según lo que el jugador empieza a escribir
      String currentInput = args[0].toLowerCase();
      return subcommands.stream()
          .filter(sub -> sub.toLowerCase().startsWith(currentInput))
          .collect(Collectors.toList());
    }

    if (args.length == 2 && args[0].equals("medics")) {
      List<String> options = new ArrayList<>();

      options.add("add");
      options.add("remove");

      // Autocompletado según lo que el jugador empieza a escribir
      String currentInput = args[1].toLowerCase();
      return options.stream()
          .filter(sub -> sub.toLowerCase().startsWith(currentInput))
          .collect(Collectors.toList());
    }

    if (args.length == 2 && args[0].equals("giveMedicine")) {
      List<String> players = new ArrayList<>();
      for (Player p : Bukkit.getOnlinePlayers()) {
        players.add(p.getName());
      }

      // Autocompletado según lo que el jugador empieza a escribir
      String currentInput = args[1].toLowerCase();
      return players.stream()
          .filter(p -> p.toLowerCase().startsWith(currentInput))
          .collect(Collectors.toList());
    }

    return null; // No completar nada en otros niveles de argumentos
  }
}
