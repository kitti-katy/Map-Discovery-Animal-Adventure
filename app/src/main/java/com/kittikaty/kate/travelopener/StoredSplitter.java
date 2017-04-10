package com.kittikaty.kate.travelopener;

import java.io.Serializable;

/**
 * Created by kate on 4/8/17.
 */

public class StoredSplitter implements Serializable {
    byte openedMap, done, mainExists;
    String id;
    byte picture, pictureOption, pictureSuboption;

    public StoredSplitter(byte openedMap, byte done, byte mainExists, String id, byte picture, byte pictureOption, byte pictureSuboption) {
        this.openedMap = openedMap;
        this.done = done;
        this.mainExists = mainExists;
        this.id = id;
        this.picture = picture;
        this.pictureOption = pictureOption;
        this.pictureSuboption = pictureSuboption;
    }
}
