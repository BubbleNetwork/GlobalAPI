package com.thebubblenetwork.api.global.website;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 19/03/2016 {17:10}
 * Created March 2016
 */
public class NamelessAPISettings {
    public static final String
    //USER TABLE STUFF
    USER_TABLE = "nl1_users",//Table
    ID = "id",//Integer, AUTO increases
    USER = "username",//Varchar: None
    PASS = "password",//Hash: None
    PASSMETH = "pass_method",//Varchar: default
    IGN = "mcname",//Varchar: None
    UUID = "uuid",//Varchar: None
    JOINED = "joined",//Integer: None
    GROUP = "group_id",//Integer: None
    EMAIL = "email",//Varchar: None
    BANNED = "isbanned",//Integer: 0
    IP = "lastip",//Varchar: Null
    ACTIVE = "active",//Integer: None
    SIGNATURE = "signature",//Varchar: Null
    REPUTATION = "reputation",//Integer: None
    RESET = "reset_code",//Varchar: null
    AVATAR = "has_avatar",//Integer: None
    GRAVATAR = "gravatar",//String: null
    ONLINE = "last_online",//Integer: None
    //GROUP TABLE STUFF
    GROUP_TABLE = "nl1_groups"

    ;

    private String host,user,pass,db;
    private int port;

    public NamelessAPISettings(String host, int port, String user, String pass, String db) {
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
