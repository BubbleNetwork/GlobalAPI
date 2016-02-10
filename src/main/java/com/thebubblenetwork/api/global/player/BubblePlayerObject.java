package com.thebubblenetwork.api.global.player;

import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.PlayerData;
import com.thebubblenetwork.api.global.plugin.BubbleHubObject;
import com.thebubblenetwork.api.global.ranks.Rank;
import de.mickare.xserver.util.ChatColor;

import java.util.*;

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
public abstract class BubblePlayerObject<T> implements BubblePlayer<T>{
    private static Map<UUID,BubblePlayerObject> playerObjectMap = new HashMap<>();

    public static BubblePlayer getObject(UUID u){
        return playerObjectMap.get(u);
    }

    public static Map<UUID, BubblePlayerObject> getPlayerObjectMap() {
        return playerObjectMap;
    }

    private UUID u;
    private PlayerData data;
    private T player;

    protected BubblePlayerObject(UUID u,PlayerData data){
        this.u = u;
        this.data = data;
    }

    public UUID getUUID(){
        return u;
    }

    public PlayerData getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public T getPlayer(){
        if(player == null){
            player = (T)(BubbleHubObject.getInstance().getPlayer(getUUID()));
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
            BubbleHubObject.getInstance().logSevere(e.getMessage());
            return Rank.getDefault();
        }
        return Rank.getRank(s);
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
            if (r != null)
                ranks.add(r);
        }
        return ranks.toArray(new Rank[0]);
    }

    public UUID[] getFriends() {
        try {
            return getData().getUUIDList(PlayerData.FRIENDSLIST);
        } catch (InvalidBaseException e) {
            return new UUID[0];
        }
    }

    public UUID[] getFriendIncomingRequests() {
        try {
            return getData().getUUIDList(PlayerData.FRIENDINCOMINGRQ);
        } catch (InvalidBaseException e) {
            return new UUID[0];
        }
    }

    public UUID[] getFriendOutgoingRequests() {
        try {
            return getData().getUUIDList(PlayerData.FRIENDOUTGOINGRQ);
        } catch (InvalidBaseException e) {
            return new UUID[0];
        }
    }

    public Map<String, Integer> getStats(String game) {
        return getData().getMap(PlayerData.STATSBASE, game);
    }

    public Map<String, Integer> getKits(String game) {
        return getData().getMap(PlayerData.KITBASE, game);
    }

    public Map<String, Integer> getHubItems() {
        return getData().getMapRaw(PlayerData.ITEMSBASE);
    }

    public Map<String, Integer> getPacks() {
        return getData().getMapRaw(PlayerData.PACKS);
    }


    public Map<String, Integer> getCurrency() {
        return getData().getMapRaw(PlayerData.CURRENCYBASE);
    }

    public int getCrystals(){
        Map<String, Integer> currency = getCurrency();
        return currency.containsKey(PlayerData.CRYSTALS) ? currency.get(PlayerData.CRYSTALS) : 0;
    }

    public int getTokens() {
        Map<String, Integer> currency = getCurrency();
        return currency.containsKey(PlayerData.TOKENS) ? currency.get(PlayerData.TOKENS) : 0;
    }

    public boolean isAuthorized(String permission) {
        return getRank().isAuthorized(permission);
    }

    public void setData(Map<String,String> data) {
        getData().getRaw().clear();
        for(Map.Entry<String,String> e: data.entrySet()){
            getData().getRaw().put(e.getKey(),e.getValue());
        }
    }

    public abstract String getName();

    public String getNickName(){
        String nick;
        try {
            nick = getData().getString(PlayerData.NICKNAME);
        } catch (InvalidBaseException e) {
            return getName();
        }
        return ChatColor.translateAlternateColorCodes('&',nick);
    }
}
