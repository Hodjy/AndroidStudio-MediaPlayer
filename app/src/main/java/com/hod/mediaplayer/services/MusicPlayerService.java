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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;

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

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        createOnClickPendingIntent(remoteViews, "play", 0, R.id.notification_play);
        createOnClickPendingIntent(remoteViews, "pause", 1, R.id.notification_pause);
        createOnClickPendingIntent(remoteViews, "next", 2, R.id.notification_next);
        createOnClickPendingIntent(remoteViews, "previous", 3, R.id.notification_previous);
        createOnClickPendingIntent(remoteViews, "close", 4, R.id.notification_closeIB);

        builder.setCustomContentView(remoteViews);
        builder.setSmallIcon(android.R.drawable.ic_media_play);

        startForeground(NOTIF_ID, builder.build());
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
            case "new_instance":
                if(m_Player.isPlaying() || m_IsPaused)
                {
                    m_Player.stop();
                }
                Bundle bundle = intent.getExtras();
                m_Songs = (ArrayList<Song>)bundle.getSerializable("songs");
                m_CurrentlyPlaying = bundle.getInt("song_index");
                playSongFromList();
                break;
            case "play":
                if(!m_Player.isPlaying())
                    play();
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

/*        if(!m_Player.isPlaying())
        {
            try
            {
                m_Player.setDataSource(link);
                m_Player.prepareAsync();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }*/

        return super.onStartCommand(intent, flags, startId);
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

    //TODO snackbar on new song.(if possible)
    private void play()
    {
        m_Player.start();
        m_IsPaused = false;
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

    /**
     * this is the first attempt at notification + channel, saved for reference.
     */
    private void startForegroundFirstAppempt() {
        m_Player.start();
        @SuppressLint("RemoteViewLayout")
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setSmallIcon(android.R.drawable.ic_media_ff)
                .setContentTitle("Playing Music")
                .setContentText("This is my first song!")
                .setContent(remoteViews);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startForegroundWithChannel(builder);
        }
        else
        {
            startForeground(1, builder.build());
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundWithChannel(Notification.Builder i_Notification)
    {
        final String NOTIFICATION_CHANNEL_ID = "Music";
        String channelName = "Music Background Service";
        NotificationChannel notificationChannel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;

        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager.createNotificationChannel(notificationChannel);

         i_Notification.setOngoing(true)
                 .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE);

         startForeground(2, i_Notification.build());
    }
}
