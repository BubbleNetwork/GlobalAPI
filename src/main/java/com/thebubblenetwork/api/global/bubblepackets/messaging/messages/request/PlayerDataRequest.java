package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages.request
 * Date-created: 25/01/2016 00:14
 * Project: BubblePackets
 */
public class PlayerDataRequest extends AbstractMessageObject{
    private String name;

    public PlayerDataRequest(String name){
        super();
        this.name = name;
    }

    public PlayerDataRequest(byte[] bytes){
        super(bytes);
    }


    public void serialize(ByteArrayDataInput in) {
        this.name = in.readUTF();
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(name);
    }

    public String getName(){
        return name;
    }
}
