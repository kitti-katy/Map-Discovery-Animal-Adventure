package com.kittikaty.kate.travelopener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.HashMap;



public class MainHolder extends AppCompatActivity implements AchievementsFragment.OnFragmentInteractionListener, BuyCircle.OnFragmentInteractionListener, Settings.OnFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static FilesAccessor filesAccessor;
    public static Context contextApp;
    public static AchievementsFragment af;
    public static HashMap<String, BitMapSplitter> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_holder);

        contextApp = getApplicationContext();
/*
        AssetManager am = contextApp.getApplicationContext().getAssets();

       Typeface typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "abc.ttf"));


changeFont(typeface,"DEFAULT");
        changeFont(typeface, "MONOSPACE");
        changeFont(typeface,"SERIF");
        changeFont(typeface, "SANS_SERIF");

*/

        checkAndAskPermissions();

    }
   static TabLayout tabLayout;
    private void createTabs(){
        filesAccessor = new FilesAccessor(this.getBaseContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("My Map"));
        tabLayout.addTab(tabLayout.newTab().setText("Achievements"));
        tabLayout.addTab(tabLayout.newTab().setText("Upgrade"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        af = (AchievementsFragment)adapter.getItem(1);

    }

/*
    public static void changeFont(Typeface typeface, String s){
        try {
            final  Field staticField = Typeface.class
                    .getDeclaredField(s);
            staticField.setAccessible(true);
            staticField.set(null, typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
*/
    public static void setUiAchievements(){
       af.resetAchievementsUI();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d("MAINHOLDER","OnDESTROY");

this.finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.activity_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void checkAndAskPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
         createTabs();
            return;
        }


        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {


            new AlertDialog.Builder(MainHolder.this)
                    .setMessage("You need to allow access to your Location. Otherwise, you can't use the app!")
                    .setPositiveButton("OK", listener)
                    .setNegativeButton("Cancel", listener)
                    .create()
                    .show();

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else askPermissions();

    }
    protected void askPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createTabs();

                }
                return;
            }
        }
    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        final int BUTTON_NEGATIVE = -2;
        final int BUTTON_POSITIVE = -1;

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_NEGATIVE:
                    // int which = -2


                    dialog.dismiss();
                    break;

                case BUTTON_POSITIVE:
                    // int which = -1


                    dialog.dismiss();
                    askPermissions();
                    break;
            }
        }
    };


    public static void addDistance(double lat1, double lng1, double lat2, double lng2) {
filesAccessor.addDistance(lat1, lng1, lat2, lng2);

    }



    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}
