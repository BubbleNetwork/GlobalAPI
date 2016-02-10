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

    private Map data;


    public DataObject(Map data) {
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

    public Map getRaw() {
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
        check(indentifier, String.class);
        return (String) getRaw().get(indentifier);
    }


    protected void check(String indentifier, Class cast) throws InvalidBaseException {
        if (!getRaw().containsKey(indentifier))
            throw new InvalidBaseException("Could not find raw data: " + indentifier);
        if (!cast.isInstance(getRaw().get(indentifier)))
            throw new InvalidBaseException("Data could not be cast to: " + cast.getName());
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
        Map.Entry entry;
        Iterator<Map.Entry> entryIterator = getRaw().entrySet().iterator();
        while(entryIterator.hasNext()){
            entry = entryIterator.next();
            Object keyobject = entry.getKey();
            Object valueobject = entry.getValue();
            String keystring = keyobject instanceof String ? (String)keyobject : String.valueOf(keyobject);
            if(update.contains(keystring)){
                SQLUtil.update(connection,table,"value",valueobject,new SQLUtil.Where(keyname + "\" AND `key`=\"" + keyobject + "\""));
            }
            else if(!proccessed.contains(keystring)){
                SQLUtil.execute(connection,table,new ImmutableMap.Builder<String,Object>()
                        .put(key,keyname)
                        .put("key",keyobject)
                        .put("value",valueobject)
                        .build());
            }
        }
    }
}
