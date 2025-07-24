package com.daniel1903hm.events.customEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

  public class MedicalAssistanceEvent extends Event implements Cancellable{
  
    private static final HandlerList  handlers = new HandlerList();
    private boolean cancelled;
    private Player Invoker;
    private Player Target;
    private int quantity = -1;
    private final String EventType;
    private String EventAction = null;
    
    public MedicalAssistanceEvent(Player invoker, Player target, String EventType) {
      this.EventType = EventType;
      this.Invoker = invoker;
      this.Target = target;
    };

    public MedicalAssistanceEvent(String EventType) {
      this.EventType = EventType;
    };

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public Player getEventInvoker() {
    if (Invoker == null)
      return null;
    return this.Invoker;
  }
  
  public int getEventQuantity() {
    return this.quantity;
  }

  public Player getEventTarget() {
    if (Target == null)
      return null;
    return this.Target;
  }
  
  public void setEventQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getEventType() {
    return this.EventType;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
  
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  public void setEventAction(String EventAction) {
    this.EventAction = EventAction;
  }

  public String getEventAction() {
    return this.EventAction;
  }


  
}
