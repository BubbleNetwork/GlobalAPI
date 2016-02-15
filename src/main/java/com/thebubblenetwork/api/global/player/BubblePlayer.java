package com.thebubblenetwork.api.global.player;

import com.thebubblenetwork.api.global.data.PlayerData;
import com.thebubblenetwork.api.global.ranks.Rank;

import java.util.Map;
import java.util.UUID;

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
 * Date-created: 26/01/2016 20:14
 * Project: GlobalAPI
 */
public interface BubblePlayer<T> {
    PlayerData getData();
    T getPlayer();
    Rank getRank();
    void setRank(Rank rank);
    Rank[] getSubRanks();
    void setSubRanks(Rank... subRanks);
    void setSubRanks(Iterable<Rank> subRanks);
    int getTokens();
    void setTokens(int tokens);
    Map<String, Integer> getStats(String game);
    void setStat(String game,String identifier,int id);
    Map<String, Integer> getKits(String game);
    void setKit(String game,String indentifier,int id);
    boolean isAuthorized(String permission);
    UUID getUUID();
    UUID[] getFriends();
    void setFriends(UUID... friends);
    void setFriends(Iterable<UUID> friends);
    UUID[] getFriendIncomingRequests();
    void setFriendsIncomingRequests(UUID... friends);
    void setFriendsIncomingRequests(Iterable<UUID> friends);
    void setData(Map<String,String> data);
    Map<String, Integer> getHubItems();
    void setHubItem(String item,int id);
    Map<String, Integer> getPacks();
    void setPacks(String pack,int amount);
    String getNickName();
    void setNick(String nick);
    void save();
}
