package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;

import java.io.DataOutputStream;
import java.io.IOException;

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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages.handshake
 * Date-created: 25/01/2016 00:18
 * Project: BubblePackets
 */
public class PlayerCountUpdate extends AbstractMessageObject {
    private int online;

    public PlayerCountUpdate(int online){
        super();
        this.online = online;
    }

    public PlayerCountUpdate(byte[] bytes){
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        online = in.readInt();
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeInt(online);
    }

    public int getOnline(){
        return online;
    }
}
