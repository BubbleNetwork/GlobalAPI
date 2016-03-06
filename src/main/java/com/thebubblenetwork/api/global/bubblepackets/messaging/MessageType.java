package com.thebubblenetwork.api.global.bubblepackets.messaging;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

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
 * Package: com.thebubblenetwork.bubblebungee.servermanager.messaging
 * Date-created: 24/01/2016 21:58
 * Project: com.thebubblenetwork.bubblebungee
 */

public class MessageType {
    public static MessageType register(Class<? extends IPluginMessage> clazz) {
        MessageType type;
        if ((type = getType(clazz)) != null) {
            return type;
        }
        type = new MessageType(clazz.getName(), clazz);
        typeSet.add(type);
        return type;
    }

    public static MessageType getType(String s) {
        for (MessageType type : typeSet) {
            if (type.getName().equalsIgnoreCase(s)) {
                return type;
            }
        }
        return null;
    }

    public static MessageType getType(Class<? extends IPluginMessage> clazz) {
        for (MessageType type : typeSet) {
            if (type.getClazz() == clazz) {
                return type;
            }
        }
        return null;
    }

    public static MessageType getType(IPluginMessage message) {
        return getType(message.getClass());
    }

    private static Set<MessageType> typeSet = new HashSet<>();
    private String name;
    private Class<? extends IPluginMessage> clazz;
    private Constructor<? extends IPluginMessage> constructor;

    private MessageType(String name, Class<? extends IPluginMessage> clazz) {
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

    public Class<? extends IPluginMessage> getClazz() {
        return clazz;
    }

    public IPluginMessage newInstance(byte[] bytes) throws Throwable {
        try {
            return constructor.newInstance(new Object[]{bytes});
        } catch (Exception ex) {
            throw ex.getCause();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageType) {
            MessageType type = (MessageType) o;
            return type.getName().equalsIgnoreCase(getName()) || type.getClazz().getName().equals(type.getClazz().getName());
        }
        return false;
    }
}
