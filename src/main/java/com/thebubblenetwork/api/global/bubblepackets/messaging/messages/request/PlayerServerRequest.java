package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.type.ServerType;
import com.thebubblenetwork.api.global.type.ServerTypeObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerServerRequest extends AbstractMessageObject{
    private ServerType type;

    public PlayerServerRequest(ServerType type) {
        this.type = type;
    }

    public PlayerServerRequest(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        type = ServerTypeObject.getType(in.readUTF());
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(type.getName());
    }

    public ServerType getServerType() {
        return type;
    }
}
