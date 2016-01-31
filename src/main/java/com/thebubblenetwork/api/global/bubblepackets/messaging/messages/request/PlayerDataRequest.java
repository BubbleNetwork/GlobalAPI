package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.PlayerMessage;

import java.io.DataOutputStream;
import java.io.IOException;
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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages.request
 * Date-created: 25/01/2016 00:14
 * Project: BubblePackets
 */
public class PlayerDataRequest extends AbstractMessageObject implements PlayerMessage {
    private UUID u;

    public PlayerDataRequest(UUID u){
        super();
        this.u = u;
    }

    public PlayerDataRequest(byte[] bytes){
        super(bytes);
    }


    public void serialize(ByteArrayDataInput in) {
        this.u = UUID.fromString(in.readUTF());
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(String.valueOf(u));
    }

    public UUID getUUID(){
        return u;
    }
}
