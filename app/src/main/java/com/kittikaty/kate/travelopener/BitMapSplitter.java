package com.kittikaty.kate.travelopener;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by kate on 3/5/17.
 */

public class BitMapSplitter implements Serializable, Parcelable {

    BitMapSplitter[] bms;
    float centerBitMapX, centerBitMapY;
    byte openedMap;
    public byte done = 0;
    byte mainExists;
    String id;
    byte picture;
    byte pictureOption;
    byte pictureSuboption;

    public BitMapSplitter(float centerX, float centerY, String idN) {
        centerBitMapX = centerX;
        centerBitMapY = centerY;
        openedMap = 0;
        id = idN;
        bms = new BitMapSplitter[4];
        picture = 0;
        pictureOption = -1;
        pictureSuboption = -1;
    }

    public BitMapSplitter(byte openedMap, byte done, byte mainExists, String id, byte picture, byte pictureOption, byte pictureSuboption) {
        this.openedMap = openedMap;
        this.done = done;
        this.mainExists = mainExists;
        this.id = id;
        this.picture = picture;
        this.pictureOption = pictureOption;
        this.pictureSuboption = pictureSuboption;
        bms = new BitMapSplitter[4];
    }


    public void resetLocation(float x, float y) {
        checkLocations(x, y);
    }

    public void checkLocations(float currentX, float currentY) {

        if (checkIFBMSArrayCanBeNull()) return;

        CheckIFBlackCanBeRemoved();

        if (checkIfCoordinateIsInside(currentX, currentY)) {

            float newDistance = (getDistance(id.length())) / 2;

            if (checkIfMinimumSquareSizeIsReached(newDistance)) return;

            checkIfNeedToCreateNewBMS(newDistance);

            checkLocationsInAllBMS(currentX, currentY);

            if (checkIFBMSArrayCanBeNull()) return;

            setShortPathesInActivity(currentX, currentY);
            return;

        } else checkIfGenerateBlack();
    }

    //CHECK LOCATION METHODS

    private void checkIfNeedToCreateNewBMS(float newDistance) {
        if (bms[0] == null && 0 == done) {
            createBMSArray(newDistance);
        }
    }

    private boolean checkIfMinimumSquareSizeIsReached(float newDistance) {
        if (newDistance <= 0.000171661376953 && 0 == done) {
            done = 1;
            bms = null;
            refreshProgress();
            return true;
        }
        return false;
    }

    private void checkLocationsInAllBMS(float currentX, float currentY) {
        for (int i = 0; i < 4; i++) {

            if (0 == bms[i].done && bms[i].checkIfCoordinateIsInside(currentX, currentY))
                bms[i].resetLocation(currentX, currentY);
        }
    }

    private void checkIfGenerateBlack() {
        if (bms[0] == null && 0 == done && 0 == openedMap) {
            generateMap();
        }
    }

    private void setShortPathesInActivity(float currentX, float currentY) {
        switch (id.length()) {
            case 16:
                MapViewFragment.shortBMS[0] = cutAPath(currentX, currentY);
                break;
            case 18:
                MapViewFragment.shortBMS[1] = cutAPath(currentX, currentY);
                break;
            case 13:
                MapViewFragment.shortBMS[2] = cutAPath(currentX, currentY);
                break;
            case 7:
                MapViewFragment.shortBMS[3] = cutAPath(currentX, currentY);
                break;
        }
    }

    private void CheckIFBlackCanBeRemoved() {
        if (mainExists == 1) {
            openedMap = 1;
            removeMap();
        }
    }


    public BitMapSplitter cutAPath(float currentX, float currentY) {
        for (int i = 0; i < 4; i++) {
            if (bms[i].checkIfCoordinateIsInside(currentX, currentY)) {
                return bms[i];
            }
        }
        return null;
    }

    public boolean checkIfCoordinateIsInside(float x, float y) {
        float distance = getDistance(id.length());
        return y >= centerBitMapY - (distance * 2) && y <= centerBitMapY + (distance * 2) &&
                x >= centerBitMapX - distance && x <= centerBitMapX + distance;
    }

    public void removeMap() {
        if (MapViewFragment.gOverlays.containsKey(id)) {
            MapViewFragment.gOverlays.get(id).remove();
            MapViewFragment.gOverlays.remove(id);
        }
        mainExists = 0;
    }

