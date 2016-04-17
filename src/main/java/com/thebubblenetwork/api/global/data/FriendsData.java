package com.thebubblenetwork.api.global.data;

import com.google.common.base.Joiner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FriendsData extends DataObject{
    public static final String CURRENT = "friends", INVITE = "invite";
    public static String table = "friends";

    public FriendsData(Map<String, String> data) {
        super(data);
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

    public static Set<String> toStrings(Iterable<?> objects) {
        Set<String> set = new HashSet<>();
        for (Object o : objects) {
            set.add(o.toString());
        }
        return set;
    }

    public void setList(String base, Iterable<String> friends) {
        set(base, Joiner.on(",").join(friends));
    }
}
