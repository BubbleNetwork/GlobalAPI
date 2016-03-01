package com.thebubblenetwork.api.global.bubblepackets.messaging;

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
 * Package: com.thebubblenetwork.bubblebungee.servermanager
 * Date-created: 24/01/2016 21:57
 * Project: com.thebubblenetwork.bubblebungee
 */

public interface IPluginMessage {
    byte[] getBytes();

    void process(byte[] bytes);

    MessageType getType();
}
