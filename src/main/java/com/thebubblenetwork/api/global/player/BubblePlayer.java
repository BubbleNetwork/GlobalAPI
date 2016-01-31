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
    Rank[] getSubRanks();
    int getTokens();
    int getCrystals();
    Map<String, Integer> getStats(String game);
    Map<String, Integer> getKits(String game);
    boolean isAuthorized(String permission);
    UUID getUUID();
    UUID[] getFriends();
    UUID[] getFriendIncomingRequests();
    UUID[] getFriendOutgoingRequests();
    void setData(Map<?,?> data);
    Map<String, Integer> getHubItems();
    Map<String, Integer> getPacks();
}
