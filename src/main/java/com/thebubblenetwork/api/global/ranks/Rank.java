package com.thebubblenetwork.api.global.ranks;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.RankData;
import de.mickare.xserver.util.ChatColor;

import java.util.*;

/**
 * Created by Jacob on 31/12/2015.
 */
public class Rank {
    public static Set<Rank> getRanks() {
        return ranks;
    }

    public static Rank getDefault() {
        for (Rank r : getRanks()) {
            if (r.isDefault()) {
                return r;
            }
        }
        Rank r;
        return (r = getRank("default")) != null ? r : ranks.size() > 0 ? Iterables.get(ranks, 0) : null;
    }

    public static Rank getRank(String s) {
        for (Rank r : getRanks()) {
            if (r.getName().equalsIgnoreCase(s)) {
                return r;
            }
        }
        return null;
    }

    public static void loadRank(String name, Map<String, String> map) {
        Rank r = getRank(name);
        //If the rank exists remove it
        if (r != null) {
            ranks.remove(r);
        }
        //If the rank has data re-create it
        if(map != null){
            r = new Rank(name,new RankData(new HashMap<>(map)));
            ranks.add(r);
        }
    }

    private static boolean isAuthorized(Rank r, String indentifier) {
        String[] args = indentifier.split("\\.");
        String current = "*";
        Decision d = isAuthorizedPrimative(r, indentifier);
        for(String s: args){
            if(d != Decision.CONTINUE)break;
            d = isAuthorizedPrimative(r, current);
            current = current.replace("*",s + ".*");
        }
        try{
            return d.decision();
        }
        catch (UnsupportedOperationException ex){
            return r.getInheritance() != null && isAuthorized(r.getInheritance(), indentifier);
        }
    }

    private static Decision isAuthorizedPrimative(Rank r, String indentifier) {
        Decision d;
        try {
            d = Decision.getDecision(r.getData().getBoolean(indentifier));
        } catch (Exception e) {
            d = Decision.CONTINUE;
        }
        return d;
    }

    private static Set<Rank> ranks = Collections.synchronizedSet(new HashSet<Rank>());
    private RankData data;
    private String name;

    public Rank(String name, RankData data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        try {
            return ChatColor.translateAlternateColorCodes('&', getData().getString(RankData.PREFIX));
        } catch (InvalidBaseException ex) {
            return "";
        }
    }

    public void setPrefix(String s) {
        getData().set(RankData.PREFIX, s);
    }

    public String getSuffix() {
        try {
            return ChatColor.translateAlternateColorCodes('&', getData().getString(RankData.SUFFIX));
        } catch (InvalidBaseException ex) {
            return "";
        }
    }

    public void setSuffix(String s) {
        getData().set(RankData.SUFFIX, s);
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
    public String toString() {
        return getName();
    }

    public Rank getInheritance() {
        return getRank(getInheritanceString());
    }

    public void setInheritance(Rank r) {
        getData().set(RankData.INHERITANCE, r == null ? null : r.getName());
    }

    public boolean isAuthorized(String indentifier) {
        return isAuthorized(this, indentifier);
    }

    public RankData getData() {
        return data;
    }

    enum Decision {
        CONTINUE(), TRUE(), FALSE();

        static Decision getDecision(boolean b) {
            if (b) {
                return TRUE;
            }
            return FALSE;
        }

        boolean decision() {
            if (this == TRUE) {
                return true;
            }
            if (this == FALSE) {
                return false;
            }
            throw new UnsupportedOperationException();
        }
    }
}
