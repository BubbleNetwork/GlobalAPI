package com.thebubblenetwork.api.global.ftp;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FTPConnection {
    private FtpClient client = null;
    private InetSocketAddress address;

    public FTPConnection(String ip, int port){
        address = new InetSocketAddress(ip, port);
    }

    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    public boolean isLoggedIn(){
        return isConnected() && client.isLoggedIn();
    }

    public void connect() throws FtpProtocolException, IOException{
        client = FtpClient.create(address);
    }

    public void login(String username, String password) throws FtpProtocolException, IOException{
        if(isConnected()) {
            client.login(username, password.toCharArray());
        }
    }

    public void abort() throws IOException, FtpProtocolException{
        if(isConnected()){
            client.abort();
            close();
        }
    }

    public FtpClient getClient(){
        return client;
    }

    public void close() throws IOException{
        try {
            client.close();
        }
        finally {
            client = null;
        }
    }

    public InetSocketAddress getAddress(){
        return address;
    }
}
