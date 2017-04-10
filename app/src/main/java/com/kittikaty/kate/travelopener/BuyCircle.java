package com.kittikaty.kate.travelopener;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuyCircle.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyCircle#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyCircle extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static Button upgradeButton;
    static int upgradeLevel;
    private OnFragmentInteractionListener mListener;
    private static FrameLayout levelCanvas;
    private static final String AD_UNIT_ID = "ca-app-pub-8349081440384386/6420634356";
    private static final String APP_ID = "ca-app-pub-8349081440384386~3366328350";
    private Button mShowVideoButton;

    public BuyCircle() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyCircle.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyCircle newInstance(String param1, String param2) {
        BuyCircle fragment = new BuyCircle();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private RewardedVideoAd mAd;
    public static void renewUI() {

        moneyAmount.setText("Coins: " + MainHolder.filesAccessor.getCoinsNumber()[1]);

        switch (upgradeLevel) {
            case 1:
                nextLevelNumber = LEVEL_2;
                costIndicator.setText("Cost: " + LEVEL_2);
                levelCanvas.setBackgroundResource(R.drawable.level1);
                break;
            case 2:
                nextLevelNumber = LEVEL_3;
                costIndicator.setText("Cost: " + LEVEL_3);
                levelCanvas.setBackgroundResource(R.drawable.level2);
                break;
            case 3:
                nextLevelNumber = LEVEL_4;
                costIndicator.setText("Cost: " + LEVEL_4);
                levelCanvas.setBackgroundResource(R.drawable.level3);
                break;
            case 4:
                nextLevelNumber = LEVEL_5;
                costIndicator.setText("Cost: " + LEVEL_5);
                levelCanvas.setBackgroundResource(R.drawable.level4);
                break;
            case 5:
                nextLevelNumber = LEVEL_6;
                costIndicator.setText("Cost: " + LEVEL_6);
                levelCanvas.setBackgroundResource(R.drawable.level5);
                break;
            case 6:
                nextLevelNumber = LEVEL_7;
                costIndicator.setText("Cost: " + LEVEL_7);
                levelCanvas.setBackgroundResource(R.drawable.level6);
                break;
            case 7:
                nextLevelNumber = LEVEL_8;
                costIndicator.setText("Cost: " + LEVEL_8);
                levelCanvas.setBackgroundResource(R.drawable.level7);
                break;
            case 8:
                nextLevelNumber = LEVEL_9;
                costIndicator.setText("Cost: " + LEVEL_9);
                levelCanvas.setBackgroundResource(R.drawable.level8);
                break;
            case 9:
                nextLevelNumber = LEVEL_10;
                costIndicator.setText("Cost: " + LEVEL_10);
                levelCanvas.setBackgroundResource(R.drawable.level9);
                break;
            case 10:
                nextLevelNumber = LEVEL_11;
                costIndicator.setText("Cost: " + LEVEL_11);
                levelCanvas.setBackgroundResource(R.drawable.level10);
                break;
            case 11:
                nextLevelNumber = LEVEL_12;
                costIndicator.setText("Cost: " + LEVEL_12);
                levelCanvas.setBackgroundResource(R.drawable.level11);
                break;
            case 12:
                nextLevelNumber = LEVEL_13;
                costIndicator.setText("Cost: " + LEVEL_13);
                levelCanvas.setBackgroundResource(R.drawable.level12);
                break;
            case 13:
                nextLevelNumber = LEVEL_14;
                costIndicator.setText("Cost: " + LEVEL_14);
                levelCanvas.setBackgroundResource(R.drawable.level13);
                break;
            case 14:
                nextLevelNumber = LEVEL_15;
                costIndicator.setText("Cost: " + LEVEL_15);
                levelCanvas.setBackgroundResource(R.drawable.level14);
                break;
            case 15:
                nextLevelNumber = LEVEL_16;
                costIndicator.setText("Cost: " + LEVEL_16);
                levelCanvas.setBackgroundResource(R.drawable.level15);
                break;
            case 16:
                //nextLevelNumber = LEVEL_17;
                levelCanvas.setBackgroundResource(R.drawable.level16);
                break;
            case 17:
              //  nextLevelNumber = LEVEL_3;
                levelCanvas.setBackgroundResource(R.drawable.level16);
                break;

        }
    }
private RewardedVideoAdListener rval;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_buy_circle, container, false);
        MobileAds.initialize(MainHolder.contextApp, APP_ID);
        levelCanvas = (FrameLayout) root.findViewById(R.id.levelCanvas);
        costIndicator = (TextView) root.findViewById(R.id.costIndicator);
        upgradeButton = (Button) root.findViewById(R.id.upgradeButton);
        upgradeLevel = MainHolder.filesAccessor.getCircleLevel();
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            upgrade();
            }
        });
        moneyAmount = (TextView) root.findViewById(R.id.moneyIndicator);
        rval = new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
mShowVideoButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                MainHolder.filesAccessor.rewardCoins();

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast toast = Toast.makeText(MainHolder.contextApp, "25 coins are earned",Toast.LENGTH_SHORT);
                toast.show();
                mShowVideoButton.setVisibility(View.INVISIBLE);
            renewUI();}

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }
        };
        mAd =  MobileAds.getRewardedVideoAdInstance(MainHolder.contextApp);
        mAd.setRewardedVideoAdListener(rval);
        loadRewardedVideoAd();
        mShowVideoButton = ((Button) root.findViewById(R.id.watch_video));
        mShowVideoButton.setVisibility(View.INVISIBLE);
        mShowVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedVideo();
            }
        });

     renewUI();

        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    static TextView moneyAmount;
    static TextView costIndicator;
    static int nextLevelNumber;
    static final int LEVEL_2 = 50, LEVEL_3 = 120, LEVEL_4 = 250, LEVEL_5 = 500, LEVEL_6 = 800,
            LEVEL_7 = 1200, LEVEL_8 = 1500, LEVEL_9 = 1700, LEVEL_10 = 2000, LEVEL_11 = 2200, LEVEL_12 = 2400,
            LEVEL_13 = 2500, LEVEL_14 = 2600, LEVEL_15 = 2700, LEVEL_16 = 3000;


    public void upgrade(){
        if(MainHolder.filesAccessor.getCoinsNumber()[1]-nextLevelNumber>=0)
        {
            upgradeLevel ++ ;
            MainHolder.filesAccessor.upgradeCircleLevel();
            MainHolder.filesAccessor.decreaseCurrentBalance(nextLevelNumber);
            MapViewFragment.setUpgradeLevel(upgradeLevel);
            renewUI();

        }
    }

    private void loadRewardedVideoAd() {
        if (!mAd.isLoaded()) {
            mAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }
    private void showRewardedVideo() {
        mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mAd.isLoaded()) {
            mAd.show();
        }
    }
}
