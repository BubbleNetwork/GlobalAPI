package com.thebubblenetwork.api.global.data;

import com.google.common.collect.ImmutableMap;
import com.thebubblenetwork.api.global.plugin.BubbleHubObject;
import com.thebubblenetwork.api.global.sql.SQLConnection;
import com.thebubblenetwork.api.global.sql.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Jacob on 31/12/2015.
 */
public class DataObject {

    private Map<String,String> data;


    public DataObject(Map<String,String> data) {
        this.data = data;
    }

    public static Map loadData(ResultSet set) throws SQLException {
        Map datamap = new HashMap();
        while (set.next()) {
            datamap.put(set.getObject("key"), set.getObject("value"));
        }
        set.close();
        return datamap;
    }

    public Map<String,String> getRaw() {
        return data;
    }

    public Boolean getBoolean(String indentifier) throws InvalidBaseException {
        String s = getString(indentifier);
        return Boolean.parseBoolean(s);
    }

    public Number getNumber(String indentifier) throws InvalidBaseException {
        String s = getString(indentifier);
        try {
            return NumberFormat.getInstance().parse(s);
        } catch (NumberFormatException ex) {
            throw new InvalidBaseException(ex);
        } catch (ParseException ex) {
            throw new InvalidBaseException(ex);
        }
    }

    public String getString(String indentifier) throws InvalidBaseException {
        check(indentifier);
        return getRaw().get(indentifier);
    }


    protected void check(String indentifier) throws InvalidBaseException {
        if (!getRaw().containsKey(indentifier))
            throw new InvalidBaseException("Could not find raw data: " + indentifier);
    }

    @SuppressWarnings("unchecked")
    public void save(String table,String key,Object keyname) throws SQLException,ClassNotFoundException{
        SQLConnection connection = BubbleHubObject.getInstance().getConnection();
        ResultSet set = SQLUtil.query(connection,table,"*",new SQLUtil.WhereVar(key,keyname));
        Set<String> proccessed = new HashSet<>();
        Set<String> update = new HashSet<>();
        while(set.next()){
            String keyobject = set.getString("key");
            String valueobject = set.getString("value");
            if(getRaw().containsKey(keyobject)){
                if(getRaw().get(keyobject).equals(valueobject))proccessed.add(keyobject);
                else update.add(keyobject);
            }
            else set.deleteRow();
        }
        set.close();
        for (Map.Entry<String,String> entry:getRaw().entrySet()){
            String keystring = entry.getKey();
            String valuestring = entry.getValue();
            if(update.contains(keystring)){
                SQLUtil.update(connection,table,"value",valuestring,new SQLUtil.Where(keyname + "\" AND `key`=\"" + keystring + "\""));
            }
            else if(!proccessed.contains(keystring)){
                SQLUtil.execute(connection,table,new ImmutableMap.Builder<String,Object>()
                        .put(key,keyname)
                        .put("key",keystring)
                        .put("value",keystring)
                        .build());
            }
        }
    }
}
