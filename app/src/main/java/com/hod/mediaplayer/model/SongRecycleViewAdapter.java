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
    private SongRecycleListener m_Callback;

    public SongRecycleViewAdapter(ArrayList<Song> i_Songs)
    {
        m_Songs = i_Songs;
    }

    public interface SongRecycleListener
    {
        public void onSongClicked(int i_Position, View i_View, Song i_Song);
    }

    public void setListener(SongRecycleListener i_Callback)
    {
        m_Callback = i_Callback;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView m_NameTv;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            m_NameTv = itemView.findViewById(R.id.song_cell_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(m_Callback != null)
                    {
                        Song song = m_Songs.get(getAdapterPosition());
                        m_Callback.onSongClicked(getAdapterPosition(), itemView,song);
                    }
                }
            });
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
