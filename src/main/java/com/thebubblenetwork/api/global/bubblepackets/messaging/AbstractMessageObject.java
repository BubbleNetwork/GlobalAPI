package com.thebubblenetwork.api.global.bubblepackets.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Copyright Statement
 * ----------------------
 * Copyright (C) The Bubble Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Wrote by Jacob Evans <jacobevansminor@gmail.com>, 01 2016
 * <p/>
 * <p/>
 * Class information
 * ---------------------
 * Package: com.thebubblenetwork.bubblebungee.servermanager.messaging
 * Date-created: 24/01/2016 22:02
 * Project: com.thebubblenetwork.bubblebungee
 */

public abstract class AbstractMessageObject implements IPluginMessage{
    private MessageType type;

    public AbstractMessageObject(){
        this.type = MessageType.getType(this);
    }

    public AbstractMessageObject(byte[] bytes){
        this();
        process(bytes);
    }

    public MessageType getType(){
        return type;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            parse(out);
            out.close();
        } catch (IOException e) {
            //Cannot be thrown
        }
        return stream.toByteArray();
    }

    public void process(byte[] bytes){
        ByteArrayDataInput stream = ByteStreams.newDataInput(bytes);
        serialize(stream);
    }

    public abstract void serialize(ByteArrayDataInput in);
    public abstract void parse(DataOutputStream out) throws IOException;
}
