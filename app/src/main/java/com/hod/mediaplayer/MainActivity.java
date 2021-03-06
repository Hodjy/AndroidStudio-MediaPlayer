package com.hod.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.fragments.AddSongFragment;
import com.hod.mediaplayer.fragments.DeleteDialogFragment;
import com.hod.mediaplayer.fragments.MusicControlFragment;
import com.hod.mediaplayer.fragments.SongDisplayFragment;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongManager;
import com.hod.mediaplayer.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity
        implements MusicControlFragment.IMusicControlFragmentListener,
        AddSongFragment.IAddSongFragmentListener,
        SongDisplayFragment.ISongDisplayFragmentListener,
        DeleteDialogFragment.IDialogFragmentListener
{
    private RecyclerView m_DisplayFragmentRecycleView;
    private DrawerLayout m_DrawerLayout;
    private NavigationView m_NavigationView;
    private NavController m_FragmentNavigationController;
    private NavHostFragment m_NavHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_DrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        m_NavigationView = findViewById(R.id.activity_main_navigation_view);
        m_NavHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_nav_host_fragment);
        m_FragmentNavigationController =  m_NavHostFragment.getNavController();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        getSupportFragmentManager().beginTransaction().add(
                R.id.activity_main_music_controller_RL,
                new MusicControlFragment(), "music_control_fragment")
                .commit();

        m_NavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                //TODO make it so its spesific for the "create song" button.
                if(m_FragmentNavigationController.getCurrentDestination().getId() != R.id.addSongFragment)
                m_FragmentNavigationController.navigate(R.id.addSongFragment);
                //TODO make the drawer close on backspace.
                m_DrawerLayout.closeDrawers();
                return false;
            }
        });




    }

    public void onCommandPressed(String i_Command)
    {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("command", i_Command);
        startService(intent);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            m_DrawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveSongExecute(Song i_Song)
    {
        SongManager.getInstance().addSong(i_Song, this);
        Snackbar.make(this, m_DrawerLayout, getString(R.string.save), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCannotSave()
    {
        Snackbar.make(this, m_DrawerLayout, getString(R.string.cannot_save), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSongSwipe(int i_SongPosition, RecyclerView i_RecycleView)
    {
        m_DisplayFragmentRecycleView = i_RecycleView;
        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment(i_SongPosition, i_RecycleView);
        deleteDialogFragment.show(getSupportFragmentManager(), "delete dialog fragment");
    }

    @Override
    public void onDialogClicked(int i_SongPosition, RecyclerView i_RecyclerView, boolean i_IsDelete) {

        if(i_IsDelete)
        {
            SongManager.getInstance().removeSong(i_SongPosition, this);
            i_RecyclerView.getAdapter().notifyItemRemoved(i_SongPosition);
            Intent intent = new Intent(this, MusicPlayerService.class);
            intent.putExtra("command", "song_deleted");
            intent.putExtra("song_position", i_SongPosition);
            startService(intent);
        }
        else
        {
            i_RecyclerView.getAdapter().notifyItemChanged(i_SongPosition);
        }

    }

    @Override
    public void onBackPressed() {
        if(m_DrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            m_DrawerLayout.closeDrawers();
        }
        else
        {
            super.onBackPressed();
        }
    }
}