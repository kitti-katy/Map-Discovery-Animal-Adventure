package com.kittikaty.kate.travelopener;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * FilesAccessor accesses all of the shared preferences variables and manipulate them
 */

public class FilesAccessor {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;




    public FilesAccessor(Context context) {
        sp = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        editor = sp.edit();
    }


    public void addCoins(int coinNumber) {

        int value;
        if (coinNumber == 0)
            value = 1;
        else if (coinNumber == 1)
            value = 5;
        else if (coinNumber == 2)
            value = 10;
        else value = 25;

        editor.putInt("TotalCoinsCollected", getCoinsNumber()[0]+value);
        editor.putInt("CoinsCurrentBalance", getCoinsNumber()[1]+value);
        editor.commit();
        checkIfTimeToUpgrade();
    }

    public void addAreaOpened(float newArea) {
        editor.putFloat("AreaOpened", getAreaOpened() + newArea);
        editor.commit();
        checkIfTimeToUpgrade();
    }

    public void upgradeLevel() {
        editor.putInt("level", getLevel()+1);
        editor.putInt("TotalCoinsCollectedRequired", (int) ((double) getCoinsNumberRequired() * 1.2 + getCoinsNumberRequired()));
        editor.putFloat("AreaOpenedRequired",  getAreaOpenedRequired() * 1.2f +  getAreaOpenedRequired());
        editor.putInt("animalsRequired", (int) ((double) getAnimalRequired() * 1.2) + getAnimalRequired());
        editor.commit();
        new myMessagingService().onMessageReceived(new RemoteMessage.Builder("Level up! You reached level " + getLevel() + "!").build());

    }

    public void checkIfTimeToUpgrade() {

        if (getCoinsNumber()[0] >= getCoinsNumberRequired()&& getAreaOpened() >=  getAreaOpenedRequired() && getAllAnimal() >= getAnimalRequired())
            upgradeLevel();
        checkUIUpdates();
    }

    public int getLevel() {
        return sp.getInt("level", 1);
    }

    public float getAreaOpened() {
        return sp.getFloat("AreaOpened", 0);
    }

    public int[] getCoinsNumber() {
        return new int[]{sp.getInt("TotalCoinsCollected", 0),
                sp.getInt("CoinsCurrentBalance", 0)};
    }

    public float getAreaOpenedRequired() {
        return sp.getFloat("AreaOpenedRequired", 4000);
    }

    public int getCoinsNumberRequired() {
        return sp.getInt("TotalCoinsCollectedRequired", 50);
    }


    public void decreaseCurrentBalance(int value) {

        editor.putInt("CoinsCurrentBalance", getCoinsNumber()[1]-value);
        editor.commit();
    }

/**
 *
 *
 */
    private double rad(double x) {
        return x * Math.PI / 180;
    }

    // Calculating area opened in square meters
    public void addDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6378137;
        double dLat = rad(lat2 - lat1);
        double dLong = rad(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(lng1)) * Math.cos(rad(lng2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        addAreaOpened((float) d);
    }

    public void setZeroArea() {
        editor.putFloat("AreaOpened", 0);
        editor.commit();
    }

    public boolean getBackground() {
        return sp.getBoolean("background", false);
    }

    public void setBackground(boolean bg) {
        editor.putBoolean("background", bg);
        editor.commit();
    }

    public int getCircleLevel() {
        return sp.getInt("UpgradeCircleLevel", 1);
    }

    public void upgradeCircleLevel() {
        editor.putInt("UpgradeCircleLevel", sp.getInt("UpgradeCircleLevel", 1) + 1);
        editor.commit();
    }

    public void checkUIUpdates() {
        if (getBackground())
            return;
        //  Log.d("renewUI","YES");
        MainHolder.setUiAchievements();


    }


    public void setInsideBackgroundStop(boolean bg) {
        editor.putBoolean("backgroundInside", bg);
        editor.commit();
    }

    private final String[] ANIMALS = new String[]{"asia", "bear", "bull", "canard", "cat", "chamois",
            "cow", "deer", "dog", "elephant", "elk", "enot", "hippo", "horse", "kangaroo", "leopard",
            "monkey", "pig", "pten", "snail", "sheep", "squirrel", "tiger", "turtle", "tusk", "whitebear", "zebra"};
    private final String[] BONUS_ANIMALS = new String[]{"owlfamily", "stuffedanimal", "teddybear", "whale"};

    public String getAnimalsUnlocked() {
        return sp.getString("animalsUnlocked", "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
    }

    public void setAnimalsUnlocked(int index) {
        String newUnlocked = sp.getString("animalsUnlocked", "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        newUnlocked = newUnlocked.substring(0, index) + 1 + newUnlocked.substring(index + 1);
        editor.putString("animalsUnlocked", newUnlocked);
        editor.commit();
    }

    public void setAllAnimals() {
        editor.putInt("allanimals", sp.getInt("allanimals", 0) + 1);
        editor.commit();
    }

    public int getAllAnimal() {
        return sp.getInt("allanimals", 0);

    }


    public void setAnimal(int index) {
        setAllAnimals();
    }


    public int getAnimalRequired() {
        return sp.getInt("animalsRequired", 7);

    }

    public String getAnimalsUnlockedBonus() {
        return sp.getString("animalsUnlockedBonus", "0000");
    }

    public void setAnimalsUnlockedBonus(int index) {
        String newUnlocked = sp.getString("animalsUnlockedBonus", "0000");
        newUnlocked = newUnlocked.substring(0, index) + 1 + newUnlocked.substring(index + 1);
        editor.putString("animalsUnlockedBonus", newUnlocked);
        editor.commit();
    }


    public void setBackgroundCheckEnable(boolean b) {
        editor.putBoolean("Enabled", b);
        editor.commit();
    }

    public boolean getBackgroundCheckEnable() {
        return sp.getBoolean("Enabled", true);
    }

    public void setTimeFrequency(int i) {
        editor.putInt("TimeFrequency", i);
        editor.commit();
    }

    public int getTimeFrequency() {
        return sp.getInt("TimeFrequency", 1000);

    }

    public int getTimeFrequencyIndex() {
        return sp.getInt("TimeFrequencyIndex", 1);

    }


    public void setTimeFrequencyIndex(int i) {
        editor.putInt("TimeFrequencyIndex", i);
        editor.commit();
    }

    public void rewardCoins() {
        editor.putInt("CoinsCurrentBalance", getCoinsNumber()[1]+25);
        editor.commit();
    }


    public int getNextAnimalsLocked() {
        return sp.getInt("NextLocked", new Random().nextInt(89));
    }

    public int getNewNextAnimalLocked() {
        String animals = getAnimalsUnlocked();
        if (animals.equals("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111"))
            return -1;
        Random random = new Random();
        int index = random.nextInt();
        boolean found = false;
        while (!found) {
            if (animals.charAt(index) == '0') {
                editor.putInt("NextLocked", index);
                editor.commit();
                return index;
            }
        }
        return -1;
    }
}
