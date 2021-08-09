package com.hod.mediaplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;

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
        View rootView = inflater.inflate(R.layout.fragment_music_control, container, false);
        Button prevBtn = rootView.findViewById(R.id.fragment_music_previous_btn);
        Button nextBtn = rootView.findViewById(R.id.fragment_music_next_btn);
        Button playBtn = rootView.findViewById(R.id.fragment_music_play_btn);
        Button pauseBtn = rootView.findViewById(R.id.fragment_music_pause_btn);
        EditText songNameEt = rootView.findViewById(R.id.fragment_music_control_currently_song_name);
        BroadcastReceiver receiverPlayingSongChanged;

        prevBtn.setOnClickListener(v -> m_Callback.onCommandPressed("previous"));
        nextBtn.setOnClickListener(v -> m_Callback.onCommandPressed("next"));
        playBtn.setOnClickListener(v -> m_Callback.onCommandPressed("play"));
        pauseBtn.setOnClickListener(v -> m_Callback.onCommandPressed("pause"));


        IntentFilter filterSongChanged = new IntentFilter("com.hod.mediaplayer.songchanged");
        receiverPlayingSongChanged = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Song song = (Song)intent.getSerializableExtra("song");
                songNameEt.setText(song.getSongName());
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiverPlayingSongChanged, filterSongChanged);

        return rootView;
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
