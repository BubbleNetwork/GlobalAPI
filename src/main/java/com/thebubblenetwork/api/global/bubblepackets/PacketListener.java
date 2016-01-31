package com.thebubblenetwork.api.global.bubblepackets;

import com.thebubblenetwork.api.global.bubblepackets.messaging.IPluginMessage;

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
 * Date-created: 25/01/2016 00:38
 * Project: BubblePackets
 */
public interface PacketListener {
    void onMessage(PacketInfo info, IPluginMessage message);
    void onConnect(PacketInfo info);
    void onDisconnect(PacketInfo info);
}
