package com.hod.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.hod.mediaplayer.fragments.MusicControlFragment;

public class MainActivity extends AppCompatActivity implements MusicControlFragment.IMusicControlFragmentListener
{
    private DrawerLayout m_DrawerLayout;
    private NavigationView m_NavigationView;
    private NavController m_FragmentNavigationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_DrawerLayout = findViewById(R.id.activity_main_drawer_layout);
        m_NavigationView = findViewById(R.id.activity_main_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.activity_main_nav_host_fragment);
        m_FragmentNavigationController =  navHostFragment.getNavController();
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
                m_FragmentNavigationController.navigate(R.id.addSongFragment);
                m_DrawerLayout.closeDrawers();
                return false;
            }
        });

    }

    public void onCommandPressed(String i_Command)
    {
        Toast.makeText(this, i_Command, Toast.LENGTH_SHORT).show();
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
}