    public void generateMap() {

        float distance = getDistance(id.length());
        mainExists = 1;

        if (centerBitMapX - distance == -90) {
            GroundOverlay minusGr = MapViewFragment.mMap.addGroundOverlay(new GroundOverlayOptions().positionFromBounds(
                    new LatLngBounds(new LatLng(centerBitMapX - distance + 0.001, centerBitMapY - distance * 2),
                            new LatLng(centerBitMapX + distance, centerBitMapY + distance * 2))).image(MapViewFragment.coinsBitMapDesrcriptors[4]));

            MapViewFragment.gOverlays.put(id, minusGr);

            return;
        }
        MapViewFragment.gOverlays.put(id,
                MapViewFragment.mMap.addGroundOverlay(new GroundOverlayOptions().positionFromBounds(
                        new LatLngBounds(new LatLng(centerBitMapX - distance, centerBitMapY - distance * 2),
                                new LatLng(centerBitMapX + distance, centerBitMapY + distance * 2))).image(MapViewFragment.coinsBitMapDesrcriptors[4])));
    }

    public void generateAll() {
        if (mainExists == 1) {
            generateMap();
            return;
        }
        if (done == 1) {
            if (picture == 1)
                generatePicture();
            return;
        }
        if (bms[0] != null)
            for (int i = 0; i < 4; i++) {
                if (bms[i] != null)
                    bms[i].generateAll();
            }

    }

    public void generatePicture() {
        addAPicture(pictureOption, pictureSuboption);
    }

    public void refreshProgress() {
        generateArea();
        generateAPicProbability();
    }

    public void generateArea() {
        float distance = getDistance(id.length());
        MainHolder.addDistance(centerBitMapX - distance, centerBitMapY - distance * 2, centerBitMapX + distance, centerBitMapY + distance * 2);
    }

    // Pictures:
    public void addAPicture(int option, int suboption) {
        picture = 1;
        GroundOverlay go = MapViewFragment.mMap.addGroundOverlay(new GroundOverlayOptions().position(new LatLng(centerBitMapX, centerBitMapY), 40f).image(MapViewFragment.allDescriptors[option][suboption]));
        go.setClickable(true);
        MapViewFragment.allPic.put(go, this);

    }

    public void generateAPicProbability() {
        int probability = new Random().nextInt(1000);

        //coin
        if (probability < 100) {
            generateACoin();
            //gift
        } else if (probability < 160) {
            generateAGift();
            // animal
        } else if (probability < 260) {
            generateAnExistingAnimal();

        }
    }

    public void generateACoin() {

        int probability = new Random().nextInt(1000);
        byte option = 0;
        if (probability < 151)
            option = 3;
        else if (probability < 351)
            option = 2;
        else if (probability < 601)
            option = 1;
        pictureOption = 0;
        pictureSuboption = option;
        addAPicture(0, option);
    }

    public void generateAnExistingAnimal() {

        byte existingAnimal = 0;
        byte option = (byte) new Random().nextInt(89);
        String unlockedAnimals = MainHolder.filesAccessor.getAnimalsUnlocked();

        while (existingAnimal == 0) {
            if (unlockedAnimals.charAt(option) == '1')
                existingAnimal = 1;
            else
                option = (byte) new Random().nextInt(89);
        }

        pictureOption = 1;
        pictureSuboption = option;
        addAPicture(1, option);
    }

    public void generateANotExistingAnimal() {

        byte notexistingAnimal = 0;
        byte option = (byte) new Random().nextInt(89);
        String unlockedAnimals = MainHolder.filesAccessor.getAnimalsUnlocked();
        if (unlockedAnimals.equals("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111")) {
            generateABonusCoin();
        }
        while (notexistingAnimal == 0) {
            if (unlockedAnimals.charAt(option) == '0')
                notexistingAnimal = 1;
            else
                option = (byte) new Random().nextInt(89);
        }

        pictureOption = 1;
        pictureSuboption = option;
        addAPicture(1, option);
    }

    public void generateABonusAnimal() {
        byte notexistingAnimalBonus = 0;
        byte option = (byte) new Random().nextInt(4);
        String unlockedAnimals = MainHolder.filesAccessor.getAnimalsUnlockedBonus();
        if (unlockedAnimals.equals("1111")) {
            generateABonusCoin();
            return;
        }
        while (notexistingAnimalBonus == 0) {
            if (unlockedAnimals.charAt(option) == '0')
                notexistingAnimalBonus = 1;
            else
                option = (byte) new Random().nextInt(4);
        }

        pictureOption = 3;
        pictureSuboption = option;
        addAPicture(3, option);
    }

    public void generateABonusCoin() {
        byte option = 2;
        if (new Random().nextInt(1001) > 700)
            option = 3;
        pictureOption = 0;
        pictureSuboption = option;
        addAPicture(0, option);
    }

