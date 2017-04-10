package com.kittikaty.kate.travelopener;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.util.GregorianCalendar;
import java.util.HashMap;


public class MapViewFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Serializable, GoogleMap.OnGroundOverlayClickListener {


    MapView mMapView;
    public static BitMapSplitter shortBMS[];
    LatLng center;
    public static GoogleMap mMap;
    BitmapDescriptor a;
    public static StartSplitter bms;
    public static int upgradeLevel;
    public static ImageView addAnimalsIndicator, addCoinsIndicator;
    Bitmap bm;
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

    public static Animation animationAnimalsAdded, animationCoinsAdded;
    public static BitmapDescriptor coinsBitMapDesrcriptors[];
    public static BitmapDescriptor animalsBitMapDesrcriptors[];
    public static BitmapDescriptor bonusAnimalsBitMapDesrcriptors[];
    public static BitmapDescriptor giftsBitMapDesrcriptors[];
    public static BitmapDescriptor allDescriptors[][];
    public static HashMap<String, GroundOverlay> gOverlays;
    public static HashMap<GroundOverlay, BitMapSplitter> allPic;
    public static Bitmap[] animalsBitmaps;
    public static Bitmap[][] allBitmaps;
    public static Bitmap[] coinsBitmaps;
    public static Bitmap[] bonusAnimalsBitmaps;
    public static TextView numberOfAnimalsIndicator, numberOfCoinsIndicator;
    int i = 0;
    private AdView mAdView;


