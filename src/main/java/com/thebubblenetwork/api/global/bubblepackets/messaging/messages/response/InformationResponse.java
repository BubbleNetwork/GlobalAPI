package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.response;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 21/02/2016 {16:00}
 * Created February 2016
 */
public class InformationResponse extends AbstractMessageObject{

    private short type;
    private byte[] info;

    public InformationResponse() {
    }

    public InformationResponse(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        type = in.readShort();
        int len = in.readInt();
        info = new byte[len];
        for(;len > 0;len--){
            info[len] = in.readByte();
        }
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeShort(getInformationType());
        int len = info.length;
        out.writeInt(len);
        for(;len > 0;len--){
            out.writeByte(info[len]);
        }
    }

    public short getInformationType() {
        return type;
    }

    public byte[] getInfo() {
        return info;
    }
}
