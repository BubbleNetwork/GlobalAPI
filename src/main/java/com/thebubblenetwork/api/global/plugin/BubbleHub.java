package com.thebubblenetwork.api.global.plugin;

import com.thebubblenetwork.api.global.bubblepackets.PacketHub;
import com.thebubblenetwork.api.global.file.PropertiesFile;
import com.thebubblenetwork.api.global.sql.SQLConnection;
import de.mickare.xserver.AbstractXServerManager;

import java.util.UUID;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) The Bubble Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Wrote by Jacob Evans <jacobevansminor@gmail.com>, 01 2016
 * <p>
 * <p>
 * Class information
 * ---------------------
 * Package: com.thebubblenetwork.api.global.plugin
 * Date-created: 26/01/2016 20:25
 * Project: GlobalAPI
 */
public interface BubbleHub<PLUGIN,PLAYER>{
    PLUGIN getPlugin();
    PLAYER getPlayer(UUID u);
    SQLConnection getConnection();
    PropertiesFile getSQLProperties();
    PacketHub getPacketHub();
    AbstractXServerManager getXManager();
    void onEnable();
    void onDisable();
    void onLoad();
    void endSetup(String reason) throws RuntimeException;
    void logInfo(String info);
    void logSevere(String error);
}
