package com.thebubblenetwork.api.global.bubblepackets.messaging.messages;

import com.thebubblenetwork.api.global.bubblepackets.messaging.IPluginMessage;

import java.util.UUID;

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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages
 * Date-created: 25/01/2016 00:15
 * Project: BubblePackets
 */
public interface PlayerMessage extends IPluginMessage {
    UUID getUUID();
}
