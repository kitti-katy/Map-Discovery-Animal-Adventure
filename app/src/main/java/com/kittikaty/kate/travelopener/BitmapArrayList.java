package com.kittikaty.kate.travelopener;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kate on 4/8/17.
 */

public class BitmapArrayList extends ArrayList<StoredSplitter> implements Serializable {
    public BitmapArrayList() {
    }

    public boolean add(BitMapSplitter o) {
        return super.add(new StoredSplitter(o.openedMap, o.done, o.mainExists, o.id, o.picture, o.pictureOption, o.pictureSuboption));
    }


    public BitMapSplitter get() {
        StoredSplitter o = super.get(0);
        BitMapSplitter bms = new BitMapSplitter(o.openedMap, o.done, o.mainExists, o.id, o.picture, o.pictureOption, o.pictureSuboption);
        remove(0);
        return bms;
    }

    @Override
    public StoredSplitter remove(int index) {
        return super.remove(index);
    }

    public boolean checkId(String id) {
        if (get(0).id.equals(id))
            return true;
        return false;
    }
}
