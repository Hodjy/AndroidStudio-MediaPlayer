package com.hod.mediaplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongManager;
import com.hod.mediaplayer.model.SongRecycleViewAdapter;
import com.hod.mediaplayer.services.MusicPlayerService;

import java.util.ArrayList;

public class SongDisplayFragment extends Fragment implements SongRecycleViewAdapter.SongRecycleListener
{
    ArrayList<Song> m_Songs;
    ISongDisplayFragmentListener m_Callback;

    public interface ISongDisplayFragmentListener
    {
        public void onSongSwipe(int i_SongPosition, RecyclerView i_RecycleView);
    }

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

        m_Songs = SongManager.getInstance().getSongs(getActivity().getApplicationContext());

        SongRecycleViewAdapter songRecycleViewAdapter = new SongRecycleViewAdapter(m_Songs);
        songRecycleViewAdapter.setListener(this);


        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                int startPosition = viewHolder.getAdapterPosition();
                int dragPosition = target.getAdapterPosition();
                if(startPosition != dragPosition)
                {
                    Song pressedSong = m_Songs.get(startPosition);
                    Song dragLocationSong = m_Songs.get(dragPosition);
                    m_Songs.set(startPosition, dragLocationSong);
                    m_Songs.set(dragPosition, pressedSong);
                    songRecycleViewAdapter.notifyItemMoved(startPosition, dragPosition);
                    SongManager.getInstance().setSongsList(m_Songs, getContext());
                }

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                m_Callback.onSongSwipe(viewHolder.getAdapterPosition(), recyclerView);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(songRecycleViewAdapter);

        return rootView;
    }

    @Override
    public void onSongClicked(int i_Position, View i_View, Song i_Song)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", i_Song);
        NavHostFragment.findNavController(this).navigate(R.id.action_songDisplayFragment_to_songDetailsFragment, bundle);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            m_Callback = (ISongDisplayFragmentListener)context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("The Activity must implement: ISongDisplayFragmentListener");
        }
    }
}
