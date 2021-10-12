package com.hod.mediaplayer.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.hod.mediaplayer.R;

import java.util.ArrayList;

public class SongRecycleViewAdapter extends RecyclerView.Adapter<SongRecycleViewAdapter.SongViewHolder> {
    private ArrayList<Song> m_Songs;
    private SongRecycleListener m_Callback;
    private Context m_Context;

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
        MaterialTextView m_SongNameTv;
        MaterialTextView m_ArtistNameTv;
        ImageView m_SongImage;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);

            m_SongNameTv = itemView.findViewById(R.id.song_cell_song_name);
            m_ArtistNameTv = itemView.findViewById(R.id.song_cell_artist_name);
            m_SongImage = itemView.findViewById(R.id.song_cell_image);

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
        m_Context = parent.getContext();
        SongViewHolder songViewHolder = new SongViewHolder(view);

        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongRecycleViewAdapter.SongViewHolder holder, int position) {
        Song song = m_Songs.get(position);
        holder.m_SongNameTv.setText(song.getSongName());
        holder.m_ArtistNameTv.setText(song.getArtistName());
        Glide.with(m_Context).load(song.getImageUriParsed()).error(R.drawable.ic_baseline_music_note_24).into(holder.m_SongImage);
    }

    @Override
    public int getItemCount() {
        return m_Songs.size();
    }


}
