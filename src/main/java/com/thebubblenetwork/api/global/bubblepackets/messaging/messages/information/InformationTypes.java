package com.thebubblenetwork.api.global.bubblepackets.messaging.messages.information;

/**
 * The Bubble Network 2016
 * GlobalAPI
 * 21/02/2016 {16:07}
 * Created February 2016
 */
public enum InformationTypes {
    LOBBYLIST;

    public short getType(){
        return (short)ordinal();
    }

    public static final InformationTypes TYPES[] = array();

    private static InformationTypes[] array(){
        InformationTypes[] array = new InformationTypes[values().length];
        for(InformationTypes types:values()){
            array[types.ordinal()] = types;
        }
        return array;
    }
}
