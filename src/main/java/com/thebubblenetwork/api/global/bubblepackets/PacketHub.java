package com.thebubblenetwork.api.global.bubblepackets;

import com.thebubblenetwork.api.global.bubblepackets.messaging.IPluginMessage;
import com.thebubblenetwork.api.global.bubblepackets.messaging.MessageType;
import com.thebubblenetwork.api.global.plugin.BubbleHub;
import de.mickare.xserver.*;
import de.mickare.xserver.annotations.XEventHandler;
import de.mickare.xserver.events.XServerDisconnectEvent;
import de.mickare.xserver.events.XServerLoggedInEvent;
import de.mickare.xserver.events.XServerMessageIncomingEvent;
import de.mickare.xserver.exceptions.NotInitializedException;
import de.mickare.xserver.net.XServer;

import java.io.IOException;
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
 * Package: com.thebubblenetwork.bubblepackets
 * Date-created: 25/01/2016 00:31
 * Project: BubblePackets
 */
public class PacketHub implements XServerListener {
    private BubbleHub<?> plugin;
    private AbstractXServerManager manager;
    private XServer currentserver;

    private Set<PacketListener> listenerSet = new HashSet<>();

    public PacketHub(){
    }

    public void register(BubbleHub<?> plugin){
        this.plugin = plugin;
        try {
            manager = XServerManager.getInstance();
        }
        catch (NotInitializedException ex){
            findHard(plugin);
        }
        if(manager == null)findHard(plugin);
        manager.getEventHandler().registerListenerUnsafe(plugin.getPlugin(),this);
        currentserver = manager.getHomeServer();
    }

    private void findHard(BubbleHub<?> plugin){
        plugin.logInfo("Could not find XServer instance, using plugin dependency");
        try {
            manager = plugin.getXPlugin().getManager();
        }
        catch (NotInitializedException e){
            plugin.logSevere("Could not find XServer instance " + e);
            plugin.endSetup("Could not find XServerManager");
        }
    }

    public void sendMessage(XServer server,IPluginMessage message) throws IOException{
        server.sendMessage(manager.createMessage(message.getType().getName(),message.getBytes()));
    }

    public void registerListener(PacketListener listener){
        listenerSet.add(listener);
    }

    public void unregisterListener(PacketListener listener){
        listenerSet.remove(listener);
    }

    @XEventHandler
    public void onConnect(XServerLoggedInEvent e){
        if(!isValid(e.getServer()))return;
        for(PacketListener listener:listenerSet)listener.onConnect(new PacketInfo(e.getServer(),e.getChannel()));
    }

    @XEventHandler
    public void onDisconnect(XServerDisconnectEvent e){
        if(!isValid(e.getServer()))return;
        for(PacketListener listener:listenerSet)listener.onDisconnect(new PacketInfo(e.getServer(),e.getChannel()));
    }

    @XEventHandler
    public void onMessage(XServerMessageIncomingEvent e){
        Message m = e.getMessage();
        if(!isValid(m.getSender()))return;
        MessageType type;
        try{
            Class<?> possibleclass = Class.forName(m.getSubChannel());
            type = MessageType.register(possibleclass.asSubclass(IPluginMessage.class));
        }
        catch (Exception ex){
            plugin.logInfo("Could not parse packet in channel: " + m.getSubChannel());
            return;
        }
        IPluginMessage message;
        try {
            message = type.newInstance(m.getContent());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
        for(PacketListener listener:listenerSet)listener.onMessage(new PacketInfo(m.getSender(),e.getChannel()),message);
    }

    private String constructInfo(XServer server){
        return server.getName() + " @ " + server.getHost() + ":" + String.valueOf(server.getPort()) + " - " + server.getPassword();
    }

    public boolean isValid(XServer server){
        return server != currentserver && !server.getName().equals(currentserver.getName());
    }
}
