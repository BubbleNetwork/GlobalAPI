package com.thebubblenetwork.api.global.file;

import com.thebubblenetwork.api.global.ftp.FTPConnection;
import sun.net.ftp.FtpProtocolException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by Jacob on 13/12/2015.
 */
public class DownloadUtil {
    public static void download(File to, String address, CopyOption option) throws Exception {
        Files.copy(download(address), to.toPath(), option);
    }

    public static void download(File to, String address) throws Exception {
        download(to, address, StandardCopyOption.REPLACE_EXISTING);
    }

    public static InputStream download(String address) throws Exception {
        return new URL(address).openStream();
    }

    public static void download(File to, String address, FTPConnection connection, CopyOption option) throws IOException{
        Files.copy(download(address,connection), to.toPath(), option);
    }

    public static void download(File to, String address, FTPConnection connection) throws IOException{
        download(to, address, connection, StandardCopyOption.REPLACE_EXISTING);
    }

    public static InputStream download(String file, FTPConnection connection){
        if(connection.isLoggedIn()){
            try {
                return connection.getClient().getFileStream(file);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        throw new IllegalArgumentException("Not logged in");
    }
}
