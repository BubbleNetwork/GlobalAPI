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
    private byte[] port;

    public FTPConnection(String ip, byte[] port){
        try {
            address = InetAddress.getByAddress(ip, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    public boolean isLoggedIn(){
        return isConnected() && client.isConnected();
    }

    public void connect() throws FTPConnectionClosedException, IOException{
        client.connect(address);
    }

    public void login(String username, String password) throws FTPConnectionClosedException, IOException{
        if(isConnected()) {
            client.login(username, password);
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
