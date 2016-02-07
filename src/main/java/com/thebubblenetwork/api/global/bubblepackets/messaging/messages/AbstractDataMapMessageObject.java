package com.thebubblenetwork.api.global.bubblepackets.messaging.messages;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages
 * Date-created: 26/01/2016 19:47
 * Project: BubblePackets
 */
public abstract class AbstractDataMapMessageObject extends AbstractMessageObject implements DataMessage{
    private Map data;

    public AbstractDataMapMessageObject(Map data) {
        this.data = data;
    }

    public AbstractDataMapMessageObject(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        serializeInfo(in);
        int size = in.readInt();
        data = new HashMap<>();
        while(data.size() < size){
            int datasize = in.readInt();
            byte[] keybyte = new byte[datasize];
            for(int temp = 0;temp < datasize;temp++){
                keybyte[temp] = in.readByte();
            }
            datasize = in.readInt();
            byte[] valuebyte = new byte[datasize];
            for(int temp = 0;temp < datasize;temp++){
                valuebyte[temp] = in.readByte();
            }
            try {
                Object key = getObject(keybyte);
                Object value = getBytes(valuebyte);
                data.put(key,value);
            } catch (Exception e) {
                //Automatic Catch Statement
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            return bos.toByteArray();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public Object getObject(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        }
        finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }}

    public void parse(DataOutputStream out) throws IOException {
        parseInfo(out);
        out.writeInt(data.size());
        for(Map.Entry entry:getData().entrySet()){
            Object key = entry.getKey();
            byte[] keybytes = getBytes(key);
            out.writeInt(keybytes.length);
            for(byte b:keybytes)out.writeByte(b);
            Object value = entry.getValue();
            byte[] valuebytes = getBytes(value);
            out.writeInt(valuebytes.length);
            for(byte b:valuebytes)out.writeByte(b);
        }
    }

    public abstract void serializeInfo(ByteArrayDataInput in);

    public abstract void parseInfo(DataOutputStream out) throws IOException;

    public Map<?,?> getData(){
        return data;
    }
}
