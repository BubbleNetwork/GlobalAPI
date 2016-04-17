package com.thebubblenetwork.api.global.player;

import com.google.common.base.Joiner;
import com.thebubblenetwork.api.global.data.InvalidBaseException;
import com.thebubblenetwork.api.global.data.PlayerData;
import com.thebubblenetwork.api.global.data.PunishmentData;
import com.thebubblenetwork.api.global.data.RankData;
import com.thebubblenetwork.api.global.java.DateUTIL;
import com.thebubblenetwork.api.global.plugin.BubbleHub;
import com.thebubblenetwork.api.global.ranks.Rank;
import com.thebubblenetwork.api.global.website.NamelessUser;
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
    private NamelessUser user;

    protected BubblePlayer(UUID u, Map<String,String> rawdata) {
        this.u = u;
        data = new PlayerData(rawdata);
        user = new NamelessUser(BubbleHub.getInstance().getNameless(), u);
        try{
            getRankString();
        }
        catch (InvalidBaseException ex){
            setRank(Rank.getDefault());
        }

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
        finishChanges();
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

    private Set<String> toStrings(Iterable<?> objects) {
        Set<String> set = new HashSet<>();
        for (Object o : objects) {
            set.add(o.toString());
        }
        return set;
    }

    private void setList(String base, Iterable<String> friends) {
        getData().set(base, Joiner.on(",").join(friends));
        finishChanges();
    }

    public void setSubRanks(Iterable<Rank> subRanks) {
        setList(PlayerData.SUBRANKS, toStrings(subRanks));
        finishChanges();
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
            return getData().getNumber(PlayerData.KITBASE + "." + game + "." + kit).intValue();
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
        finishChanges();
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
    }

    public boolean isAuthorized(String permission) {
        return getRank().isAuthorized(permission);
    }

    public void incrementStat(String game, String indentifier, double increment){
        setStat(game, indentifier, getStats(game, indentifier) + increment);
    }

    public void setStat(String game, String indentifier, double id) {
        getData().set(PlayerData.STATSBASE + "." + game + "." + indentifier, id);
        finishChanges();
    }

    public void setKit(String game, String indentifier, int id) {
        getData().set(PlayerData.KITBASE + "." + game + "." + indentifier, id);
        finishChanges();
    }

    public void setHubItem(String item, int id) {
        getData().set(PlayerData.ITEMSBASE + "." + item, id);
        finishChanges();
    }

    public void setHubItemUsable(String item, boolean b){
        getData().set(PlayerData.ITEMALLOWED + "." + item,b);
        finishChanges();
    }

    public void setNick(String nick) {
        getData().set(PlayerData.NICKNAME, nick);
        finishChanges();
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
        finishChanges();
    }

    public boolean isSpectating(){
        try{
            return getData().getBoolean(PlayerData.SPECTATING);
        }
        catch (InvalidBaseException e){
            return false;
        }
    }

    public void setSpectating(boolean spectating){
        getData().set(PlayerData.SPECTATING, spectating);
        finishChanges();
    }

    public boolean canAfford(int tokens){
        return getTokens()-tokens > 0;
    }

    public NamelessUser getNamelessUser() {
        return user;
    }

    public abstract String getName();

    public void finishChanges(){
        if(isOnline())update();
        else save();
    }

    public void useReward(String reward){
        getData().set(PlayerData.REWARD + "." + reward, System.currentTimeMillis());
        finishChanges();
    }

    public boolean hasReward(String reward){
        return isAuthorized(PlayerData.REWARD + "." + reward);
    }

    public boolean canUseReward(String reward, long rewardtime){
        try{
            long date = getData().getNumber(PlayerData.REWARD + "." + reward).longValue();
            return (date + rewardtime) <= System.currentTimeMillis();
        }
        catch (InvalidBaseException ex){
            return true;
        }
    }

    public String getWaitTime(String reward, long rewardtime){
        try{
            long date = getData().getNumber(PlayerData.REWARD + "." + reward).longValue();
            return DateUTIL.formatDateDiff(date + rewardtime);
        }
        catch (InvalidBaseException ex){
            return "now";
        }
    }


    public boolean isOnline(){
        return true;
    }

    protected void save(){

    }

    protected void update() {
    }
}
