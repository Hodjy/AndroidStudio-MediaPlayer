package com.hod.mediaplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hod.mediaplayer.R;

public class MusicControlFragment extends Fragment
{
    private IMusicControlFragmentListener m_Callback;

    public interface IMusicControlFragmentListener
    {
        public void onCommandPressed(String i_Command);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootview = inflater.inflate(R.layout.fragment_music_control, container, false);
        Button prevBtn = rootview.findViewById(R.id.fragment_music_previous_btn);
        Button nextBtn = rootview.findViewById(R.id.fragment_music_next_btn);
        Button playBtn = rootview.findViewById(R.id.fragment_music_play_btn);
        Button pauseBtn = rootview.findViewById(R.id.fragment_music_pause_btn);

        prevBtn.setOnClickListener(v -> m_Callback.onCommandPressed("previous"));
        nextBtn.setOnClickListener(v -> m_Callback.onCommandPressed("next"));
        playBtn.setOnClickListener(v -> m_Callback.onCommandPressed("play"));
        pauseBtn.setOnClickListener(v -> m_Callback.onCommandPressed("pause"));

        return rootview;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try
        {
            m_Callback = (IMusicControlFragmentListener)context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("The activity must implement the interface IMusicControlFragmentListener");
        }
    }
}
