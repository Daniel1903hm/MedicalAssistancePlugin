package com.daniel1903hm.commands;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.daniel1903hm.Managers.Config;
import com.daniel1903hm.events.customEvents.MedicalAssistanceEvent;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;


public class MainCommand implements CommandExecutor {
	Config config = new Config();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player))
				{
					sender.sendMessage("Sender is not a player");
					return true;
				}

		if (!sender.hasPermission("medicalAssistance.use") || !sender.hasPermission("medicalassistance.admin")) {
			sender.sendMessage(ChatColor.RED + "You are not allowed to use this command");
			return true;
		}
		
		if(args.length < 1){
						sender.sendMessage(ChatColor.RED+"Usage /ma <SubCommand> <Player>");
						return true;
		}

		switch (args[0]) {
			case "medics":
				if (!sender.hasPermission("medicalassistance.admin")) {
					sender.sendMessage(ChatColor.RED+"You can't use this command");
					return true;
					}

					Player invoker = (Player) sender;

					if(args.length < 3 ){
						sender.sendMessage(ChatColor.RED+"Usage /ma medics <add | remove> <Player>");
						return true;
					}

					Player p = Bukkit.getPlayer(args[2]);

					if (p == null) {
						sender.sendMessage(ChatColor.RED + "Player not found");
						return true;
					}
					
					
					
					MedicalAssistanceEvent _event = new MedicalAssistanceEvent(invoker, p, "MedicListChange");
				
				if (_event.isCancelled()) {
					return true;
				}
				_event.setEventAction(args[1]);
				
				Bukkit.getPluginManager().callEvent(_event);

				
				if (args[1].equals("add")) {
					if (config.getMedics().contains(p.getUniqueId().toString())) {
						p.sendMessage("This player is on medics list already");
						return true;
					}

					config.addMedic(p);
					sender.sendMessage("Player added to medics list");
				}
				
				if (args[1].equals("remove")) {
					if (!config.getMedics().contains(p.getUniqueId().toString())) {
						sender.sendMessage("El usuario no se encuentra en lista");
						return true;
					}
					config.removeMedic(p);
					sender.sendMessage("Player added to medics list");

				}
						return true;
				case "giveMedicine":
					Player s = Bukkit.getPlayer(sender.getName());
					Player target;
					if (config.getBoolean("medicine-needs-perms") && (!s.hasPermission("medicalAssistance.medicine") || !s.hasPermission("medicalAssistance.admin"))) {
						s.sendMessage("You are not allowed to do that");
						return true;
					}
					if (args.length < 2){
						s.sendMessage(ChatColor.RED+"USAGE: /ma giveMedicine <Player> [Quantity]");
						return true;
					} else
						target = Bukkit.getPlayer(args[1]);
					
					int amount = args[2] != null ? Integer.parseInt(args[2]) : 1;

					MedicalAssistanceEvent event = new MedicalAssistanceEvent(s, target, "Medicine");
					event.setEventQuantity(amount);
					if (event.isCancelled()) {
						return true;
					}
					Bukkit.getPluginManager().callEvent(event);

						// Crear una poción
							ItemStack potion = new ItemStack(Material.POTION);
							PotionMeta meta = (PotionMeta) potion.getItemMeta();
							meta.setDisplayName(ChatColor.YELLOW + "Medicine");
							meta.setColor(Color.AQUA);
							PotionEffect effect = new PotionEffect(PotionEffectType.HEAL, 600, 2);
							meta.addCustomEffect(effect, true);
							potion.setAmount(amount);
							potion.setItemMeta(meta);

							s.getInventory().addItem(potion);
							s.sendMessage("You gived the medicines to your patient");
							target.sendMessage("You received your medicines ");
					return true;

				case "listMedics":
					List<String> medicsList = config.getMedics();
						ArrayList<String> medics = new ArrayList<>();
						if (medicsList.isEmpty()) {
							sender.sendMessage("No hay medicos registrados");
							return true;
						}

						for (String UUId : medicsList) {
							String PlayerName = Bukkit.getPlayer(UUID.fromString(UUId)).getName();
							medics.add(PlayerName);
						}

						sender.sendMessage("Hay estos medicos disponibles: \r\n" + String.join("\r\n", medics));
						break;
			default:
				break;
		}
		return true;
	}

	@Nullable
	public Player getPlayersWithin(Player player) {
		Player ejecutor = player;

		double distanciaMinima = Double.MAX_VALUE;
		Player jugadorMasCercano = null;

		for (Player otro : Bukkit.getOnlinePlayers()) {
				if (otro.equals(ejecutor)) continue; // ignoramos al mismo jugador

				if (otro.getWorld().equals(ejecutor.getWorld())) { // mismo mundo
						double distancia = ejecutor.getLocation().distance(otro.getLocation());
						if (distancia < distanciaMinima) {
								distanciaMinima = distancia;
								jugadorMasCercano = otro;
						}
				}
		}

		if (jugadorMasCercano != null) {
			return jugadorMasCercano;
			} else {
				return player;
			}
	}
}
