package com.kittikaty.kate.travelopener;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class AchievementsFragment extends Fragment {

    public static TextView progressIndicators[];
    public static ImageView animalsPic[], bonusAnimalsPic[];
    public static Context context;
    static View rootView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AchievementsFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AchievementsFragment newInstance(String param1, String param2) {
        AchievementsFragment fragment = new AchievementsFragment();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_achievements, container, false);
        resetAchievementsUI();
        return rootView;

    }

    public static void resetAchievementsUI() {
        resetLevelIndicator();
        resetProgressBarsIndicators();
        setAnimalsIconsUnlocked();

    }

    private static void resetProgressBarsIndicators() {
        progressIndicators = new TextView[]{
                (TextView) rootView.findViewById(R.id.coinsIndicator),
                (TextView) rootView.findViewById(R.id.areaIndicator),
                (TextView) rootView.findViewById(R.id.coins5Indicator)};

        ProgressBar progressBarsAll = (ProgressBar) rootView.findViewById(R.id.pb_coins),
                pb_animals = (ProgressBar) rootView.findViewById(R.id.pb_animals),
                pb_area = (ProgressBar) rootView.findViewById(R.id.pb_area);

        int totalCoinsCollected = MainHolder.filesAccessor.getCoinsNumber()[0];
        int coinsRequiredToCollect = MainHolder.filesAccessor.getCoinsNumberRequired();
        progressIndicators[0].setText(totalCoinsCollected + "/" + coinsRequiredToCollect);

        double opened = MainHolder.filesAccessor.getAreaOpened();
        double requiredToOPen = MainHolder.filesAccessor.getAreaOpenedRequired();
        progressIndicators[1].setText(String.format("%.2f", (opened / 1000)) + "/" + String.format("%.2f", (requiredToOPen / 1000)));


        progressBarsAll.setMax(coinsRequiredToCollect);
        progressBarsAll.setProgress(totalCoinsCollected);

        pb_area.setMax((int) requiredToOPen);
        pb_area.setProgress((int) opened);

        int animalsAllCollected = MainHolder.filesAccessor.getAllAnimal();
        int animalsRequired = MainHolder.filesAccessor.getAnimalRequired();
        pb_animals.setMax((int) animalsRequired);
        pb_animals.setProgress((int) animalsAllCollected);
        progressIndicators[2].setText(animalsAllCollected + "/" + animalsRequired);
    }

    private static void setAnimalsIconsUnlocked() {
        animalsPic = new ImageView[]{
                (ImageView) rootView.findViewById(R.id.a0),
                (ImageView) rootView.findViewById(R.id.a1),
                (ImageView) rootView.findViewById(R.id.a2),
                (ImageView) rootView.findViewById(R.id.a3),
                (ImageView) rootView.findViewById(R.id.a4),
                (ImageView) rootView.findViewById(R.id.a5),
                (ImageView) rootView.findViewById(R.id.a6),
                (ImageView) rootView.findViewById(R.id.a7),
                (ImageView) rootView.findViewById(R.id.a8),
                (ImageView) rootView.findViewById(R.id.a9),
                (ImageView) rootView.findViewById(R.id.a10),
                (ImageView) rootView.findViewById(R.id.a11),
                (ImageView) rootView.findViewById(R.id.a12),
                (ImageView) rootView.findViewById(R.id.a13),
                (ImageView) rootView.findViewById(R.id.a14),
                (ImageView) rootView.findViewById(R.id.a15),
                (ImageView) rootView.findViewById(R.id.a16),
                (ImageView) rootView.findViewById(R.id.a17),
                (ImageView) rootView.findViewById(R.id.a18),
                (ImageView) rootView.findViewById(R.id.a19),
                (ImageView) rootView.findViewById(R.id.a20),
                (ImageView) rootView.findViewById(R.id.a21),
                (ImageView) rootView.findViewById(R.id.a22),
                (ImageView) rootView.findViewById(R.id.a23),
                (ImageView) rootView.findViewById(R.id.a24),
                (ImageView) rootView.findViewById(R.id.a25),
                (ImageView) rootView.findViewById(R.id.a26),
                (ImageView) rootView.findViewById(R.id.a27),
                (ImageView) rootView.findViewById(R.id.a28),
                (ImageView) rootView.findViewById(R.id.a29),
                (ImageView) rootView.findViewById(R.id.a30),
                (ImageView) rootView.findViewById(R.id.a31),
                (ImageView) rootView.findViewById(R.id.a32),
                (ImageView) rootView.findViewById(R.id.a33),
                (ImageView) rootView.findViewById(R.id.a34),
                (ImageView) rootView.findViewById(R.id.a35),
                (ImageView) rootView.findViewById(R.id.a36),
                (ImageView) rootView.findViewById(R.id.a37),
                (ImageView) rootView.findViewById(R.id.a38),
                (ImageView) rootView.findViewById(R.id.a39),
                (ImageView) rootView.findViewById(R.id.a40),
                (ImageView) rootView.findViewById(R.id.a41),
                (ImageView) rootView.findViewById(R.id.a42),
                (ImageView) rootView.findViewById(R.id.a43),
                (ImageView) rootView.findViewById(R.id.a44),
                (ImageView) rootView.findViewById(R.id.a45),
                (ImageView) rootView.findViewById(R.id.a46),
                (ImageView) rootView.findViewById(R.id.a47),
                (ImageView) rootView.findViewById(R.id.a48),
                (ImageView) rootView.findViewById(R.id.a49),
                (ImageView) rootView.findViewById(R.id.a50),
                (ImageView) rootView.findViewById(R.id.a51),
                (ImageView) rootView.findViewById(R.id.a52),
                (ImageView) rootView.findViewById(R.id.a53),
                (ImageView) rootView.findViewById(R.id.a54),
                (ImageView) rootView.findViewById(R.id.a55),
                (ImageView) rootView.findViewById(R.id.a56),
                (ImageView) rootView.findViewById(R.id.a57),
                (ImageView) rootView.findViewById(R.id.a58),
                (ImageView) rootView.findViewById(R.id.a59),
                (ImageView) rootView.findViewById(R.id.a60),
                (ImageView) rootView.findViewById(R.id.a61),
                (ImageView) rootView.findViewById(R.id.a62),
                (ImageView) rootView.findViewById(R.id.a63),
                (ImageView) rootView.findViewById(R.id.a64),
                (ImageView) rootView.findViewById(R.id.a65),
                (ImageView) rootView.findViewById(R.id.a66),
                (ImageView) rootView.findViewById(R.id.a67),
                (ImageView) rootView.findViewById(R.id.a68),
                (ImageView) rootView.findViewById(R.id.a69),
                (ImageView) rootView.findViewById(R.id.a70),
                (ImageView) rootView.findViewById(R.id.a71),
                (ImageView) rootView.findViewById(R.id.a72),
                (ImageView) rootView.findViewById(R.id.a73),
                (ImageView) rootView.findViewById(R.id.a74),
                (ImageView) rootView.findViewById(R.id.a75),
                (ImageView) rootView.findViewById(R.id.a76),
                (ImageView) rootView.findViewById(R.id.a77),
                (ImageView) rootView.findViewById(R.id.a78),
                (ImageView) rootView.findViewById(R.id.a79),
                (ImageView) rootView.findViewById(R.id.a80),
                (ImageView) rootView.findViewById(R.id.a81),
                (ImageView) rootView.findViewById(R.id.a82),
                (ImageView) rootView.findViewById(R.id.a83),
                (ImageView) rootView.findViewById(R.id.a84),
                (ImageView) rootView.findViewById(R.id.a85),
                (ImageView) rootView.findViewById(R.id.a86),
                (ImageView) rootView.findViewById(R.id.a87),
                (ImageView) rootView.findViewById(R.id.a88)


        };


        bonusAnimalsPic = new ImageView[]{
                (ImageView) rootView.findViewById(R.id.ba0),
                (ImageView) rootView.findViewById(R.id.ba1),
                (ImageView) rootView.findViewById(R.id.ba2),
                (ImageView) rootView.findViewById(R.id.ba3),
        };
        String unlockedAnimals = MainHolder.filesAccessor.getAnimalsUnlocked();
        for (int i = 0; i < 89; i++) {
            if (unlockedAnimals.charAt(i) == '1')
                animalsPic[i].setImageTintList(null);
        }

        String unlockedAnimalsBonus = MainHolder.filesAccessor.getAnimalsUnlockedBonus();
        for (int i = 0; i < 4; i++) {
            if (unlockedAnimalsBonus.charAt(i) == '1')
                bonusAnimalsPic[i].setImageTintList(null);
        }
    }

    private static void resetLevelIndicator() {
        ((TextView) rootView.findViewById(R.id.Levelndicator)).setText("Next Level " + (MainHolder.filesAccessor.getLevel() + 1) + ":");
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


}


