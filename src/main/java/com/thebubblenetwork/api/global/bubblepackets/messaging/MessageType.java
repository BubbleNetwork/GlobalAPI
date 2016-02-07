package com.thebubblenetwork.api.global.bubblepackets.messaging;

import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake.AssignMessage;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake.PlayerCountUpdate;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request.PlayerDataRequest;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.response.PlayerDataResponse;

import java.lang.reflect.Constructor;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) The Bubble Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Wrote by Jacob Evans <jacobevansminor@gmail.com>, 01 2016
 * <p/>
 * <p/>
 * Class information
 * ---------------------
 * Package: com.thebubblenetwork.bubblebungee.servermanager.messaging
 * Date-created: 24/01/2016 21:58
 * Project: com.thebubblenetwork.bubblebungee
 */

public enum MessageType {
    A("Assign",AssignMessage.class),
    DATARQ("PlayerDataRequest", PlayerDataRequest.class),
    C("PlayerCountHandshake",PlayerCountUpdate.class),
    DATARE("PlayerDataResponse", PlayerDataResponse.class);

    public static MessageType getType(String s){
        for(MessageType type:values()){
            if(type.getName().equalsIgnoreCase(s))return type;
        }
        return null;
    }

    public static MessageType getType(Class<? extends IPluginMessage> clazz){
        for(MessageType type:values()){
            if(type.getClazz() == clazz)return type;
        }
        return null;
    }

    public static MessageType getType(IPluginMessage message){
        return getType(message.getClass());
    }

    private String name;
    private Class<? extends IPluginMessage> clazz;
    private Constructor<? extends IPluginMessage> constructor;
    MessageType(String name,Class<? extends IPluginMessage> clazz){
        this.name = name;
        this.clazz = clazz;
        try {
            constructor = getClazz().getConstructor(byte[].class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(clazz.getName() + " does not have a byte[] constructor");
        }
        constructor.setAccessible(true);
    }

    public String getName() {
        return name;
    }

    public Class<? extends IPluginMessage> getClazz(){
        return clazz;
    }

    public IPluginMessage newInstance(byte[] bytes) throws Throwable{
        try{
            return constructor.newInstance(new Object[]{bytes});
        }
        catch (Exception ex){
            throw ex.getCause();
        }
    }
}
