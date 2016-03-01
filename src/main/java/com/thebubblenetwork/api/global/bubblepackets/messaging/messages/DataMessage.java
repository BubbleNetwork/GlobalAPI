package com.thebubblenetwork.api.global.bubblepackets.messaging.messages;


import com.thebubblenetwork.api.global.bubblepackets.messaging.IPluginMessage;

import java.util.Map;

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
 * Date-created: 26/01/2016 19:50
 * Project: BubblePackets
 */
public interface DataMessage extends IPluginMessage {
    Map<String, String> getData();
}
