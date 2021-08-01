package com.hod.mediaplayer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.hod.mediaplayer.R;

public class MainScreenFragment extends Fragment {

    public MainScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);

        MaterialButton materialButton = rootView.findViewById(R.id.fragment_main_btn);

        materialButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.songDisplayFragment));
        return rootView;
    }
}