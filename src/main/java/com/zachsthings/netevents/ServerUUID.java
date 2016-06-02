/** Copyright (C) 2014 zml (netevents@zachsthings.com) Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License. */

package com.zachsthings.netevents;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

/** Holder for a fixed server UUID field that is persistent */
class ServerUUID {
  private static final Logger log = Logger.getLogger(ServerUUID.class.getCanonicalName());
  private UUID serverUid;
  private final Path storeFile;
  
  public ServerUUID(Path storeFile) {
    this.storeFile = storeFile;
    load();
  }
  
  private void load() {
    
    if (!Files.exists(storeFile)) {
      generateNew();
    }
    
    UUID fileUUID;
    BufferedReader brJSON = null;
    try {
      brJSON = new BufferedReader(new FileReader(storeFile.toFile()));
    }
    catch (FileNotFoundException e) {
      log.severe("NetEventsPlugin Error: " + e.getMessage());
      if (NetEventsPlugin.isDebugMode()) {
        e.printStackTrace();
      }
    }
    
    fileUUID = new GsonBuilder().setPrettyPrinting().create().fromJson(brJSON, UUID.class);
    
    serverUid = fileUUID;
    
    try {
      brJSON.close();
    }
    catch (IOException ex) {
      log.severe("NetEventsPlugin Error: " + ex.getMessage());
      if (NetEventsPlugin.isDebugMode()) {
        ex.printStackTrace();
      }
    }
    
    try (DataInputStream str = new DataInputStream(Files.newInputStream(storeFile))) {
    }
    catch (IOException e) {
      log.warning("Failed to read server UUID, generating new");
      generateNew();
    }
  }
  
  private void generateNew() {
    serverUid = UUID.randomUUID();
    
    String json = new GsonBuilder().setPrettyPrinting().create().toJson(serverUid, UUID.class);
    
    try {
      FileWriter writer = new FileWriter(storeFile.toFile());
      writer.write(json);
      writer.close();
    }
    catch (IOException ex) {
      log.severe("NetEventsPlugin Error: " + ex.getMessage());
      if (NetEventsPlugin.isDebugMode()) {
        ex.printStackTrace();
      }
    }
  }
  
  public synchronized UUID get() {
    if (serverUid == null) {
      load();
    }
    return serverUid;
  }
}
