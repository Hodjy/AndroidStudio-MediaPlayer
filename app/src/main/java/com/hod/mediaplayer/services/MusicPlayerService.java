package com.hod.mediaplayer.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;
import com.hod.mediaplayer.model.SongManager;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener
{
    private MediaPlayer m_Player = new MediaPlayer();
    private final int NOTIF_ID = 1;
    private ArrayList<Song> m_Songs;
    private int m_CurrentlyPlaying;
    private boolean m_IsPaused = false;
    private RemoteViews m_RemoteViews;
    private NotificationCompat.Builder m_Builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        m_Player.setOnCompletionListener(this);
        m_Player.setOnPreparedListener(this);
        m_Player.reset();

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String channelID = "music_id";

        if(Build.VERSION.SDK_INT>=26)
        {
            String channelName = "Music Channel";
            NotificationChannel channel = new NotificationChannel(
                    channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

            manager.createNotificationChannel(channel);
        }

        m_Builder = new NotificationCompat.Builder(this, channelID);
        m_RemoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        createOnClickPendingIntent(m_RemoteViews, "play", 0, R.id.notification_play);
        createOnClickPendingIntent(m_RemoteViews, "pause", 1, R.id.notification_pause);
        createOnClickPendingIntent(m_RemoteViews, "next", 2, R.id.notification_next);
        createOnClickPendingIntent(m_RemoteViews, "previous", 3, R.id.notification_previous);
        createOnClickPendingIntent(m_RemoteViews, "close", 4, R.id.notification_closeIB);

        m_Builder.setCustomContentView(m_RemoteViews);
        m_Builder.setSmallIcon(android.R.drawable.ic_media_play);

        startForeground(NOTIF_ID, m_Builder.build());
    }

    private void createOnClickPendingIntent(
            RemoteViews remoteViews, String i_Command,
            int i_RequestCode,int i_ViewID ) {
        Intent intent = new Intent(this, MusicPlayerService.class);

        intent.putExtra("command", i_Command);
        PendingIntent pendingIntent = PendingIntent.getService(
                this, i_RequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(i_ViewID, pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String command = intent.getStringExtra("command");

        if(command == null)
            command = "null";

        switch(command)
        {
            case "set_songs":
                setSongs();
                break;
            case "play":
                if(!m_Player.isPlaying())
                {
                    if(m_Songs == null)
                    {
                        setSongs();
                    }
                    play();
                }
                break;
            case "pause":
                if(m_Player.isPlaying())
                    pause();
                break;
            case "next":
                next();
                break;
            case "previous":
                if(m_Player.isPlaying() || m_IsPaused)
                    changeSongFromList(false);
                break;
            case "close":
                stopSelf();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void setSongs()
    {
        if(m_Player.isPlaying() || m_IsPaused)
        {
            m_Player.stop();
        }
        m_Songs = SongManager.getInstance().loadSongs(this);
        m_CurrentlyPlaying = 0;
    }

    private void next()
    {
        if(m_Player.isPlaying() || m_IsPaused)
            changeSongFromList(true);
    }

    private void changeSongFromList(boolean i_IsNext)
    {
        if(i_IsNext)
        {
            m_CurrentlyPlaying++;
            if(m_CurrentlyPlaying == m_Songs.size())
            {
                m_CurrentlyPlaying = 0;
            }
        }
        else
        {
            m_CurrentlyPlaying--;

            if(m_CurrentlyPlaying < 0)
            {
                m_CurrentlyPlaying = m_Songs.size() - 1;
            }
        }

        m_Player.reset();
        playSongFromList();
    }

    /**
     * will play by using the "m_CurrentlyPlaying" as list index.
     */
    private void playSongFromList()
    {
        try
        {
            m_Player.setDataSource(m_Songs.get(m_CurrentlyPlaying).getSong());
            m_Player.prepareAsync();
        } catch (IOException e)
        {
            e.printStackTrace();
            next();
        }
    }

    private void play()
    {
        if(m_Songs.size() > 0)
        {
            m_Player.start();
            m_RemoteViews.setTextViewText(R.id.notification_song_title_tv, m_Songs.get(m_CurrentlyPlaying).getSongName());
            m_Builder.setContentText(m_Songs.get(m_CurrentlyPlaying).getSongName());
            startForeground(NOTIF_ID, m_Builder.build());
            m_IsPaused = false;
        }
        else
        {
            Toast.makeText(this, "No songs", Toast.LENGTH_SHORT).show();
            stopSelf();
        }

    }

    private void pause()
    {
        m_Player.pause();
        m_IsPaused = true;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(m_Player != null)
        {
            if(m_Player.isPlaying())
            {
                m_Player.stop();
            }
            m_Player.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        changeSongFromList(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        play();
    }
}
