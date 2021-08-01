package com.hod.mediaplayer.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.hod.mediaplayer.R;

import java.util.ArrayList;

public class SongRecycleViewAdapter extends RecyclerView.Adapter<SongRecycleViewAdapter.SongViewHolder> {
    private ArrayList<Song> m_Songs;

    public SongRecycleViewAdapter(ArrayList<Song> i_Songs)
    {
        m_Songs = i_Songs;
    }


    public class SongViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView m_NameTv;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            m_NameTv = itemView.findViewById(R.id.song_cell_name);
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_cell, parent, false);
        SongViewHolder songViewHolder = new SongViewHolder(view);

        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongRecycleViewAdapter.SongViewHolder holder, int position) {
        Song song = m_Songs.get(position);
        holder.m_NameTv.setText(song.getSongName());
    }

    @Override
    public int getItemCount() {
        return m_Songs.size();
    }


}
