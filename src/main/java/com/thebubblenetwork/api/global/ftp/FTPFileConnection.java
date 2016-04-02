package com.thebubblenetwork.api.global.ftp;

import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FTPFileConnection extends AbstractFileConnection{
    private FtpClient client = null;
    private InetSocketAddress address;

    public FTPFileConnection(String ip, int port){
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
            client.login(username, password == null ? null : password.toCharArray());
        }
    }

    public void abort(){
        if(isConnected()){
            try {
                client.abort();
            } catch (FtpProtocolException e) {
            } catch (IOException e) {
            }
            close();
        }
    }

    public FtpClient getClient(){
        return client;
    }

    public void close(){
        try {
            client.close();
        }
        catch (Exception ex){

        }
        finally {
            client = null;
        }
    }

    public InputStream get(String remote) throws IOException, FtpProtocolException{
        if(!isLoggedIn()){
            throw new IllegalArgumentException("Not logged in");
        }
        return client.getFileStream(remote);
    }

    public void put(InputStream in, String remote) throws FtpProtocolException, IOException{
        if(!isLoggedIn()){
            throw new IllegalArgumentException("Not logged in");
        }
        client.putFile(remote, in, true);
    }

    public InetSocketAddress getAddress(){
        return address;
    }
}
