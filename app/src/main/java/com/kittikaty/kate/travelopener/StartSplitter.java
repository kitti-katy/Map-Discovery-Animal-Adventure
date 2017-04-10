package com.kittikaty.kate.travelopener;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by kate on 3/7/17.
 */

public class StartSplitter implements Serializable, Parcelable {
    BitMapSplitter[] bms;

    static final long serialVersionUID = 1566772230155061499L;

    public StartSplitter() {

        bms = new BitMapSplitter[4];
    }


    public void resetLocation(float x, float y) {

        checkLocations(x, y);
    }

    public void checkLocations(float currentX, float currentY) {
        if (bms[0] == null) {


            bms[0] = new BitMapSplitter(-45, -90, "1");
            bms[1] = new BitMapSplitter(-45, 90, "2");
            bms[2] = new BitMapSplitter(45, -90, "3");
            bms[3] = new BitMapSplitter(45, 90, "4");
            bms[0].checkLocations(currentX, currentY);
            bms[1].checkLocations(currentX, currentY);
            bms[2].checkLocations(currentX, currentY);
            bms[3].checkLocations(currentX, currentY);


            return;
        }

        bms[0].resetLocation(currentX, currentY);
        bms[1].resetLocation(currentX, currentY);
        bms[2].resetLocation(currentX, currentY);
        bms[3].resetLocation(currentX, currentY);
    }


    public void regenerateLocations() {
        regenerateCenterBitmapXY();
        for (int i = 0; i < 4; i++) {

            if (bms[i] != null) {
                bms[i].generateAll();
            }
        }
    }


    public void resetLocationBackground(float x, float y) {
        regenerateCenterBitmapXY();
        checkLocationsBackground(x, y);
    }

    public void checkLocationsBackground(float currentX, float currentY) {
        if (bms[0] == null) {
            bms[0] = new BitMapSplitter(-45, -90, "1");
            bms[1] = new BitMapSplitter(-45, 90, "2");
            bms[2] = new BitMapSplitter(45, -90, "3");
            bms[3] = new BitMapSplitter(45, 90, "4");
            bms[0].checkLocationsBackground(currentX, currentY);
            bms[1].checkLocationsBackground(currentX, currentY);
            bms[2].checkLocationsBackground(currentX, currentY);
            bms[3].checkLocationsBackground(currentX, currentY);
            return;
        }

        bms[0].resetLocationBackground(currentX, currentY);
        bms[1].resetLocationBackground(currentX, currentY);
        bms[2].resetLocationBackground(currentX, currentY);
        bms[3].resetLocationBackground(currentX, currentY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.bms, flags);

    }

    protected StartSplitter(Parcel in) {
        this.bms = in.createTypedArray(BitMapSplitter.CREATOR);

    }

    public static final Parcelable.Creator<StartSplitter> CREATOR = new Parcelable.Creator<StartSplitter>() {
        @Override
        public StartSplitter createFromParcel(Parcel source) {
            return new StartSplitter(source);
        }

        @Override
        public StartSplitter[] newArray(int size) {
            return new StartSplitter[size];
        }
    };

    public void regenerateCenterBitmapXY() {
        bms[0].resetCenterBitmapXY(-45, -90);
        bms[1].resetCenterBitmapXY(-45, 90);
        bms[2].resetCenterBitmapXY(45, -90);
        bms[3].resetCenterBitmapXY(45, 90);
    }


    public void setBMS() {
        // Log.d("SS","REached");
        if (!MapViewFragment.bitMapArray.isEmpty()) {
            // Log.d("SS","RESETTING");

            bms[0] = MapViewFragment.bitMapArray.get();
            bms[1] = MapViewFragment.bitMapArray.get();
            bms[2] = MapViewFragment.bitMapArray.get();
            bms[3] = MapViewFragment.bitMapArray.get();
            bms[0].setBMS();
            bms[1].setBMS();
            bms[2].setBMS();
            bms[3].setBMS();
        }
    }


    public void setBMSBackground() {
        if (AlarmReceiver.bitMapArray != null)

        {
            bms[0] = AlarmReceiver.bitMapArray.get();
            bms[1] = AlarmReceiver.bitMapArray.get();
            bms[2] = AlarmReceiver.bitMapArray.get();
            bms[3] = AlarmReceiver.bitMapArray.get();
            bms[0].setBMSBackground();
            bms[1].setBMSBackground();
            bms[2].setBMSBackground();
            bms[3].setBMSBackground();
        }
    }

    public void addToArray() {
        if (bms[0] != null) {
            //Log.d("SS","ARRAYADDED");
            SavingService.bitMapArray.add(bms[0]);
            SavingService.bitMapArray.add(bms[1]);
            SavingService.bitMapArray.add(bms[2]);
            SavingService.bitMapArray.add(bms[3]);
            bms[0].addToArray();
            bms[1].addToArray();
            bms[2].addToArray();
            bms[3].addToArray();
        }

    }


}



