package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServerShutdownRequest extends AbstractMessageObject {
    public ServerShutdownRequest() {
    }

    public ServerShutdownRequest(byte[] bytes) {
        super(bytes);
    }

    @Override
    public void serialize(ByteArrayDataInput in) {

    }

    @Override
    public void parse(DataOutputStream out) throws IOException {

    }
}
