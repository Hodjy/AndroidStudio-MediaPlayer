package com.hod.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hod.mediaplayer.fragments.MusicControlFragment;

public class MainActivity extends AppCompatActivity implements MusicControlFragment.IMusicControlFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(
                R.id.activity_main_music_controller_RL,
                new MusicControlFragment(), "music_control_fragment")
                .commit();

    }

    public void onCommandPressed(String i_Command)
    {
        Toast.makeText(this, i_Command, Toast.LENGTH_SHORT).show();
    }
}