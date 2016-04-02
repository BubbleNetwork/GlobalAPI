package com.thebubblenetwork.api.global.ftp;

import com.jcraft.jsch.*;

import java.io.InputStream;

public class SSHFileConnection extends AbstractFileConnection{
    private JSch jSch = new JSch();
    private Session session = null;
    private String ip;
    private int port;
    private ChannelSftp channel;

    public SSHFileConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public void login(String username, String password) throws JSchException{
        session = jSch.getSession(username, ip, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        session.connect();
        channel = (ChannelSftp) session.openChannel("sftp");
    }

    public boolean isLoggedIn(){
        return isConnected() && session.isConnected() && channel != null && channel.isConnected();
    }


    public boolean isConnected(){
        return true;
    }

    public InputStream get(String remote) throws JSchException, SftpException{
        return channel.get(remote);
    }

    public void put(InputStream in, String remote) throws JSchException, SftpException {
        channel.put(in, remote);
    }

    public void connect(){
    }

    public void abort() {
        close();
    }

    public void close() {
        try {
            channel.exit();
        } catch (Exception e) {
        }
        try {
            session.disconnect();
        } catch (Exception e){

        }
    }
}
