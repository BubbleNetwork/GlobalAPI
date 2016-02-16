package com.thebubblenetwork.api.global.ranks;

import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.RankData;
import de.mickare.xserver.util.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jacob on 31/12/2015.
 */
public class Rank {
    private static Map<String, Rank> ranks = new HashMap<>();

    public static Map<String, Rank> getRanks() {
        return ranks;
    }

    private RankData data;
    private String name;

    public Rank(String name, RankData data) {
        this.name = name;
        this.data = data;
    }

    public static Rank getDefault() {
        for (Rank r : ranks.values())
            if (r.isDefault())
                return r;
        Rank r;
        return (r = ranks.get("default")) != null ? r : ranks.size() > 0 ? Iterables.get(ranks.values(), 0) : null;
    }

    public static Rank getRank(String s) {
        return ranks.get(s);
    }

    public static void loadRank(String name,Map map){
        ranks.put(name,new Rank(name,new RankData(map)));
    }

    private static boolean isAuthorized(Rank r, String indentifier) {
        boolean b = isAuthorizedPrimative(r,indentifier);
        String currentlytesting = "*";
        String[] split = indentifier.split(".");
        int i = 0;
        while(!b && i < split.length){
            b = isAuthorizedPrimative(r,currentlytesting);
            if(currentlytesting.equalsIgnoreCase(indentifier))break;
            currentlytesting = currentlytesting.replace("*",split[i] + ".*");
            i++;
        }
        return b || (r.getInheritance() != null && isAuthorized(r.getInheritance(), indentifier));
    }

    private static boolean isAuthorizedPrimative(Rank r, String indentifier){
        try {
            return r.getData().getBoolean(indentifier);
        } catch (InvalidBaseException e) {
        }
        return false;
    }


    public String getName() {
        return name;
    }

    public void setPrefix(String s){
        getData().set(RankData.PREFIX,s);
    }

    public String getPrefix() {
        try {
            return ChatColor.translateAlternateColorCodes('&',getData().getString(RankData.PREFIX));
        } catch (InvalidBaseException ex) {
            return "";
        }
    }

    public void setSuffix(String s){
        getData().set(RankData.SUFFIX,s);
    }

    public String getSuffix() {
        try {
            return ChatColor.translateAlternateColorCodes('&',getData().getString(RankData.SUFFIX));
        } catch (InvalidBaseException ex) {
            return "";
        }
    }

    protected String getInheritanceString() {
        try {
            return getData().getString(RankData.INHERITANCE);
        } catch (InvalidBaseException ex) {
            return null;
        }
    }

    public boolean isDefault() {
        try {
            return getData().getBoolean("default");
        } catch (InvalidBaseException e) {
            return false;
        }
    }

    @Override
    public String toString(){
        return getName();
    }

    public void setInheritance(Rank r){
        getData().set(RankData.INHERITANCE,r == null ? null : r.getName());
    }

    public Rank getInheritance() {
        return ranks.get(getInheritanceString());
    }

    public boolean isAuthorized(String indentifier) {
        return isAuthorized(this, indentifier);
    }

    public RankData getData() {
        return data;
    }
}
