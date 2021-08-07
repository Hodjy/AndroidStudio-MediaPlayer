package com.hod.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.fragments.AddSongFragment;
import com.hod.mediaplayer.fragments.MusicControlFragment;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongManager;
import com.hod.mediaplayer.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity
        implements MusicControlFragment.IMusicControlFragmentListener,
        AddSongFragment.IAddSongFragmentListener
{
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
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

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
        SongManager.getInstance().addSong(i_Song);
        //TODO make proper string
        Snackbar.make(this, m_DrawerLayout, "Saved", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCannotSave()
    {
        //TODO make proper string
        Snackbar.make(this, m_DrawerLayout, "Cannot save before all details are filled.", Snackbar.LENGTH_SHORT).show();
    }
}