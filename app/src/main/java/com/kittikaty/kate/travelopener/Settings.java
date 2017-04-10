package com.kittikaty.kate.travelopener;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean clicked;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinner;
    final String HELP = "This app opens your location on the map. \n\nYou can collect" +
            " coins by tapping and then upgrade the area opened." + "\n\nYou may want to reduce the "
            + "frequency of location checked to save you battery level.";
    final String DISCLAIMER = "Your data is not collected by us. It is only stored on your device." + "\n\n\nAll of the pictures were taken from pixabay.com from the following authors: " +
            "\n\nhttps://pixabay.com/en/users/OpenClipart-Vectors-30363/" +
            "\n\nhttps://pixabay.com/en/users/officialkaywille-444458/" +
            "\n\nhttps://pixabay.com/en/users/Clker-Free-Vector-Images-3736/";
    private OnFragmentInteractionListener mListener;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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

    ToggleButton tb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainHolder.contextApp,
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(MainHolder.filesAccessor.getTimeFrequencyIndex());
        clicked = true;


        tb = (ToggleButton) rootView.findViewById(R.id.toggleButton);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tb.isChecked())
                    MainHolder.filesAccessor.setBackgroundCheckEnable(true);
                else MainHolder.filesAccessor.setBackgroundCheckEnable(false);

            }
        });
        tb.setChecked(MainHolder.filesAccessor.getBackgroundCheckEnable());
        // Inflate the layout for this fragment

        ((TextView) rootView.findViewById(R.id.help)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(0);
            }
        });

        ((TextView) rootView.findViewById(R.id.disclaimer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(1);
            }
        });
        return rootView;
    }

    public void buildDialog(int option) {
        String message, title;
        if (option == 0) {
            message = HELP;
            title = "Help";
        } else {
            message = DISCLAIMER;
            title = "Disclaimer";
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("OK", null);
        AlertDialog alert = alertDialog.create();
        alert.show();
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

    @Override
    public void onResume() {
        super.onResume();
        clicked = false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        MainHolder.filesAccessor.setTimeFrequencyIndex(i);
        switch (i) {
            case 0:
                MainHolder.filesAccessor.setTimeFrequency(5000);
                break;
            case 1:
                MainHolder.filesAccessor.setTimeFrequency(10000);
                break;
            case 2:
                MainHolder.filesAccessor.setTimeFrequency(15000);
                break;
            case 3:
                MainHolder.filesAccessor.setTimeFrequency(30000);
                break;
            case 4:
                MainHolder.filesAccessor.setTimeFrequency(60000);
                break;
            case 5:
                MainHolder.filesAccessor.setTimeFrequency(120000);
                break;
            case 6:
                MainHolder.filesAccessor.setTimeFrequency(300000);
                break;
            case 7:
                MainHolder.filesAccessor.setTimeFrequency(900000);
                break;
            case 8:
                MainHolder.filesAccessor.setTimeFrequency(1800000);
                break;

        }
        if (clicked) {
            Toast toast = Toast.makeText(MainHolder.contextApp, "Restart the app to apply the changes", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        spinner.setSelection(MainHolder.filesAccessor.getTimeFrequencyIndex());
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
