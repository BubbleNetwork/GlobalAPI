package com.thebubblenetwork.api.global.website;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 19/03/2016 {17:10}
 * Created March 2016
 */
public class WebAPISettings {
    public static final String
    //USER TABLE STUFF
    USER_TABLE = "nl1_users",
    ID = "id",
    USER = "username",
    PASS = "password",
    PASSMETH = "pass_method",
    IGN = "mcname",
    UUID = "uuid",
    JOINED = "joined",
    GROUP = "group_id",
    EMAIL = "email",
    BANNED = "isbanned",
    IP = "lastip",
    ACTIVE = "active",
    SIGNATURE = "signature",
    REPUTATION = "reputation",
    RESET = "reset_code",
    AVATAR = "has_avatar",
    GRAVATAR = "gravatar",
    ONLINE = "last_online",
    //GROUP TABLE STUFF
    GROUP_TABLE = "nl1_groups"

    ;

    private String host,user,pass,db;
    private int port;

    public WebAPISettings(String host, int port, String pass, String user, String db) {
        this.host = host;
        this.port = port;
        this.pass = pass;
        this.user = user;
        this.db = db;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDb() {
        return db;
    }

    public int getPort() {
        return port;
    }
}
