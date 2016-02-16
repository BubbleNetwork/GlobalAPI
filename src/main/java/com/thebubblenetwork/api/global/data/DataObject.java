package com.thebubblenetwork.api.global.data;

import com.google.common.base.Joiner;
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
    public void save(String table,String var,Object object) throws SQLException,ClassNotFoundException{
        SQLConnection connection = BubbleHubObject.getInstance().getConnection();
        connection.executeSQL("DELETE FROM `" + table + "` WHERE " + new SQLUtil.WhereVar(var,object).getWhere());
        String preset = "INSERT INTO `" + table + "` (`" + var + "`,`key`,`value`) VALUES ";
        Set<String> stringSet = new HashSet<>();
        for(Map.Entry<String,String> stringEntry:getRaw().entrySet()){
            stringSet.add("('" + object + "','" + stringEntry.getKey() + "','" + stringEntry.getValue() + "')");
        }
        String insert = Joiner.on(",").join(stringSet) + ";";
        connection.executeSQL(preset + insert);
    }

    public void set(String s,String s2){
        getRaw().put(s,s2);
    }

    public void set(String s,int i){
        getRaw().put(s,String.valueOf(i));
    }

    public void set(String s,boolean b){
        getRaw().put(s,String.valueOf(b));
    }
}
