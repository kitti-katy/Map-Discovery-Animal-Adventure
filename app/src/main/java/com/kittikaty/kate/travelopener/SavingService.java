package com.kittikaty.kate.travelopener;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by kate on 3/30/17.
 */

public class SavingService extends IntentService {
    public static BitmapArrayList bitMapArray;

    public SavingService() {
        super("SavingService");
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        bitMapArray = new BitmapArrayList();
        StartSplitter bms = intent.getExtras().getParcelable("SS");
        bms.addToArray();
        createBMSArray(bitMapArray);

    }


    public void createBMSArray(BitmapArrayList bmss) {

        String fileName;
        fileName = "arrayBMS";

        FileOutputStream f = null;
        try {
            f = openFileOutput(fileName, Context.MODE_PRIVATE);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        try {
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(bmss);
            o.flush();
            f.flush();
            o.close();
            f.close();

        } catch (EOFException ex) {
            ex.printStackTrace();


        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
