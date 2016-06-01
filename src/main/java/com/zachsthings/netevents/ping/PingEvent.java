
package com.zachsthings.netevents.ping;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/** Event for testing purposes */
public class PingEvent extends Event implements Serializable {
  
  private static final HandlerList HANDLERS = new HandlerList();
  private final String hostname;
  private final String servername;
  
  public PingEvent(Server server) {
    String hostname;
    try {
      hostname = InetAddress.getLocalHost().getHostAddress() + ":" + server.getPort();
    }
    catch (UnknownHostException e) {
      hostname = "<unknown>";
    }
    this.hostname = hostname;
    
    if (server.getName().isEmpty()) {
      servername = "Server-" + server.getPort();
    }
    else {
      servername = server.getServerName();
    }
  }
  
  public String getHostname() {
    return hostname;
  }
  
  public String getServerName() {
    return servername;
  }
  
  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }
  
  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