    public void generateAGift() {
        picture = 1;
        byte option;
        int probability = new Random().nextInt(1000);
        if (probability < 750)
            option = 0;
        else if (probability < 950)
            option = 1;
        else option = 2;
        pictureOption = 2;
        pictureSuboption = option;
        addAPicture(2, option);
    }

    public void generateAGift(int giftNumber) {
        if (giftNumber == 0)
            generateABonusCoin();
        else if (giftNumber == 1)
            generateANotExistingAnimal();
        else generateABonusAnimal();
    }

    public int[] removePic() {
        int i1 = pictureOption, i2 = pictureSuboption;
        picture = 0;
        if (pictureOption == 0)
            MainHolder.filesAccessor.addCoins(pictureSuboption);
        else if (pictureOption == 1) {
            MainHolder.filesAccessor.setAnimal(pictureSuboption);
            MainHolder.filesAccessor.setAnimalsUnlocked(pictureSuboption);
        } else if (pictureOption == 3) {
            MainHolder.filesAccessor.setAnimalsUnlockedBonus(pictureSuboption);
        } else generateAGift(pictureSuboption);

        return new int[]{i1, i2};
    }


// BACKGROUND METHODS:

    public void resetLocationBackground(float x, float y) {

        checkLocationsBackground(x, y);
    }

    public void checkLocationsBackground(float currentX, float currentY) {

        if (checkIFBMSArrayCanBeNull()) return;
        if (mainExists == 1) {
            openedMap = 1;
            removeMapBackground();
        }

        if (checkIfCoordinateIsInside(currentX, currentY)) {

            float newDistance = (getDistance(id.length())) / 2;

            if (newDistance <= 0.000171661376953 && 0 == done) {
                done = 1;
                bms = null;
                refreshProgressBackground();
                return;
            }

            if (bms[0] == null && 0 == done) {
                createBMSArray(newDistance);
                for (int i = 0; i < 4; i++) {
                    bms[i].checkLocationsBackground(currentX, currentY);
                }

            } else {

                for (int i = 0; i < 4; i++) {

                    if (0 == bms[i].done && bms[i].checkIfCoordinateIsInside(currentX, currentY))
                        bms[i].resetLocationBackground(currentX, currentY);
                }
            }
            if (checkIFBMSArrayCanBeNull()) return;

        } else if (bms[0] == null && 0 == done && 0 == openedMap) {
            generateMapBackground();
        }
    }


    public void removeMapBackground() {
        mainExists = 0;
    }

    public void generateMapBackground() {
        mainExists = 1;
    }

    public void refreshProgressBackground() {
        generateAreaBackground();
        generateAPicProbabilityBackground();
    }

    public void generateAreaBackground() {

        float distance = getDistance(id.length());
        AlarmReceiver.filesAccessor.addDistance(centerBitMapX - distance, centerBitMapY - distance * 2, centerBitMapX + distance, centerBitMapY + distance * 2);
    }

// BACKGROUND PICTURE GENERATION

    public void generateAPicProbabilityBackground() {
        int probability = new Random().nextInt(1000);

        //coin
        if (probability < 100) {
            generateACoinBackground();
            //gift
        } else if (probability < 140) {
            generateAGiftBackground();
            // animal
        } else if (probability < 240) {
            generateAnExistingAnimalBackground();
        }
    }

    public void addAPictureBackground() {
        picture = 1;
    }

    public void generateACoinBackground() {

        int probability = new Random().nextInt(1000);
        byte option = 0;
        if (probability < 151)
            option = 3;
        else if (probability < 351)
            option = 2;
        else if (probability < 601)
            option = 1;
        pictureOption = 0;
        pictureSuboption = option;
        addAPictureBackground();
    }

    public void generateAnExistingAnimalBackground() {

        byte existingAnimal = 0;
        byte option = (byte) new Random().nextInt(89);
        String unlockedAnimals = AlarmReceiver.filesAccessor.getAnimalsUnlocked();

        while (existingAnimal == 0) {
            if (unlockedAnimals.charAt(option) == '1')
                existingAnimal = 1;
            else
                option = (byte) new Random().nextInt(89);
        }

        pictureOption = 1;
        pictureSuboption = option;
        addAPictureBackground();
    }

    public void generateAGiftBackground() {
        picture = 1;
        byte option;
        int probability = new Random().nextInt(1000);
        if (probability < 750)
            option = 0;
        else if (probability < 950)
            option = 1;
        else option = 2;
        pictureOption = 2;
        pictureSuboption = option;
        addAPictureBackground();
    }


// CONVERTING DISTANCE AND COORDINATES

