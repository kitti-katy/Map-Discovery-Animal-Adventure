package com.kittikaty.kate.travelopener;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.GregorianCalendar;

/**
 * Created by kate on 3/20/17.
 */

public class AlarmReceiver extends BroadcastReceiver
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    final float SQUARE_SIZE = 0.00034332275f;
    final float SQUARE_SIZE2 = 0.0006866455f;
    final float SQUARE_SIZE3 = 0.00102996826f;
    final float SQUARE_SIZE4 = 0.00137329101f;
    final float SQUARE_SIZE6 = 0.00205993652f;
    final float SQUARE_SIZE5 = 0.00171661376f;
    final float SQUARE_SIZE8 = 0.00274658203f;
    final float SQUARE_SIZE10 = 0.00343322753f;
    final float SQUARE_SIZE12 = 0.00411987304f;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Context c;
    StartSplitter bms;
    public static BitMapSplitter shortBMS[];

    public static FilesAccessor filesAccessor;
    public static BitmapArrayList bitMapArray;

    @Override
    public void onReceive(Context context, Intent intent) {
        bitMapArray = new BitmapArrayList();

        filesAccessor = new FilesAccessor(context);

        if (filesAccessor.getBackground()) {
            c = context;

            shortBMS = new BitMapSplitter[4];

            if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(c, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            buildGoogleApiClient();


        } else filesAccessor.setInsideBackgroundStop(true);


    }


    public void createBMS(StartSplitter sbms) {
        setIntentSave(sbms);
    }


    public BitmapArrayList setBMS(Context context) {
        if (fileExistance("arrayBMS", context)) {

            ObjectInputStream input;
            String filename = "arrayBMS";
            try {
                // Log.d("FILE SIZE:", (((double) new File(new File(context.getFilesDir(), "") + File.separator + filename).length()) / 1024) / 1024 + " mb");
                FileInputStream fls = new FileInputStream(new File(new File(context.getFilesDir(), "") + File.separator + filename));

                input = new ObjectInputStream(fls);
                BitmapArrayList bms = (BitmapArrayList) input.readObject();
                fls.close();
                input.close();

                return bms;
            } catch (FileNotFoundException e) {


                // Log.d("FILE", "NOT FOUND");

            } catch (IOException a) {
                a.printStackTrace();
                Long time = new GregorianCalendar().getTimeInMillis() + filesAccessor.getTimeFrequency();

                Intent intentAlarm = new Intent(c, AlarmReceiver.class);

                AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(c, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                // Log.d("ALARM", "RESCHEDULED");


            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }

    public boolean fileExistance(String fname, Context context) {
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }


    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval((filesAccessor.getTimeFrequency()));
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this.c,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        } else {


        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        // Log.d("BUILDING GOOGLE API", "DONE");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            bitMapArray = setBMS(c);
        } catch (Exception ex) {
            Long time = new GregorianCalendar().getTimeInMillis() + filesAccessor.getTimeFrequency();

            Intent intentAlarm = new Intent(c, AlarmReceiver.class);

            AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(c, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            // Log.d("Alarm","RESET");

            return;

        }

        float lat = (float) (location.getLatitude());
        float lng = (float) (location.getLongitude());

        if (bitMapArray != null) {
            synchronized (this) {
                bms = new StartSplitter();
                bms.setBMSBackground();

                bms.resetLocationBackground(lat, lng);
                checkSurroundingPoints(lat, lng);

                if (filesAccessor.getBackground())
                    createBMS(bms);
            }
        }
    }


    public void checkUpgrade1(float x, float y) {

        checkShortPath(x, y);
        checkShortPath(x + SQUARE_SIZE, y);
        checkShortPath(x - SQUARE_SIZE, y);
        checkShortPath(x, y + SQUARE_SIZE2);
        checkShortPath(x, y - SQUARE_SIZE2);

    }

    public void checkUpgrade2(float x, float y) {
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE2);
    }

    public void checkUpgrade3(float x, float y) {
        checkShortPath(x, y + SQUARE_SIZE4);
        checkShortPath(x, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE2, y);
        checkShortPath(x - SQUARE_SIZE2, y);
    }

    public void checkUpgrade4(float x, float y) {
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE2, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE2, y - SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE2, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE2, y - SQUARE_SIZE2);
    }

    public void checkUpgrade5(float x, float y) {
        checkShortPath(x, y + SQUARE_SIZE6);
        checkShortPath(x, y - SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE3, y);
        checkShortPath(x - SQUARE_SIZE3, y);
    }

    public void checkUpgrade6(float x, float y) {
        checkShortPath(x - SQUARE_SIZE2, y + SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE2, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE2, y + SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE2, y - SQUARE_SIZE4);

    }

    public void checkUpgrade7(float x, float y) {
        checkShortPath(x - SQUARE_SIZE3, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE3, y - SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE3, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE3, y - SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE6);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE6);
    }

    public void checkUpgrade8(float x, float y) {
        checkShortPath(x, y + SQUARE_SIZE8);
        checkShortPath(x, y - SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE4, y);
        checkShortPath(x - SQUARE_SIZE4, y);

    }

    public void checkUpgrade9(float x, float y) {
        checkShortPath(x - SQUARE_SIZE3, y + SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE3, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE3, y + SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE3, y - SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE2, y + SQUARE_SIZE6);
        checkShortPath(x - SQUARE_SIZE2, y - SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE2, y + SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE2, y - SQUARE_SIZE6);
    }

    public void checkUpgrade10(float x, float y) {

        checkShortPath(x - SQUARE_SIZE4, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE4, y - SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE4, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE4, y - SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE8);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE8);
    }

    public void checkUpgrade11(float x, float y) {
        checkShortPath(x, y + SQUARE_SIZE10);
        checkShortPath(x, y - SQUARE_SIZE10);
        checkShortPath(x + SQUARE_SIZE5, y);
        checkShortPath(x - SQUARE_SIZE5, y);
    }

    public void checkUpgrade12(float x, float y) {
        checkShortPath(x - SQUARE_SIZE3, y + SQUARE_SIZE6);
        checkShortPath(x - SQUARE_SIZE3, y - SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE3, y + SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE3, y - SQUARE_SIZE6);
        checkShortPath(x - SQUARE_SIZE4, y + SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE4, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE4, y + SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE4, y - SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE2, y + SQUARE_SIZE8);
        checkShortPath(x - SQUARE_SIZE2, y - SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE2, y + SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE2, y - SQUARE_SIZE8);
    }

    public void checkUpgrade13(float x, float y) {

        checkShortPath(x - SQUARE_SIZE5, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE5, y - SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE5, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE5, y - SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE10);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE10);
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE10);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE10);
    }

    public void checkUpgrade14(float x, float y) {
        checkShortPath(x, y + SQUARE_SIZE12);
        checkShortPath(x, y - SQUARE_SIZE12);
        checkShortPath(x + SQUARE_SIZE6, y);
        checkShortPath(x - SQUARE_SIZE6, y);
    }

    public void checkUpgrade15(float x, float y) {
        checkShortPath(x - SQUARE_SIZE3, y + SQUARE_SIZE8);
        checkShortPath(x - SQUARE_SIZE3, y - SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE3, y + SQUARE_SIZE8);
        checkShortPath(x + SQUARE_SIZE3, y - SQUARE_SIZE8);
        checkShortPath(x - SQUARE_SIZE4, y + SQUARE_SIZE6);
        checkShortPath(x - SQUARE_SIZE4, y - SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE4, y + SQUARE_SIZE6);
        checkShortPath(x + SQUARE_SIZE4, y - SQUARE_SIZE6);

    }

    public void checkUpgrade16(float x, float y) {
        checkShortPath(x - SQUARE_SIZE2, y + SQUARE_SIZE10);
        checkShortPath(x - SQUARE_SIZE2, y - SQUARE_SIZE10);
        checkShortPath(x + SQUARE_SIZE2, y + SQUARE_SIZE10);
        checkShortPath(x + SQUARE_SIZE2, y - SQUARE_SIZE10);
        checkShortPath(x - SQUARE_SIZE5, y + SQUARE_SIZE4);
        checkShortPath(x - SQUARE_SIZE5, y - SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE5, y + SQUARE_SIZE4);
        checkShortPath(x + SQUARE_SIZE5, y - SQUARE_SIZE4);

    }

    public void checkUpgrade17(float x, float y) {
        checkShortPath(x + SQUARE_SIZE, y + SQUARE_SIZE12);
        checkShortPath(x + SQUARE_SIZE, y - SQUARE_SIZE12);
        checkShortPath(x - SQUARE_SIZE, y + SQUARE_SIZE12);
        checkShortPath(x - SQUARE_SIZE, y - SQUARE_SIZE12);
        checkShortPath(x + SQUARE_SIZE6, y + SQUARE_SIZE2);
        checkShortPath(x + SQUARE_SIZE6, y - SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE6, y + SQUARE_SIZE2);
        checkShortPath(x - SQUARE_SIZE6, y - SQUARE_SIZE2);
    }

    public void checkShortPath(float lattitude, float longitude) {

        bms.resetLocationBackground(lattitude, longitude);
    }

    public void checkSurroundingPoints(float x, float y) {
        int upgradeLevel = filesAccessor.getCircleLevel();

        if (upgradeLevel >= 1)
            checkUpgrade1(x, y);
        if (upgradeLevel >= 2)
            checkUpgrade2(x, y);
        if (upgradeLevel >= 3)
            checkUpgrade3(x, y);
        if (upgradeLevel >= 4)
            checkUpgrade4(x, y);
        if (upgradeLevel >= 5)
            checkUpgrade5(x, y);
        if (upgradeLevel >= 6)
            checkUpgrade6(x, y);
        if (upgradeLevel >= 7)
            checkUpgrade7(x, y);
        if (upgradeLevel >= 8)
            checkUpgrade8(x, y);
        if (upgradeLevel >= 9)
            checkUpgrade9(x, y);
        if (upgradeLevel >= 10)
            checkUpgrade10(x, y);
        if (upgradeLevel >= 11)
            checkUpgrade11(x, y);
        if (upgradeLevel >= 12)
            checkUpgrade12(x, y);
        if (upgradeLevel >= 13)
            checkUpgrade13(x, y);
        if (upgradeLevel >= 14)
            checkUpgrade14(x, y);
        if (upgradeLevel >= 15)
            checkUpgrade15(x, y);
        if (upgradeLevel >= 16)
            checkUpgrade16(x, y);
        if (upgradeLevel >= 17)
            checkUpgrade17(x, y);

    }

    public void setIntentSave(StartSplitter sbms) {
        Intent i = new Intent(c, SavingService.class);
        i.putExtra("SS", (Parcelable) sbms);
        c.startService(i);
    }
}
