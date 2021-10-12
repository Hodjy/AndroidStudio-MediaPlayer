package com.hod.mediaplayer.model;

import android.app.Activity;
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

    private SongManager() { }

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
        m_Songs = new ArrayList<>();

        String path = "android.resource://com.hod.mediaplayer/drawable/never_give_u_up_cover";
        m_Songs.add(new Song(path, "https://drive.google.com/uc?export=download&id=12i2QWvokUfJq4Z896cKjoSo-xljRaOsZ",
                "Never Gonna Give You Up", "Rick Astley"));

        path = "android.resource://com.hod.mediaplayer/drawable/scatman_john";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1H9UDy1N8oHnbBwL0hbDUED0un_VPUxBO",
                "I'm A Scatman!","Scatman John"));

        path = "android.resource://com.hod.mediaplayer/drawable/smash_mouth";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1BNxjyssMnBZFoSsFhpuM1TfUpDBeO6VX",
                "All Star","Smash Mouth"));

        path = "android.resource://com.hod.mediaplayer/drawable/the_bee_gees_stayin_alive";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1TdDzePQTxPdTGE0XVrvsWe5qoMdv_Tcy",
                "Stayin' Alive","Bee Gees"));

        path = "android.resource://com.hod.mediaplayer/drawable/dont_fear_the_reaper";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1XgEL9IAZ5Ycwu2qqYijRTQl6V7SxnxLP",
                "(Don't Fear) The Reaper","Blue Oyster Cult"));

        path = "android.resource://com.hod.mediaplayer/drawable/knights_of_cydonia";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1IMfUwg2kQrMJTJeeEZqkbhDqto_XwL4I",
                "Knights Of Cydonia","Muse"));

        path = "android.resource://com.hod.mediaplayer/drawable/gorillaz";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1GoCSgEe_DTy3dkzcrGEengzlt9HoDDCd",
                "Feel Good Inc.","Gorillaz"));

        path = "android.resource://com.hod.mediaplayer/drawable/big_iron";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1YavZ3_5fTwfUBRXaJrYK5p5YlWXluKMc",
                "Big Iron","Marty Robbins"));

        path = "android.resource://com.hod.mediaplayer/drawable/down_the_road";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1Ze0inGR834ooaeeeu4dEmn5IQSgMVPNr",
                "Down The Road","C2C"));

        path = "android.resource://com.hod.mediaplayer/drawable/to_never_feel_happy";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1J3V7u5ZARuv5kCwijQNN4hD9bVv3mJ7U",
                "Happy","C2C"));

        path = "android.resource://com.hod.mediaplayer/drawable/bob_dylan";
        m_Songs.add(new Song(path,"https://www.syntax.org.il/xtra/bob.m4a", "One More Cup Of Coffee","Bob Dylan"));
        m_Songs.add(new Song(path, "https://www.syntax.org.il/xtra/bob1.m4a", "Sara", "Bob Dylan"));
        m_Songs.add(new Song(path,"https://www.syntax.org.il/xtra/bob2.mp3","The Man In Me","Bob Dylan"));

        path = "android.resource://com.hod.mediaplayer/drawable/red_hot_chilli_cant_stop";
        m_Songs.add(new Song(path,"https://drive.google.com/uc?export=download&id=1ALbcyUNyF_SFYdzwrseN4Z68r1Eyi21b",
                "Can't Stop","Red Hot Chilli Peppers"));

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

    public ArrayList<Song> getSongs(Context i_Context)
    {
        if(m_Songs == null)
        {
            loadSongs(i_Context);
        }

        return m_Songs;
    }

    private ArrayList<Song> loadSongs(Context i_Context)
    {
        try {
            FileInputStream fis  = i_Context.openFileInput(SONGS_FILE_NAME);
            ObjectInputStream ois  = new ObjectInputStream(fis);
            m_Songs = (ArrayList<Song>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            if(m_Songs == null)
            m_Songs = loadDefaultSongs();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return m_Songs;
    }

    public void addSong(Song i_Song, Context i_Context)
    {
        if(m_Songs == null)
            loadSongs(i_Context);
        m_Songs.add(i_Song);
        saveSongs(i_Context);
    }
    public void removeSong(int i_SongPosition, Context i_Context)
    {
        if(m_Songs == null)
            loadSongs(i_Context);

        m_Songs.remove(m_Songs.get(i_SongPosition));
        saveSongs(i_Context);
    }

    public void setSongsList(ArrayList<Song> i_Songs, Context i_Context)
    {
        m_Songs = i_Songs;
        saveSongs(i_Context);
    }
}
