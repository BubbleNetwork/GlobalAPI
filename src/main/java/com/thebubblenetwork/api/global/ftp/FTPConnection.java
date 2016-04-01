package com.thebubblenetwork.api.global.ftp;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class FTPConnection {
    private FTPClient client = new FTPClient();
    private InetAddress address;
    private String ip;
    private int port;
    private String user;
    private String pass;

    public FTPConnection(String ip, int port, String user, String pass){
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    public boolean isLoggedIn(){
        return isConnected() && client.isConnected();
    }

    public void connect() throws FTPConnectionClosedException, IOException{
        client.connect(address, port);
    }

    public void login() throws FTPConnectionClosedException, IOException{
        if(isConnected()) {
            client.login(user, pass);
        }
    }

    public void abort() throws IOException, FTPConnectionClosedException{
        if(isConnected()){
            client.abort();
            close();
        }
    }

    public FTPClient getClient(){
        return client;
    }

    public void close() throws IOException{
        try {
            //disconnect the ftp session
            client.disconnect();
        }
        finally {
            client = null;
        }
    }

    public InetAddress getAddress(){
        return address;
    }
}
