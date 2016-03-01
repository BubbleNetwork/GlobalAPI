package com.thebubblenetwork.api.global.plugin;

import com.thebubblenetwork.api.global.bubblepackets.PacketHub;
import com.thebubblenetwork.api.global.file.PropertiesFile;
import com.thebubblenetwork.api.global.plugin.updater.FileUpdater;
import com.thebubblenetwork.api.global.plugin.updater.SQLUpdater;
import com.thebubblenetwork.api.global.sql.SQLConnection;
import de.mickare.xserver.XServerPlugin;

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
public interface BubbleHub<P> extends FileUpdater {
    P getPlugin();

    Object getPlayer(UUID u);

    SQLConnection getConnection();

    PropertiesFile getSQLProperties();

    PacketHub getPacketHub();

    XServerPlugin getXPlugin();

    void onEnable();

    void onDisable();

    void onLoad();

    void endSetup(String reason) throws RuntimeException;

    void logInfo(String info);

    void logSevere(String error);

    void addUpdater(SQLUpdater updater);

    void addUpdater(FileUpdater updater);

    Plugman<P> getPlugman();
}
