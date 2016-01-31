package com.thebubblenetwork.api.global.plugin;

import com.thebubblenetwork.api.global.bubblepackets.PacketHub;
import com.thebubblenetwork.api.global.file.PropertiesFile;
import com.thebubblenetwork.api.global.sql.SQLConnection;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

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
 * Date-created: 26/01/2016 20:28
 * Project: GlobalAPI
 */
public abstract class BubbleHubObject<PLUGIN,PLAYER> implements BubbleHub<PLUGIN,PLAYER>{
    private static BubbleHub<?,?> instance;

    public static BubbleHub getInstance(){
        return instance;
    }

    private final File sqlpropertiesfile = new File("sql.properties");
    private PropertiesFile sqlproperties;
    private SQLConnection connection;
    private PacketHub hub;

    public BubbleHubObject(){
        instance = this;
        if(!sqlpropertiesfile.exists()){
            try {
                PropertiesFile.generateFresh(sqlpropertiesfile,new String[]
                                {"hostname","port","username","password","database"},
                        new String[]
                                {"localhost","3306","root","NONE","bubbleserver"});
            } catch (Exception e) {
                e.printStackTrace();
            }
            logSevere("Properties File not found");
            endSetup("Could not find properties file");
            return;
        }
        try {
            sqlproperties = new PropertiesFile(sqlpropertiesfile);
        } catch (Exception e) {
            logSevere(e.getMessage());
            endSetup("Exception occurred when loading properties");
            return;
        }
        String temp;
        try {
            connection = new SQLConnection(
                    sqlproperties.getString("hostname"),
                    sqlproperties.getNumber("port").intValue(),
                    sqlproperties.getString("database"),
                    sqlproperties.getString("username"),
                    (temp = sqlproperties.getString("password")).equals("NONE") ? null : temp
            );
        }
        catch (ParseException ex){
            logSevere(ex.getMessage());
            endSetup("Invalid database port");
        }
    }

    public final void onEnable(){
        hub = new PacketHub();
        onBubbleEnable();
        runTaskLater(new Runnable() {
            public void run() {
                hub.register(getInstance());
            }
        },
        0L,
        TimeUnit.MILLISECONDS);
    }

    public final void onDisable(){
        onBubbleDisable();
        try {
            getConnection().closeConnection();
        } catch (SQLException e) {
            logSevere(e.getMessage());
            logSevere("Could not close SQL connection");
        }
    }

    public final void onLoad(){
        try {
            getConnection().openConnection();
        } catch (Exception e) {
            logSevere(e.getMessage());
            endSetup("Could not establish connection to database");
            return;
        }
        saveXServerDefaults();
        onBubbleLoad();
    }

    public SQLConnection getConnection(){
        return connection;
    }

    public PropertiesFile getSQLProperties(){
        return sqlproperties;
    }

    public PacketHub getPacketHub(){
        return hub;
    }

    public abstract void runTaskLater(Runnable r,long l,TimeUnit timeUnit);
    public abstract void saveXServerDefaults();
    public abstract void onBubbleEnable();
    public abstract void onBubbleDisable();
    public abstract void onBubbleLoad();
}
