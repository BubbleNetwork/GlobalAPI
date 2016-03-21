package com.thebubblenetwork.api.global.website;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Chars;
import com.thebubblenetwork.api.global.sql.SQLConnection;
import com.thebubblenetwork.api.global.sql.SQLUtil;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 19/03/2016 {20:03}
 * Created March 2016
 */
public class NamelessAPI {
    private static List<Character> characterList = Chars.asList("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
    public static final String RESETPREFIX = "https://www.thebubblenetwork.com/change_password/?c=";

    private NamelessAPISettings settings;
    private SQLConnection deprecated;

    public NamelessAPI(NamelessAPISettings settings){
        this.settings = settings;
        deprecated = new SQLConnection(settings.getHost(),3306,settings.getDb(),settings.getUser(),settings.getPass());
    }

    public boolean setupPlayer(String player, String email, String ip, UUID u){
        String compresseduuid = u.toString().replace("-","");
        int now = getUnixTimestamp(TimeUnit.MILLISECONDS, System.currentTimeMillis());
        String resetcode = getRandomResetCode();
        try {
            return SQLUtil.execute(deprecated, NamelessAPISettings.USER_TABLE, new ImmutableMap.Builder<String, Object>()
                    .put(NamelessAPISettings.IGN, player)
                    .put(NamelessAPISettings.USER, player)
                    .put(NamelessAPISettings.BANNED, 0)
                    .put(NamelessAPISettings.AVATAR, 0)
                    .put(NamelessAPISettings.PASS, getRandomResetCode())
                    .put(NamelessAPISettings.RESET, resetcode)
                    .put(NamelessAPISettings.GROUP, 1)
                    .put(NamelessAPISettings.GRAVATAR, "")
                    .put(NamelessAPISettings.JOINED, now)
                    .put(NamelessAPISettings.REPUTATION, 0)
                    .put(NamelessAPISettings.ONLINE, now)
                    .put(NamelessAPISettings.IP, ip)
                    .put(NamelessAPISettings.ACTIVE, 1)
                    .put(NamelessAPISettings.UUID, compresseduuid)
                    .put(NamelessAPISettings.PASSMETH, "default")
                    .put(NamelessAPISettings.EMAIL, email)
                    .build());
        } catch (Exception e) {
        }
        return false;
    }

    public NamelessUser isRegistered(UUID u){
        String compresseduuid = u.toString().replace("-","");
        ResultSet set = null;
        try {
            set =  SQLUtil.query(deprecated, NamelessAPISettings.USER_TABLE, NamelessAPISettings.ONLINE, new SQLUtil.WhereVar(NamelessAPISettings.UUID, compresseduuid));
            return set.next() ? new NamelessUser(this, u) : null;
        } catch (Exception e) {
            return null;
        } finally {
            if (set != null) {
                try{
                    set.close();
                }
                catch (Exception ex) {
                }
            }
        }
    }

    public SQLConnection getConnection(){
        return deprecated;
    }

    public static boolean isEmail(String email){
        String[] split;
        return !email.isEmpty() && (split = email.split("\\@")).length == 2 && split[1].contains("\\.");
    }

    public static String getRandomResetCode(){
        List<Character> newList = new ArrayList<>(characterList);
        Collections.shuffle(newList);
        return new String(Chars.toArray(newList)).substring(0,60);
    }

    public static long getMillisFromTimestamp(int timestamp){
        return TimeUnit.SECONDS.toMillis(timestamp);
    }

    public static int getUnixTimestamp(TimeUnit unit,long time){
        return (int)unit.toSeconds(time);
    }
}