    public float getDistance(int idLength) {
        switch (idLength) {
            case 1:
                return 45f;
            case 2:
                return 22.5f;
            case 3:
                return 11.25f;
            case 4:
                return 5.625f;
            case 5:
                return 2.8125f;
            case 6:
                return 1.40625f;
            case 7:
                return 0.703125f;
            case 8:
                return 0.3515625f;
            case 9:
                return 0.17578125f;
            case 10:
                return 0.087890625f;
            case 11:
                return 0.0439453125f;
            case 12:
                return 0.02197265625f;
            case 13:
                return 0.010986328125f;
            case 14:
                return 0.0054931640625f;
            case 15:
                return 0.00274658203125f;
            case 16:
                return 0.001373291015625f;
            case 17:
                return 6.866455078125E-4f;
            case 18:
                return 3.4332275390625E-4f;
            case 19:
                return 1.71661376953125E-4f;
            case 20:
                return 8.58306884765625E-5f;


        }
        return 45;
    }

    public void resetCenterBitmapXY(float x, float y) {
        centerBitMapX = x;
        centerBitMapY = y;
        if (bms != null && bms[0] != null) {
            bms[0].resetCenterBitmapXY(x - getDistance(id.length()) / 2, y - getDistance(id.length()));
            bms[1].resetCenterBitmapXY(x - getDistance(id.length()) / 2, y + getDistance(id.length()));
            bms[2].resetCenterBitmapXY(x + getDistance(id.length()) / 2, y - getDistance(id.length()));
            bms[3].resetCenterBitmapXY(x + getDistance(id.length()) / 2, y + getDistance(id.length()));
        }
    }


// ARRAY REGENERATION METHODS

    public void setBMS() {
        if (!MapViewFragment.bitMapArray.isEmpty() && MapViewFragment.bitMapArray.checkId(id + 1)) {
            for (int i = 0; i < 4; i++) {
                bms[i] = MapViewFragment.bitMapArray.get();
            }
            for (int i = 0; i < 4; i++) {
                bms[i].setBMS();
            }

        }
    }

    public void setBMSBackground() {
        if (!AlarmReceiver.bitMapArray.isEmpty() && AlarmReceiver.bitMapArray.checkId(id + 1)) {
            for (int i = 0; i < 4; i++) {
                bms[i] = AlarmReceiver.bitMapArray.get();
            }
            for (int i = 0; i < 4; i++) {
                bms[i].setBMSBackground();
            }
        }
    }

    public void addToArray() {
        if (bms != null && bms[0] != null) {
            for (int i = 0; i < 4; i++) {
                SavingService.bitMapArray.add(bms[i]);
            }

            for (int i = 0; i < 4; i++) {
                bms[i].addToArray();
            }


        }
    }


    // PARCEABLE JOB:
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.bms, flags);
        dest.writeByte(this.openedMap);
        dest.writeByte(this.done);
        dest.writeByte(this.mainExists);
        dest.writeString(this.id);
        dest.writeByte(this.picture);
        dest.writeByte(this.pictureOption);
        dest.writeByte(this.pictureSuboption);
    }

    protected BitMapSplitter(Parcel in) {
        this.bms = in.createTypedArray(BitMapSplitter.CREATOR);
        this.openedMap = in.readByte();
        this.done = in.readByte();
        this.mainExists = in.readByte();
        this.id = in.readString();
        this.picture = in.readByte();
        this.pictureOption = in.readByte();
        this.pictureSuboption = in.readByte();
    }

    public static final Parcelable.Creator<BitMapSplitter> CREATOR = new Parcelable.Creator<BitMapSplitter>() {
        @Override
        public BitMapSplitter createFromParcel(Parcel source) {
            return new BitMapSplitter(source);
        }

        @Override
        public BitMapSplitter[] newArray(int size) {
            return new BitMapSplitter[size];
        }
    };


// COMMON USED METHODS

    private void createBMSArray(float newDistance) {
        bms[0] = new BitMapSplitter(centerBitMapX - newDistance, centerBitMapY - newDistance * 2, id + 1);
        bms[1] = new BitMapSplitter(centerBitMapX - newDistance, centerBitMapY + newDistance * 2, id + 2);
        bms[2] = new BitMapSplitter(centerBitMapX + newDistance, centerBitMapY - newDistance * 2, id + 3);
        bms[3] = new BitMapSplitter(centerBitMapX + newDistance, centerBitMapY + newDistance * 2, id + 4);
    }

    private boolean checkIFBMSArrayCanBeNull() {
        if (bms[0] != null && bms[0].done == 1 && bms[1].done == 1 && bms[2].done == 1 && bms[3].done == 1
                && bms[0].picture == 0 && bms[1].picture == 0 &&
                bms[2].picture == 0 && bms[3].picture == 0) {
            done = 1;
            bms = null;
            return true;
        }
        return false;
    }

}
