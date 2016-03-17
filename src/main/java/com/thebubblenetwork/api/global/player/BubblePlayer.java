package com.thebubblenetwork.api.global.player;

import com.google.common.base.Joiner;
import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.PlayerData;
import com.thebubblenetwork.api.global.plugin.BubbleHub;
import com.thebubblenetwork.api.global.ranks.Rank;
import de.mickare.xserver.util.ChatColor;

import java.util.*;
import java.util.logging.Level;

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
 * Package: com.thebubblenetwork.api.global.player
 * Date-created: 26/01/2016 20:19
 * Project: GlobalAPI
 */
public abstract class BubblePlayer<T> {
    public static BubblePlayer getObject(UUID u) {
        return playerObjectMap.get(u);
    }

    public static Map<UUID, BubblePlayer> getPlayerObjectMap() {
        return playerObjectMap;
    }

    private static Map<UUID, BubblePlayer> playerObjectMap = new HashMap<>();
    private UUID u;
    private PlayerData data;
    private T player;

    protected BubblePlayer(UUID u, PlayerData data) {
        this.u = u;
        this.data = data;
    }

    public UUID getUUID() {
        return u;
    }

    public PlayerData getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        getData().getRaw().clear();
        for (Map.Entry<String, String> e : data.entrySet()) {
            getData().getRaw().put(e.getKey(), e.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public T getPlayer() {
        if (player == null) {
            player = (T) (BubbleHub.getInstance().getPlayer(getUUID()));
        }
        return player;
    }

    private String getRankString() throws InvalidBaseException {
        return getData().getString(PlayerData.MAINRANK);
    }

    public Rank getRank() {
        String s;
        try {
            s = getRankString();
        } catch (InvalidBaseException e) {
            BubbleHub.getInstance().getLogger().log(Level.SEVERE, "Could not get rank string", e);
            return Rank.getDefault();
        }
        return Rank.getRank(s);
    }

    public void setRank(Rank rank) {
        getData().set(PlayerData.MAINRANK, rank.getName());
        update();
    }

    private String[] getSubRanksString() throws InvalidBaseException {
        return getData().getString(PlayerData.SUBRANKS).split(",");
    }

    public Rank[] getSubRanks() {
        String[] s;
        try {
            s = getSubRanksString();
        } catch (InvalidBaseException e) {
            return new Rank[0];
        }
        List<Rank> ranks = new ArrayList<>();
        for (String rankname : s) {
            Rank r = Rank.getRank(rankname);
            if (r != null) {
                ranks.add(r);
            }
        }
        return ranks.toArray(new Rank[0]);
    }

    public void setSubRanks(Rank... subRanks) {
        setSubRanks(Arrays.asList(subRanks));
    }

    public void setSubRanks(Iterable<Rank> subRanks) {
        setList(PlayerData.SUBRANKS, toStrings(subRanks));
        update();
    }

    public UUID[] getFriends() {
        try {
            return getData().getUUIDList(PlayerData.FRIENDSLIST);
        } catch (InvalidBaseException e) {
            return new UUID[0];
        }
    }

    public void setFriends(UUID... friends) {
        setFriends(Arrays.asList(friends));
    }

    public void setFriends(Iterable<UUID> friends) {
        setList(PlayerData.FRIENDSLIST, toStrings(friends));
        update();
    }

    public UUID[] getFriendIncomingRequests() {
        try {
            return getData().getUUIDList(PlayerData.FRIENDINCOMINGRQ);
        } catch (InvalidBaseException e) {
            return new UUID[0];
        }
    }

    public double getStats(String game, String stat) {
        try {
            return getData().getNumber(PlayerData.STATSBASE + "." + game + "." + stat).doubleValue();
        } catch (InvalidBaseException e) {
            return 0D;
        }
    }

    public int getKit(String game, String kit) {
        try {
            return getData().getNumber(PlayerData.STATSBASE + "." + game + "." + kit).intValue();
        } catch (InvalidBaseException e) {
            return 0;
        }
    }

    public int getHubItem(String item) {
        try {
            return getData().getNumber(PlayerData.ITEMSBASE + "." + item).intValue();
        } catch (InvalidBaseException e) {
            return 0;
        }
    }

    public boolean getHubItemUsable(String item){
        try{
            return getData().getBoolean(PlayerData.ITEMALLOWED + "." + item);
        }
        catch (InvalidBaseException e){
            return isAuthorized("ultracosmetics." + item);
        }
    }

    public int getTokens() {
        try {
            return getData().getNumber(PlayerData.TOKENS).intValue();
        } catch (InvalidBaseException e) {
            return 0;
        }
    }

    public void setTokens(int tokens) {
        getData().set(PlayerData.TOKENS, tokens);
        update();
    }

    public int getKeys() {
        try {
            return getData().getNumber(PlayerData.KEYS).intValue();
        } catch (InvalidBaseException e) {
            return 0;
        }
    }

    public void setKeys(int keys) {
        getData().set(PlayerData.KEYS, keys);
        update();
    }

    public boolean isAuthorized(String permission) {
        return getRank().isAuthorized(permission);
    }

    public void setStat(String game, String indentifier, int id) {
        getData().set(PlayerData.STATSBASE + "." + game + "." + indentifier, id);
        update();
    }

    public void setKit(String game, String indentifier, int id) {
        getData().set(PlayerData.KITBASE + "." + game + "." + indentifier, id);
        update();
    }

    public void setFriendsIncomingRequests(UUID... friends) {
        setFriendsIncomingRequests(Arrays.asList(friends));
    }

    public void setFriendsIncomingRequests(Iterable<UUID> friends) {
        setList(PlayerData.FRIENDINCOMINGRQ, toStrings(friends));
        update();
    }

    private Set<String> toStrings(Iterable<?> objects) {
        Set<String> set = new HashSet<>();
        for (Object o : objects) {
            set.add(o.toString());
        }
        return set;
    }

    private void setList(String base, Iterable<String> friends) {
        getData().set(base, Joiner.on(",").join(friends));
    }

    public void setHubItem(String item, int id) {
        getData().set(PlayerData.ITEMSBASE + "." + item, id);
        update();
    }

    public void setHubItemUsable(String item, boolean b){
        getData().set(PlayerData.ITEMALLOWED + "." + item,b);
    }

    public void setNick(String nick) {
        getData().set(PlayerData.NICKNAME, nick);
        update();
    }

    public String getNickName() {
        String nick;
        try {
            nick = getData().getString(PlayerData.NICKNAME);
        } catch (InvalidBaseException e) {
            return getName();
        }
        return ChatColor.translateAlternateColorCodes('&', nick);
    }

    //Defaults true
    public boolean isUsingGadgets() {
        try {
            return getData().getBoolean(PlayerData.GADGETS);
        } catch (InvalidBaseException e) {
            return true;
        }
    }

    public void setUsingGadgets(boolean usingGadgets) {
        getData().set(PlayerData.GADGETS, usingGadgets);
        update();
    }

    public abstract String getName();


    public void update() {

    }
}
