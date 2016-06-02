/** Copyright (C) 2014 zml (netevents@zachsthings.com) Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License. */

package com.zachsthings.netevents;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.zachsthings.netevents.ping.PingEvent;

/** Command that provides status report */
class StatusCommand implements CommandExecutor {
  private static String text(String... args) {
    StringBuilder build = new StringBuilder(ChatColor.AQUA.toString());
    for (String arg : args) {
      build.append(arg).append(ChatColor.AQUA);
    }
    return build.toString();
  }
  
  private static String error(String args) {
    return ChatColor.RED + args;
  }
  
  private static String hl(String text) {
    return ChatColor.YELLOW + text;
  }
  
  private final NetEventsPlugin plugin;
  
  public StatusCommand(NetEventsPlugin plugin) {
    this.plugin = plugin;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(text(" Remote listener bound to ", hl(plugin.getBoundAddress().toString())));
      sender.sendMessage(text(" Connected servers:"));
      for (Forwarder f : plugin.getForwarders()) {
        if (f.isActive()) {
          sender.sendMessage(text(" - ", hl(f.getRemoteAddress().toString())));
        }
        else if (f.getRemoteAddress() != null) {
          sender.sendMessage(text(" - ", ChatColor.RED + f.getRemoteAddress().toString()));
        }
      }
      sender.sendMessage(ChatColor.LIGHT_PURPLE + "  Usage: /" + label + " [reload | tryconnect | ping | uid | debug]");
    }
    else {
      final String commandLabel = args[0];
      switch (commandLabel) {
        case "reload":
          try {
            plugin.reload();
            sender.sendMessage(text("NetEvents reloaded"));
          }
          catch (IOException e) {
            sender.sendMessage(error("Error reloading. See console for details."));
            plugin.getLogger().log(Level.SEVERE, "Error reloading", e);
          }
          
          break;
        
        case "tryconnect":
          plugin.getReconnectTask().attemptAllNext();
          sender.sendMessage(text("Attempting to reconnect to all errored servers next second"));
          break;
        case "ping":
          plugin.callEvent(new PingEvent(plugin.getServer()));
          sender.sendMessage(text("Sent ping to all connected servers"));
          break;
        case "uid":
        case "uuid":
          sender.sendMessage(text(String.format("Server %s UUID: %s", plugin.getServer().getServerName(), plugin.getServerUUID())));
          break;
        case "debug":
          final boolean debugState = !plugin.isDebugMode();
          plugin.setDebugMode(debugState);
          sender.sendMessage(text("Debug mode ", hl(debugState ? "enabled" : "disabled")));
          break;
        default:
          sender.sendMessage(error("Unknown sub command..."));
          sender.sendMessage(error("Usage: /" + label + " <reload|tryconnect|ping|uid|debug>"));
          break;
      }
      
    }
    return true;
  }
}
