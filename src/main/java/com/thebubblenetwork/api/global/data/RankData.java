package com.thebubblenetwork.api.global.data;

import java.util.Map;

/**
 * Created by Jacob on 31/12/2015.
 */
public class RankData extends DataObject {
    public static final String PREFIX = "prefix", SUFFIX = "suffix", INHERITANCE = "inherit";

    public RankData(Map<String, String> data) {
        super(data);
    }

}
