package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.PlayerMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerMoveRequest extends AbstractMessageObject implements PlayerMessage {
    private String name;
    private String to;

    public PlayerMoveRequest(String player, String to) {
        this.name = player;
        this.to = to;
    }

    public PlayerMoveRequest(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        name = in.readUTF();
        to = in.readUTF();
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(name);
        out.writeUTF(to);
    }

    public String getName() {
        return name;
    }

    public String getTo() {
        return to;
    }
}
