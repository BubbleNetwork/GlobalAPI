package com.thebubblenetwork.api.global.website;

import com.google.common.primitives.Chars;
import com.thebubblenetwork.api.global.sql.SQLConnection;

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

    private WebAPISettings settings;
    private SQLConnection deprecated;

    public NamelessAPI(WebAPISettings settings){
        this.settings = settings;
        deprecated = new SQLConnection(settings.getHost(),3306,settings.getDb(),settings.getUser(),settings.getPass());
    }

    public String setupPlayer(String player, UUID u){
        String compresseduuid = u.toString().replace("-","");
        //TODO - setup
        throw new UnsupportedOperationException();
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

    public static int getUnixTimestamp(TimeUnit unit,long time){
        return (int)unit.toSeconds(time);
    }
}
