package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.AbstractDataMapMessageObject;

import java.io.DataOutputStream;
import java.io.IOException;
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
 * Package: com.thebubblenetwork.bubblepackets.messaging.messages.handshake
 * Date-created: 26/01/2016 19:28
 * Project: BubblePackets
 */
public class RankDataUpdate extends AbstractDataMapMessageObject {
    private String name;

    public RankDataUpdate(String name, Map<String, String> map) {
        super(map);
        this.name = name;
    }

    public RankDataUpdate(byte[] bytes) {
        super(bytes);
    }

    public void serializeInfo(ByteArrayDataInput in) {
        name = in.readUTF();
    }

    public void parseInfo(DataOutputStream out) throws IOException {
        out.writeUTF(name);
    }

    public String getName() {
        return name;
    }
}
