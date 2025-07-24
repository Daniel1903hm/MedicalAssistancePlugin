package com.daniel1903hm.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.daniel1903hm.events.customEvents.MedicalAssistanceEvent;
import com.daniel1903hm.utils.Logger;


public final class EventsListener implements Listener {

	@EventHandler
    public void PlayerRegeneration(EntityRegainHealthEvent e) {
      if ((e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN) && e.getEntity().hasPermission("medicalAssistance.allowRegen"))
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDead(PlayerDeathEvent e) {
      e.setKeepInventory(false);
      e.setDroppedExp(0); 
      e.setDeathMessage(e.getEntity().getName()+ChatColor.RED+" Died, " +ChatColor.YELLOW+"see you in paradise");
    }
  
    @EventHandler
    public void MedicHealingListener(MedicalAssistanceEvent e) {
      if (e.getEventType() != "Medicine")
        return;
      Logger logger = new Logger(e.getEventInvoker().getName() + "-" + e.getEventType() + ".log");

      String message = "Doctor " + e.getEventInvoker().getName() + " gives a recipe of " + e.getEventQuantity()
          + "uds to "
          + e.getEventTarget().getName();

      logger.log(e, message, "medicalAssistance.medicine");
    }
    
     @EventHandler
    public void MedicListChangeListener(MedicalAssistanceEvent e) {
      if(e.getEventType() != "MedicListChange") return;
      Logger logger = new Logger(e.getEventInvoker().getName() + "-"+e.getEventType()+".log", true);
      String resolution;
      if(e.getEventAction().equals("add"))
        resolution = " to";
        else resolution = " from";
      String message = "User " + e.getEventInvoker().getName() + " "+e.getEventAction()+" " + e.getEventTarget().getName() + resolution +" medics list";

      logger.log(e, message, "medicalassistance.admin");
    }
}
