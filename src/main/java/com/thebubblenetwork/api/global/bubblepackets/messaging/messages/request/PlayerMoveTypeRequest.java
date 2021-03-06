package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.PlayerMessage;
import com.thebubblenetwork.api.global.type.ServerType;

import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerMoveTypeRequest extends AbstractMessageObject implements PlayerMessage {
    private String player, servertype;

    public PlayerMoveTypeRequest(String player, ServerType servertype) {
        this.player = player;
        this.servertype = servertype.getName();
    }

    public PlayerMoveTypeRequest(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        player = in.readUTF();
        servertype = in.readUTF();
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(player);
        out.writeUTF(servertype);
    }

    public String getName() {
        return player;
    }

    public ServerType getServerType() {
        return ServerType.getType(servertype);
    }
}
