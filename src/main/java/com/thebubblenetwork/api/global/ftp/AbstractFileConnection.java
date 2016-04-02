package com.thebubblenetwork.api.global.ftp;

import java.io.InputStream;
import java.lang.reflect.Constructor;

public abstract class AbstractFileConnection {
    public static <T extends AbstractFileConnection> T create(Class<T> clazz,String host, int port){
        Constructor<T> constructor;
        try {
            constructor = clazz.getConstructor(String.class, int.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Invalid constructor", e);
        }
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(host, port);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid constructor", e);
        }
    }

    public abstract boolean isLoggedIn();
    public abstract boolean isConnected();
    public abstract void connect() throws Exception;
    public abstract void login(String username, String password) throws Exception;
    public abstract InputStream get(String remote) throws Exception;
    public abstract void put(InputStream in, String remote) throws Exception;
    public abstract void abort();
    public abstract void close();
}
