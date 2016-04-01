package com.thebubblenetwork.api.global.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PunishmentData extends DataObject {

    public static final String BANNED = "ban.banned", BANTIME = "ban.time", BANREASON = "ban.reason", BANBY = "ban.by", MUTED = "mute.muted", MUTETIME = "mute.time", MUTEREASON = "mute.reason", MUTEBY = "mute.by";

    public static String table = "punishments";

    public PunishmentData(Map<String, String> data) {
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

}
