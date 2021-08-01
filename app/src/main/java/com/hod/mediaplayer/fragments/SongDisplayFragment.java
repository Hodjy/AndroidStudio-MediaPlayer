package com.hod.mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongRecycleViewAdapter;

import java.util.ArrayList;

public class SongDisplayFragment extends Fragment {

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

        ArrayList<Song> songs = new ArrayList<>();
        
        songs.add(new Song("Hod"));
        songs.add(new Song("Ofir"));
        songs.add(new Song("Niv"));
        songs.add(new Song("Matan"));
        songs.add(new Song("Saar"));
        songs.add(new Song("Ezra"));
        songs.add(new Song("Ido"));
        songs.add(new Song("Nv"));

        SongRecycleViewAdapter songRecycleViewAdapter = new SongRecycleViewAdapter(songs);
        recyclerView.setAdapter(songRecycleViewAdapter);


        return rootView;
    }

}
