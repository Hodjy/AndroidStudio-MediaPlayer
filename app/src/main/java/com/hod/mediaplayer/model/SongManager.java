package com.hod.mediaplayer.model;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SongManager
{
    ArrayList<Song> m_Songs;
    private static SongManager m_Instance;
    private static final String SONGS_FILE_NAME = "Songs";

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

    private ArrayList<Song> loadDefaultSongs()
    {
        String path = "android.resource://com.hod.mediaplayer/drawable/bob_dylan";
        m_Songs.add(new Song(path.toString(),"https://www.syntax.org.il/xtra/bob.m4a", "One More Cup Of Coffee","Bob Dylan"));
        m_Songs.add(new Song(path.toString(), "https://www.syntax.org.il/xtra/bob1.m4a", "Sara", "Bob Dylan"));
        m_Songs.add(new Song(path.toString(),"https://www.syntax.org.il/xtra/bob2.mp3","The Man In Me","Bob Dylan"));



        return m_Songs;
    }

    private void saveSongs(Context i_Activity)
    {
        try {
            FileOutputStream fos = i_Activity.openFileOutput(SONGS_FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos  = new ObjectOutputStream(fos);
            oos.writeObject(m_Songs);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Song> loadSongs(Context i_Activity)
    {
        try {
            FileInputStream fis  = i_Activity.openFileInput(SONGS_FILE_NAME);
            ObjectInputStream ois  = new ObjectInputStream(fis);
            m_Songs = (ArrayList<Song>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            if(m_Songs.size() == 0)
            m_Songs = loadDefaultSongs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return m_Songs;
    }

    //TODO make it save after adding.
    public void addSong(Song i_Song)
    {
        m_Songs.add(i_Song);
    }
    public void removeSong(int i_SongPosition)
    {
        m_Songs.remove(m_Songs.get(i_SongPosition));
    }

    public void setSongsList(ArrayList<Song> i_Songs)
    {
        m_Songs = i_Songs;
    }
}
