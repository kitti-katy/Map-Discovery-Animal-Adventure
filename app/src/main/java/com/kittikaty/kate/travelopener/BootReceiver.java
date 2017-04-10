package com.kittikaty.kate.travelopener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.GregorianCalendar;

/**
 * Created by kate on 4/1/17.
 */

public class BootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") && new FilesAccessor(context).getBackgroundCheckEnable()) {
            scheduleAlarm(context);
        }

    }


    public void scheduleAlarm(Context context) {

        Long time = new GregorianCalendar().getTimeInMillis() + new FilesAccessor(context).getTimeFrequency();

        Intent intentAlarm = new Intent(context, AlarmReceiver.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

    }
}