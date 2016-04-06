package com.thebubblenetwork.api.global.plugin;

import com.thebubblenetwork.api.global.bubblepackets.PacketHub;
import com.thebubblenetwork.api.global.file.PropertiesFile;
import com.thebubblenetwork.api.global.ftp.AbstractFileConnection;
import com.thebubblenetwork.api.global.ftp.FTPFileConnection;
import com.thebubblenetwork.api.global.ftp.SSHFileConnection;
import com.thebubblenetwork.api.global.player.BubblePlayer;
import com.thebubblenetwork.api.global.plugin.updater.FileUpdater;
import com.thebubblenetwork.api.global.plugin.updater.SQLUpdater;
import com.thebubblenetwork.api.global.plugin.updater.Updatetask;
import com.thebubblenetwork.api.global.sql.SQLConnection;
import com.thebubblenetwork.api.global.sql.SQLUtil;
import com.thebubblenetwork.api.global.type.ServerType;
import com.thebubblenetwork.api.global.website.NamelessAPI;
import com.thebubblenetwork.api.global.website.NamelessAPISettings;
import de.mickare.xserver.XServerPlugin;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public abstract class BubbleHub<P> implements FileUpdater {
    public static BubbleHub getInstance() {
        return instance;
    }

    private static boolean shutdown = false;

    private static BubbleHub<?> instance;
    private final File sqlpropertiesfile = new File("connect.properties");
    private PropertiesFile propertiesFile;
    private SQLConnection connection;
    private AbstractFileConnection fileConnection;
    private PacketHub hub;
    private Set<FileUpdater> fileupdaters = new HashSet<>();
    private Set<SQLUpdater> sqlUpdaters = new HashSet<>();
    private NamelessAPI api;

    public BubbleHub() {
        instance = this;
    }

    public final void onEnable() {

        getLogger().log(Level.INFO, "Creating PacketHub");

        hub = new PacketHub();

        getLogger().log(Level.INFO, "Enabling the plugin...");

        onBubbleEnable();

        runTaskLater(new Runnable() {
            public void run() {
                getLogger().log(Level.INFO, "Registering PacketHub...");
                try {
                    hub.register(getInstance());
                } catch (Exception ex) {
                    getLogger().log(Level.SEVERE, "Could not register packethub", ex);
                }
            }
        }, bungee() ? 1L : 0L, TimeUnit.SECONDS);

        getLogger().log(Level.INFO, "Finding updates table");

        try {
            if (!SQLUtil.tableExists(getConnection(), "updates")) {
                getLogger().log(Level.INFO, "Creating updates table");
                getConnection().executeSQL("CREATE TABLE `updates` (" +
                        "`artifact` VARCHAR(32) NOT NULL," +
                        "`version` INT(3) NOT NULL," +
                        "`url` VARCHAR(255) NOT NULL," +
                        "PRIMARY KEY (`artifact`)," +
                        "UNIQUE INDEX `UNIQUE KEY` (`artifact`)," +
                        "UNIQUE INDEX `UNIQUE URL` (`url`)," +
                        "INDEX `KEY` (`artifact`)" +
                        ")" +
                        ";");
                //TODO - SQL API
            }
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "Could not create updates table", ex);
        }


        runTaskLater(new Runnable() {
            public void run() {
                try {
                    new Updatetask(getConnection(), "updates", fileupdaters, sqlUpdaters) {
                        String name = getName();

                        public void logInfo(String s) {
                            System.out.println("[" + name + "] " + s);
                        }

                        public void logSevere(String s) {
                            System.err.println("[" + name + "] " + s);
                        }

                        public void update(Runnable r) {
                            BubbleHub.this.update(r);
                        }

                        public File getFile() {
                            return getReplace();
                        }
                    };
                } catch (Exception e) {
                    getLogger().log(Level.WARNING, "Could not run updater", e);
                }
                runTaskLater(this, 5L, TimeUnit.MINUTES);
            }
        }, 30L, TimeUnit.SECONDS);
    }

    public final void onDisable() {
        getLogger().log(Level.INFO, "Plugin is now disabling...");

        onBubbleDisable();

        getLogger().log(Level.INFO, "Closing connection to MySQL");

        try {
            getConnection().closeConnection();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, "Could not close database connection", e);
        }

        try{
            if(getFileConnection().isConnected()){
                getFileConnection().close();
            }
        }
        catch (Exception ex){
            getLogger().log(Level.SEVERE, "Could not close FTP connection", ex);
        }

        try{
            if(api.getConnection().checkConnection())api.getConnection().closeConnection();
        }
        catch (Exception e){
            getLogger().log(Level.SEVERE, "Could not close nameless database connection", e);
        }

        getPacketHub().unregisterThis();

        if (Updatetask.instance != null) {
            try {
                Updatetask.instance.interrupt();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Could not interrupt updatetask thread", e);
            }
            Updatetask.instance = null;
        }
    }

    public final void onLoad() {
        //Loading properties
        getLogger().log(Level.INFO, "Loading SQL Properties");

        if (!sqlpropertiesfile.exists()) {

            try {
                PropertiesFile.generateFresh(sqlpropertiesfile, new String[]{"hostname", "port", "username", "password", "database", "site-hostname","site-port", "site-username", "site-password","site-database","fileConnection-ip","fileConnection-port","fileConnection-type","fileConnection-user","fileConnection-password"}, new String[]{"localhost", "3306", "root", "NONE", "bubbleserver", "thebubblenetwork.com", "3306", "site","NONE","thebubbl_site","localhost","21","FTP","root","NONE"});
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Could not generate fresh properties file");
            }
        }

        try {
            propertiesFile = new PropertiesFile(sqlpropertiesfile);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not load SQL properties file", e);
            endSetup("Exception occurred when loading properties");
        }

        //Setting up nameless api

        String temp;
        try {
            api = new NamelessAPI(new NamelessAPISettings(propertiesFile.getString("site-hostname"), propertiesFile.getNumber("site-port").intValue(), propertiesFile.getString("site-username"), (temp = propertiesFile.getString("site-password")).equals("NONE") ? null : temp, propertiesFile.getString("site-database")));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE,"Could not setup nameless api");
        }

        getLogger().log(Level.INFO, "Finding database information...");

        //SQL info
        try {
            connection = new SQLConnection(propertiesFile.getString("hostname"), propertiesFile.getNumber("port").intValue(), propertiesFile.getString("database"), propertiesFile.getString("username"), (temp = propertiesFile.getString("password")).equals("NONE") ? null : temp);
        } catch (ParseException ex) {
            getLogger().log(Level.WARNING, "Could not load database information", ex);
            endSetup("Invalid database port");
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Could not load database information", ex);
            endSetup("Invalid configuration");
        }

        //FTP connection type
        Class<? extends AbstractFileConnection> fileConnectionClass;
        switch (propertiesFile.getString("fileConnection-type")){
            case "FTP":
            case "TLS":
            case "SSL":
                fileConnectionClass = FTPFileConnection.class;
                break;
            case "SSH":
            case "SFTP":
                fileConnectionClass = SSHFileConnection.class;
                break;
            default:
                getLogger().log(Level.WARNING, "Invalid fileConnection-type");
                endSetup("Invalid fileconnection");
                return;
        }

        //FTP info
        try{
            fileConnection = AbstractFileConnection.create(fileConnectionClass, propertiesFile.getString("fileConnection-ip"), propertiesFile.getNumber("fileConnection-port").intValue());
        } catch (ParseException ex) {
            getLogger().log(Level.WARNING, "Could not load fileConnection information", ex);
            endSetup("Invalid fileConnection port");
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Could not load fileConnection information", ex);
            endSetup("Invalid configuration");
        }

        //Connecting to MySQL
        getLogger().log(Level.INFO, "Connecting to MySQL...");

        try {
            getConnection().openConnection();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Could not connect to MySQL", e);
            endSetup("Could not establish connection to database");
        }

        //Connecting to FTP
        try{
            getFileConnection().connect();
            getFileConnection().login(propertiesFile.getString("fileConnection-user"), (temp = propertiesFile.getString("fileConnection-password")).equals("NONE") ? null : temp);
            getFileConnection().close();
            getFileConnection().connect();
            getFileConnection().login(propertiesFile.getString("fileConnection-user"), (temp = propertiesFile.getString("fileConnection-password")).equals("NONE") ? null : temp);
        }
        catch (Exception ex){
            getLogger().log(Level.WARNING, "Could not connect to FTP", ex);
            endSetup("Could not establish connection to fileConnection");
        }


        //Loading server types

        getLogger().log(Level.INFO, "Finding server types...");

        try {
            if (!SQLUtil.tableExists(getConnection(), "servertypes")) {
                connection.executeSQL("CREATE TABLE `servertypes` (" +
                        "`name` VARCHAR(32) NOT NULL," +
                        "`prefix` VARCHAR(16) NOT NULL," +
                        "`download` VARCHAR(255) NOT NULL," +
                        "`maxplayer` INT(3) NOT NULL," +
                        "`low-limit` INT(3) NOT NULL," +
                        "`high-limit` INT(3) NOT NULL," +
                        "PRIMARY KEY (`name`)," +
                        "UNIQUE INDEX `UNIQUE` (`name`, `prefix`)," +
                        "UNIQUE INDEX `DOWNLOADUNIQUE` (`download`)," +
                        "INDEX `NAME KEY` (`name`)," +
                        "INDEX `PREFIX` (`prefix`)," +
                        "INDEX `DOWNLOAD` (`download`)" +
                        ")" +
                        ";");
            }
            ResultSet set = SQLUtil.query(getConnection(), "servertypes", "*", new SQLUtil.Where("1"));
            while (set.next()) {
                ServerType.registerType(new ServerType(set.getString("name"), set.getString("prefix"), set.getString("download"),set.getInt("maxplayer"), set.getInt("low-limit"), set.getInt("high-limit")));
            }
            set.close();
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Could not find server types", ex);
            endSetup("Could not find server types");
        }

        getLogger().log(Level.INFO, "Saving XServer Configuration...");


        //Normal load stuff
        saveXServerDefaults();

        getLogger().log(Level.INFO, "Loading plugin...");

        onBubbleLoad();

        getLogger().log(Level.INFO,"Adding updaters");

        addUpdater(this);
        addUpdater(new SQLUpdater() {
            public void update(SQLConnection connection) throws SQLException, ClassNotFoundException {
                ResultSet set = null;
                try {
                    set = SQLUtil.query(getConnection(), "servertypes", "*", new SQLUtil.Where("1"));
                    final Set<ServerType> serverTypeObjects = new HashSet<>();
                    while (set.next()) {
                        serverTypeObjects.add(new ServerType(set.getString("name"), set.getString("prefix"), set.getString("download"), set.getInt("maxplayer"), set.getInt("low-limit"), set.getInt("high-limit")));
                    }
                    runTaskLater(new Runnable() {
                        public void run() {
                            synchronized (ServerType.getTypes()) {
                                ServerType.getTypes().clear();
                                for (ServerType serverType : serverTypeObjects) {
                                    ServerType.registerType(serverType);
                                }
                            }
                        }
                    }, 0L, TimeUnit.MILLISECONDS);
                } finally {
                    try {
                        if (set != null) {
                            set.close();
                        }
                    } catch (Throwable throwable) {
                    }
                }
            }

            public String getName() {
                return "ServerTypeUpdater";
            }
        });


        getLogger().log(Level.INFO, "Load complete");
    }

    public abstract BubblePlayer<?> getPlayer();

    public NamelessAPI getNameless(){
        return api;
    }

    public SQLConnection getConnection() {
        return connection;
    }

    public PropertiesFile getProperties() {
        return propertiesFile;
    }

    public AbstractFileConnection getFileConnection(){
        return fileConnection;
    }

    public PacketHub getPacketHub() {
        return hub;
    }

    public void addUpdater(FileUpdater updater) {
        fileupdaters.add(updater);
    }

    public void addUpdater(SQLUpdater updater) {
        sqlUpdaters.add(updater);
    }

    public abstract void update(Runnable r);

    public abstract boolean bungee();

    public abstract void runTaskLater(Runnable r, long l, TimeUnit timeUnit);

    public abstract void saveXServerDefaults();

    public abstract void onBubbleEnable();

    public abstract void onBubbleDisable();

    public abstract void onBubbleLoad();

    public abstract P getPlugin();

    public abstract Object getPlayer(UUID u);

    public abstract Logger getLogger();

    public abstract XServerPlugin getXPlugin();

    public void endSetup(String s) {
        getLogger().log(Level.SEVERE, s);
        if(!shutdown) {
            stop();
            shutdown = true;
        }
        throw new IllegalArgumentException("Disabling... " + s);
    }

    public abstract void stop();
}
