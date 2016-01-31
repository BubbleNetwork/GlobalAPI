package com.thebubblenetwork.api.global.bubblepackets;

import de.mickare.xserver.net.XServer;

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
 * Date-created: 25/01/2016 00:49
 * Project: BubblePackets
 */
public class PacketInfo {
    private XServer server;
    private String channel;

    public PacketInfo(XServer server, String channel) {
        this.server = server;
        this.channel = channel;
    }

    public XServer getServer() {
        return server;
    }

    public String getChannel() {
        return channel;
    }

}
