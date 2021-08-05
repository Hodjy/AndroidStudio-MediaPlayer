package com.hod.mediaplayer.model;

import android.net.Uri;

import java.util.ArrayList;

public class SongManager
{
    ArrayList<Song> m_Songs;
    private static SongManager m_Instance;

    private SongManager()
    {
        m_Songs = new ArrayList<>();
    }

    public static SongManager getInstance()
    {
        if(m_Instance == null)
        {
            m_Instance = new SongManager();
        }

        return m_Instance;
    }

    public ArrayList<Song> loadDefaultSongs()
    {
        Uri uri = Uri.parse("android.resource://com.hod.mediaplayer/drawable/bob_dylan");
        m_Songs.add(new Song(uri.toString(),"https://www.syntax.org.il/xtra/bob.m4a", "One More Cup Of Coffee","Bob Dylan"));
        m_Songs.add(new Song(uri.toString(), "https://www.syntax.org.il/xtra/bob1.m4a", "Sara", "Bob Dylan"));
        m_Songs.add(new Song(uri.toString(),"https://www.syntax.org.il/xtra/bob2.mp3","The Man In Me","Bob Dylan"));

        return m_Songs;
    }

}
