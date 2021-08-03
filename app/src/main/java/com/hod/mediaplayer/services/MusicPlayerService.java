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
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.hod.mediaplayer.R;

import java.io.IOException;

public class MusicPlayerService extends Service
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer m_Player = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m_Player.setOnCompletionListener(this);
        m_Player.setOnPreparedListener(this);
        m_Player.reset();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String link = intent.getStringExtra("link");

        if(!m_Player.isPlaying())
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
        }

        return super.onStartCommand(intent, flags, startId);
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
        stopSelf();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
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
