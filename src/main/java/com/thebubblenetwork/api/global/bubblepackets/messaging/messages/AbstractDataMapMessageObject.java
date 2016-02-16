package com.thebubblenetwork.api.global.bubblepackets.messaging.messages;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.plugin.BubbleHubObject;

import java.io.*;
import java.util.HashMap;
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
 * Date-created: 26/01/2016 19:47
 * Project: BubblePackets
 */
public abstract class AbstractDataMapMessageObject extends AbstractMessageObject implements DataMessage{
    private Map<String,String> data;

    public AbstractDataMapMessageObject(Map data) {
        this.data = data;
    }

    public AbstractDataMapMessageObject(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        serializeInfo(in);
        int size = in.readInt();
        if(size == -1)data = null;
        else {
            data = new HashMap<>();
            for (int done = 0; done < size; done++) {
                try {
                    String key = in.readUTF();
                    String value = in.readUTF();
                    data.put(key, value);
                } catch (Exception e) {
                    BubbleHubObject.getInstance().logSevere(e.getMessage());
                    BubbleHubObject.getInstance().logSevere("Failed to proccess map object");
                    break;
                }
            }
        }
    }

    public void parse(DataOutputStream out) throws IOException {
        parseInfo(out);
        if(getData() == null){
            out.writeInt(-1);
        }
        else {
            out.writeInt(getData().size());
            for (Map.Entry<String, String> entry : getData().entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeUTF(entry.getValue());
            }
        }
    }

    public abstract void serializeInfo(ByteArrayDataInput in);

    public abstract void parseInfo(DataOutputStream out) throws IOException;

    public Map<String,String> getData(){
        return data;
    }
}
