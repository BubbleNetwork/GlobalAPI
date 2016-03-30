package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.response;

import com.google.common.io.ByteArrayDataInput;
import com.thebubblenetwork.api.global.anticheat.ViolationWrapper;
import com.thebubblenetwork.api.global.bubblepackets.messaging.AbstractMessageObject;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.PlayerMessage;

import java.io.DataOutputStream;
import java.io.IOException;

public class AntiCheatViolationMessage extends AbstractMessageObject implements PlayerMessage{
    private ViolationWrapper violationWrapper;

    public AntiCheatViolationMessage(ViolationWrapper wrapper) {
        violationWrapper = wrapper;
    }

    public AntiCheatViolationMessage(byte[] bytes) {
        super(bytes);
    }

    public void serialize(ByteArrayDataInput in) {
        violationWrapper = new ViolationWrapper(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble());
    }

    public void parse(DataOutputStream out) throws IOException {
        out.writeUTF(violationWrapper.getPlayer());
        out.writeUTF(violationWrapper.getViolation());
        out.writeDouble(violationWrapper.getTotalVL());
        out.writeDouble(violationWrapper.getAddedVL());
    }

    public String getName(){
        return violationWrapper.getPlayer();
    }
}
