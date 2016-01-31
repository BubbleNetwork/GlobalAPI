package com.thebubblenetwork.api.global.ranks;

import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.RankData;
import com.thebubblenetwork.api.global.plugin.BubbleHubObject;
import com.thebubblenetwork.api.global.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        boolean b = false;
        try {
            b = r.getData().getBoolean(indentifier);
        } catch (InvalidBaseException e) {
        }
        return b || (r.getInheritance() != null && isAuthorized(r.getInheritance(), indentifier));
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        try {
            return getData().getString(RankData.PREFIX);
        } catch (InvalidBaseException ex) {
            return "";
        }
    }

    public String getSuffix() {
        try {
            return getData().getString(RankData.SUFFIX);
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
