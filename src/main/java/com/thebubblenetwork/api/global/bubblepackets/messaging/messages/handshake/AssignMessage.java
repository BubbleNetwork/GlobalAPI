package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.type.ServerType;

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
 * Date-created: 24/01/2016 22:10
 * Project: com.thebubblenetwork.bubblebungee
 */
public class AssignMessage extends AbstractMessageObject {
    private int id;
    private ServerType type;

    public AssignMessage(int id, ServerType type) {
        super();
        this.id = id;
        this.type = type;
    }

    public AssignMessage(byte[] bytes){
        super(bytes);
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeInt(id);
        out.writeUTF(type.getName());
    }

    public void serialize(ByteArrayDataInput in){
        this.id = in.readInt();
        this.type = ServerType.getType(in.readUTF());
    }

    public int getId() {
        return id;
    }

    public ServerType getWrapperType() {
        return type;
    }
}
