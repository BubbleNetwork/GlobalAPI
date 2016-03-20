package com.thebubblenetwork.api.global.website;

import com.google.common.base.Joiner;
import com.thebubblenetwork.api.global.sql.SQLUtil;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 20/03/2016 {14:19}
 * Created March 2016
 */
public class NamelessUser {
    private NamelessAPI api;
    private UUID u;

    public NamelessUser(NamelessAPI api, UUID u){
        this.api = api;
        this.u = u;
    }

    public Object[] getSafe(String... s){
        if(s.length == 0)return new Object[0];
        Object[] array = new Object[s.length];
        String ANDSQL = Joiner.on("` AND `").join(s);
        ResultSet set = null;
        try{
            set = api.getConnection().querySQL("SELECT `" + ANDSQL + "` FROM `" + NamelessAPISettings.USER_TABLE + "` WHERE `" + NamelessAPISettings.UUID + "`=\"" + u.toString().replace("-",""));
            if(!set.next())return array;
            for(int i = 0; i < s.length; i++){
                try {
                    array[i] = set.getObject(s[i]);
                }
                catch (Exception ex){
                }
            }
        }
        catch (Exception ex){
        }
        finally {
            if(set != null){
                try{
                    set.close();
                }
                catch (Exception ex){
                }
            }
        }
        return array;
    }

    public boolean setSafe(String s,Object o){
        try{
            SQLUtil.update(api.getConnection(), NamelessAPISettings.USER, s, o, new SQLUtil.WhereVar(NamelessAPISettings.UUID, u.toString().replace("-","")));
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    public boolean isRegistered(){
        return api.isRegistered(u) != null;
    }

    public boolean register(String name, String email, String ip){
        return api.setupPlayer(name, email, ip, u);
    }

    public String getResetCode(){
        return (String)getSafe(NamelessAPISettings.RESET)[0];
    }

    public String resetResetCode(){
        String resetCode = NamelessAPI.getRandomResetCode();
        setSafe(NamelessAPISettings.RESET, resetCode);
        return resetCode;
    }

    public Date getRegisteredDate(){
        return new Date(NamelessAPI.getMillisFromTimestamp((Integer)getSafe(NamelessAPISettings.JOINED)[0]));
    }

    public void setRegisteredDate(Date date){
        setRegisteredDate(date.getTime());
    }

    public void setRegisteredDate(long date){
        setSafe(NamelessAPISettings.JOINED, NamelessAPI.getUnixTimestamp(TimeUnit.MILLISECONDS, date));
    }

    public Date getLastOnline(){
        return new Date(NamelessAPI.getMillisFromTimestamp((Integer)getSafe(NamelessAPISettings.ONLINE)[0]));
    }

    public void setLastOnline(Date date){
        setRegisteredDate(date.getTime());
    }

    public void setLastOnline(long date){
        setSafe(NamelessAPISettings.ONLINE, NamelessAPI.getUnixTimestamp(TimeUnit.MILLISECONDS, date));
    }

    public String getForumName(){
        return (String)getSafe(NamelessAPISettings.USER)[0];
    }

    public String getSupposedIGN(){
        return (String)getSafe(NamelessAPISettings.IGN)[0];
    }

    public void setSupposedIGN(String ign){
        //Set both user and IGN
        setSafe(NamelessAPISettings.IGN, ign);
        setSafe(NamelessAPISettings.USER, ign);
    }

    public int getReputation(){
        return (int)getSafe(NamelessAPISettings.REPUTATION)[0];
    }

    public void setReputation(int reputation){
        setSafe(NamelessAPISettings.REPUTATION, reputation);
    }

    public boolean hasLoggedInWebsite(){
        Object[] objects = getSafe(NamelessAPISettings.JOINED,NamelessAPISettings.ONLINE);
        return (int)objects[1] != (int)objects[0];
    }
}
