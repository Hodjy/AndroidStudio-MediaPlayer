package com.hod.mediaplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;

public class SongDetailsFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_song_details, container, false);
        ImageView songImg = rootView.findViewById(R.id.fragment_song_details_image);
        EditText songNameTv = rootView.findViewById(R.id.fragment_song_details_name_et);
        EditText artistNameTv = rootView.findViewById(R.id.fragment_song_details_artist_name_et);
        Button backBtn = rootView.findViewById(R.id.fragment_song_details_back);

        Song song = (Song)(getArguments().getSerializable("song"));

        Glide.with(getContext()).load(song.getImageUriParsed()).error(R.drawable.ic_baseline_music_note_24).into(songImg);
        songNameTv.setText(song.getSongName());
        artistNameTv.setText(song.getArtistName());

        backBtn.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        return rootView;
    }
}
