package com.thebubblenetwork.api.global.type;

import com.thebubblenetwork.api.global.plugin.BubbleHubObject;

import java.util.HashSet;
import java.util.Set;

/**
 * The Bubble Network 2016
 * BubbleBungee
 * 09/01/2016 {11:12}
 * Created January 2016
 */

public class ServerType {
    private static Set<ServerType> types = new HashSet<>();

    public static ServerType registerType(ServerType type){
        types.add(type);
        BubbleHubObject.getInstance().logInfo("Registered servertype: " + type.getName());
        return type;
    }

    public static ServerType getType(String name){
        for(ServerType wrapper:types){
            if(wrapper.getName().equals(name))return wrapper;
        }
        throw new IllegalArgumentException(name + " is not a correct servertype");
    }

    public static Set<ServerType> getTypes() {
        return types;
    }

    private String name,prefix;
    private int maxplayers,lowlimit,highlimit;

    public ServerType(String name,String prefix, int maxplayers, int lowlimit, int highlimit) {
        this.name = name;
        this.highlimit = highlimit;
        this.lowlimit = lowlimit;
        this.maxplayers = maxplayers;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getMaxPlayers(){
        return maxplayers;
    }

    public int getLowlimit() {
        return lowlimit;
    }

    public int getHighlimit() {
        return highlimit;
    }
}