    public static BitmapArrayList bitMapArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);
        bitMapArray = new BitmapArrayList();
        shortBMS = new BitMapSplitter[4];
        addAnimalsIndicator = (ImageView) rootView.findViewById(R.id.tt);
        addCoinsIndicator = (ImageView) rootView.findViewById(R.id.money_pic);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();
        numberOfAnimalsIndicator = (TextView) rootView.findViewById(R.id.animals_indicator);
        numberOfCoinsIndicator = (TextView) rootView.findViewById(R.id.money_indicator);
        numberOfCoinsIndicator.setText(MainHolder.filesAccessor.getCoinsNumber()[1] + "");
        numberOfAnimalsIndicator.setText(MainHolder.filesAccessor.getAllAnimal() + "");

        // Button moves camera to the user location
        ((ImageView) rootView.findViewById(R.id.homeButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
            }
        });
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(this);


        animationAnimalsAdded = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide);
        animationCoinsAdded = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_left);

        mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        bitMapArray = getBMSArray();
        MainHolder.filesAccessor.setBackground(false);
        upgradeLevel = MainHolder.filesAccessor.getCircleLevel();
        mMapView.onResume();
    }

    public void sendSavingIntent() {
        Intent i = new Intent(MainHolder.contextApp, SavingService.class);
        i.putExtra("SS", (Parcelable) bms);
        MainHolder.contextApp.startService(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        synchronized (this) {

            // Save the map ground overlays object
            sendSavingIntent();

            // Change state to background
            MainHolder.filesAccessor.setBackground(true);

            // Set alarm for location scanning if it is enabled
            if (MainHolder.filesAccessor.getBackgroundCheckEnable()) {
                scheduleAlarm();
            }

            // Sleep for 3 seconds - otherwise not there is not enough time to send the object in the saving intent
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mMapView.onPause();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MainHolder.filesAccessor.getTimeFrequency());
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(MainHolder.contextApp,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        float lattitude, longitude;

        if (location != null) {

            lattitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
            LatLng newCenter = new LatLng(lattitude, longitude);

            if (center != null && newCenter.latitude < center.latitude + SQUARE_SIZE2 &&
                    newCenter.latitude > center.latitude - SQUARE_SIZE2 &&
                    newCenter.longitude < center.longitude + SQUARE_SIZE4 &&
                    newCenter.longitude > center.longitude - SQUARE_SIZE4) {

                // do nothing as the user stayed in the almost the same location as before

            } else {
                // if center is null then the user just opened the app
                if (center == null) {

                    center = newCenter;
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(center));

                    // if the user opens the app first time
                    if (bitMapArray.isEmpty()) {

                        bms = new StartSplitter();

                        // Splitting the map into 16 black squares by openning 4 center locations
                        bms.checkLocations(-45, -90);
                        bms.checkLocations(45, -90);
                        bms.checkLocations(45, 90);
                        bms.checkLocations(-45, 90);

                        // reset calculated area opened to zero
                        MainHolder.filesAccessor.setZeroArea();

                    } else {

                        bms = new StartSplitter();

                        //if user already used the app then recreate the bms tree object from the saved array
                        bms.setBMS();

                        // Draw all of the animals, coins and black tiles on the map
                        bms.regenerateLocations();
                    }
                }

                // open new area around the coordinate point
                unlockArea(lattitude, longitude);

                center = newCenter;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setOnGroundOverlayClickListener(this);

        allPic = new HashMap<>();
        gOverlays = new HashMap<>();

        buildGoogleApiClient();

        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this.getContext(), R.raw.mapstyle1));

        setAllPictures();


    }

    private void setAllPictures() {
        createBlackSquarePic();
        generateAnimalsPics();
        generateCoinsPics();
        generateGiftsBitMapDescriptors();
        generateBonusAnimals();
        combineAllTogether();
    }

    private void combineAllTogether() {
        combineDescriptorsArrays();
        combineBitmapsArrays();
    }

    private void generateBonusAnimals() {
        generateBonusAnimalsBitmaps();
        generateBonusAnimalsBitMapDescriptors();
    }

    private void generateCoinsPics() {
        generateCoinsBitmaps();
        generateCoinsBitmapDescriptorAndBlackSquare();
    }

    private void generateAnimalsPics() {
        generateAnimalsPicsBitmaps();
        generateAnimalsPicsBitmapDescriptors();
    }

    private void combineBitmapsArrays() {
        allBitmaps = new Bitmap[][]{coinsBitmaps, animalsBitmaps, null, bonusAnimalsBitmaps};
    }

    private void combineDescriptorsArrays() {
        allDescriptors = new BitmapDescriptor[][]{coinsBitMapDesrcriptors, animalsBitMapDesrcriptors, giftsBitMapDesrcriptors, bonusAnimalsBitMapDesrcriptors};
    }

    private void generateBonusAnimalsBitMapDescriptors() {
        bonusAnimalsBitMapDesrcriptors = new BitmapDescriptor[]{

                BitmapDescriptorFactory.fromBitmap(bonusAnimalsBitmaps[0]),
                BitmapDescriptorFactory.fromBitmap(bonusAnimalsBitmaps[1]),
                BitmapDescriptorFactory.fromBitmap(bonusAnimalsBitmaps[2]),
                BitmapDescriptorFactory.fromBitmap(bonusAnimalsBitmaps[3])};
    }

    private void generateBonusAnimalsBitmaps() {
        bonusAnimalsBitmaps = new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.mipmap.owlfamily),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.stuffedanimal)), (BitmapFactory.decodeResource(getResources(), R.mipmap.teddybear)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.whale))
        };
    }

    private void generateGiftsBitMapDescriptors() {
        giftsBitMapDesrcriptors = new BitmapDescriptor[]{

                BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.giftboxblue)),

                BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.giftboxwhite)),

                BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.boxheart)),

        };
    }

    private void generateCoinsBitmapDescriptorAndBlackSquare() {
        coinsBitMapDesrcriptors = new BitmapDescriptor[]{

                BitmapDescriptorFactory.fromBitmap(coinsBitmaps[0]),
                BitmapDescriptorFactory.fromBitmap(coinsBitmaps[1]),


                BitmapDescriptorFactory.fromBitmap(coinsBitmaps[2]),
                BitmapDescriptorFactory.fromBitmap(coinsBitmaps[3]), a};
    }

    private void generateCoinsBitmaps() {
        coinsBitmaps = new Bitmap[]{(BitmapFactory.decodeResource(getResources(), R.mipmap.coint_cent)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.coint_5_cents)),

                (BitmapFactory.decodeResource(getResources(), R.mipmap.moneybag)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.banknotes))};
    }

    private void generateAnimalsPicsBitmapDescriptors() {
        animalsBitMapDesrcriptors = new BitmapDescriptor[89];
        for (int j = 0; j <89 ; j++) {
            animalsBitMapDesrcriptors[j] =  BitmapDescriptorFactory.fromBitmap(animalsBitmaps[j]);
        }
    }

    private void createBlackSquarePic() {
        bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        bm.eraseColor(Color.argb(245, 9, 24, 19));
        a = BitmapDescriptorFactory.fromBitmap(bm);
    }

    private void generateAnimalsPicsBitmaps() {
        animalsBitmaps = new Bitmap[]{
                (BitmapFactory.decodeResource(getResources(), R.mipmap.sheep)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.cow)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.ymilkcow)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.pig)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zpig)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zgoat)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zgoat2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.horse)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdonkey)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.canard)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.pten)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zchiken1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zchicken2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zrooster)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zduck)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zgoose)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zmalllard)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zswan)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zostrich)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zpeacock)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zturkey)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zturkey2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.browncat)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.cat)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.leopard)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zcat2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zcat3)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zcat4)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zjaguar)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zcat5)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.tiger)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zlion)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.dog)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdog1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdog2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdog4)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zrat1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zrat2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zrat3)),
                BitmapFactory.decodeResource(getResources(), R.mipmap.asia),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.bull)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.ykt)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.chamois)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.deer)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.elk)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zebra)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zlamb)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zlama)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.bear)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.whitebear)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zpanda)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zpolarbear)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zwolf1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zwolf2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.snail)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zsnailtwo)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.enot)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zhedgehog)),
                BitmapFactory.decodeResource(getResources(), R.mipmap.zbunny),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.squirrel)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.greysquirrel)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.ybober)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.monkey)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.kangaroo)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zgiraffe1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zgiraffe2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.elephant)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zelephant1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zelephant2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zjug)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.hippo)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zhippo)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zrhino)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.tusk)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zseal)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zfrog)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.ycrocodile)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.turtle)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.yturtle)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zsnake1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zsnake2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zspider)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdeno6)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino1)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino5)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino2)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino3)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino4)),
                (BitmapFactory.decodeResource(getResources(), R.mipmap.zdino7))
        };
    }


    public void checkShortPath(float lattitude, float longitude) {
        for (int i = 0; i < 4; i++) {
            if (shortBMS[i] != null && 0 == (shortBMS[i].done) && shortBMS[i].checkIfCoordinateIsInside(lattitude, longitude)) {
                shortBMS[i].resetLocation(lattitude, longitude);
                return;
            }
        }
        bms.resetLocation(lattitude, longitude);
    }

    public void unlockArea(float x, float y) {

        //   Log.d("UPGRADE LEVEL", upgradeLevel + "");
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
//        sendSavingIntent();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public BitmapArrayList getBMSArray() {
        synchronized (MapViewFragment.class) {
            if (fileExistance("arrayBMS")) {
                Context c = MainHolder.contextApp;
                ObjectInputStream input;
                String filename = "arrayBMS";
                try {
                    FileInputStream fls = new FileInputStream(new File(new File(c.getFilesDir(), "") + File.separator + filename));
                    input = new ObjectInputStream(fls);
                    BitmapArrayList sbms = (BitmapArrayList) input.readObject();
                    fls.close();
                    input.close();

                    return sbms;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException a) {
                    a.printStackTrace();
                    return getBMSArray();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
        }
        return new BitmapArrayList();
    }

    public boolean fileExistance(String fname) {
        File file = getActivity().getBaseContext().getFileStreamPath(fname);
        return file.exists();
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


    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {


        if (allPic.containsKey(groundOverlay)) {
            int option[] = allPic.get(groundOverlay).removePic();
            if (option[0] != 2) {
                if (option[0] == 0) {
                    addCoinsIndicator.setImageBitmap(allBitmaps[option[0]][option[1]]);

                    addCoinsIndicator.startAnimation(animationCoinsAdded);
                } else if (option[0] == 1) {
                    addAnimalsIndicator.setImageBitmap(allBitmaps[option[0]][option[1]]);
                    addAnimalsIndicator.startAnimation(animationAnimalsAdded);
                }
            }
            groundOverlay.remove();
            allPic.remove(groundOverlay);

            AchievementsFragment.resetAchievementsUI();
            //     createBMS(bms);
            numberOfCoinsIndicator.setText(MainHolder.filesAccessor.getCoinsNumber()[1] + "");
            numberOfAnimalsIndicator.setText(MainHolder.filesAccessor.getAllAnimal() + "");
            BuyCircle.renewUI();
            //   sendSavingIntent();
        }
    }

    public void scheduleAlarm() {

        MainHolder.filesAccessor.setBackground(true);

        Long time = new GregorianCalendar().getTimeInMillis() + new FilesAccessor(MainHolder.contextApp).getTimeFrequency();

        Intent intentAlarm = new Intent(getContext(), AlarmReceiver.class);

        intentAlarm.setAction("com.travelopener.CUSTOM_INTENT");

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(getContext(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    public static void setUpgradeLevel(int i) {
        numberOfCoinsIndicator.setText(MainHolder.filesAccessor.getCoinsNumber()[1] + "");
        upgradeLevel = i;

    }

}
