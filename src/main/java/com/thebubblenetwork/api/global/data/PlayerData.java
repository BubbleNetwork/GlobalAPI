package com.thebubblenetwork.api.global.data;

import java.util.*;

/**
 * Created by Jacob on 31/12/2015.
 */
public class PlayerData extends DataObject {

    public static final String NICKNAME = "nickname", RANKBASE = "rank", STATSBASE = "stats", FRIENDSBASE = "friends", ITEMSBASE = "items", ITEMALLOWED = ITEMSBASE + ".allowed", KITBASE = "kits", NAME = "name", TOKENS = "tokens", KEYS = "keys", MAINRANK = RANKBASE + ".mainrank", SUBRANKS = RANKBASE + ".subranks", FRIENDSLIST = FRIENDSBASE + ".current", FRIENDINCOMINGRQ = FRIENDSBASE + ".incoming", PETNAME = "petname", GADGETS = "gadgets",BANNED = "ban.banned", BANTIME = "ban.time", BANREASON = "ban.reason", BANBY = "ban.by", SPECTATING = "spectating", REWARD = "reward";

    public static String table = "playerdata";

    public PlayerData(Map<String, String> loaded) {
        super(loaded);
    }

    public UUID[] getUUIDList(String indentifier) throws InvalidBaseException {
        String[] list = getString(indentifier).split(",");
        Set<UUID> uuids = new HashSet<>();
        for (String s : list) {
            try {
                uuids.add(UUID.fromString(s));
            } catch (Exception ex) {
            }
        }
        return uuids.toArray(new UUID[uuids.size()]);
    }
}
