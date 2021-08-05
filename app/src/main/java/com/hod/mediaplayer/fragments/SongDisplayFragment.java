package com.hod.mediaplayer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongManager;
import com.hod.mediaplayer.model.SongRecycleViewAdapter;
import com.hod.mediaplayer.services.MusicPlayerService;

import java.util.ArrayList;

public class SongDisplayFragment extends Fragment implements SongRecycleViewAdapter.SongRecycleListener
{
    ArrayList<Song> m_Songs;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song_display, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.fragment_song_display_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        m_Songs = SongManager.getInstance().loadDefaultSongs();
/*
        m_Songs.add(new Song("Hod", "https://www.syntax.org.il/xtra/bob.m4a"));
        m_Songs.add(new Song("Ofir", "https://www.syntax.org.il/xtra/bob1.m4a"));
        m_Songs.add(new Song("Niv","https://www.syntax.org.il/xtra/bob2.mp3"));*/


        SongRecycleViewAdapter songRecycleViewAdapter = new SongRecycleViewAdapter(m_Songs);
        songRecycleViewAdapter.setListener(this);
        recyclerView.setAdapter(songRecycleViewAdapter);


        return rootView;
    }

    @Override
    public void onSongClicked(int i_Position, View i_View, Song i_Song)
    {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        intent.putExtra("songs", m_Songs);
        intent.putExtra("song_index", i_Position);
        intent.putExtra("command", "new_instance");
        Toast.makeText(getActivity(), i_Song.getSong(), Toast.LENGTH_SHORT).show();
        getActivity().startService(intent);

    }
}
