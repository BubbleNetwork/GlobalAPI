package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.request;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 21/02/2016 {15:58}
 * Created February 2016
 */
public class InformationRequest extends AbstractMessageObject{
    private short type;

    public InformationRequest(short type) {
        this.type = type;
    }

    public InformationRequest(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        type = in.readShort();
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeShort(getInformationType());
    }

    public short getInformationType() {
        return type;
    }
}
