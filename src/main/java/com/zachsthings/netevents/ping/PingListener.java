
package com.zachsthings.netevents.ping;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.zachsthings.netevents.NetEventsPlugin;

/** Ping listener. */
public class PingListener implements Listener {
  private final NetEventsPlugin plugin;
  
  public PingListener(NetEventsPlugin plugin) {
    this.plugin = plugin;
  }
  
  @EventHandler
  public void onPingEvent(PingEvent event) {
    plugin.getLogger().info(String.format("Received ping from: %s - Host %s", event.getServerName(), event.getHostname()));
    
    for (Player player : plugin.getServer().getOnlinePlayers()) {
      if (player.hasPermission("netevents.status")) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b > Ping event: &d" + event.getHostname()));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b >> Server: &d" + event.getServerName()));
      }
    }
  }
}
