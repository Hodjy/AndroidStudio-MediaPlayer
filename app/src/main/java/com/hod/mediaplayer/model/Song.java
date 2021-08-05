package com.hod.mediaplayer.model;

import android.net.Uri;

import java.io.Serializable;


//TODO check if needed artist and song name.
public class Song implements Serializable {
    private Uri m_Image;
    private String m_Song;
    private String m_SongName;
    private String m_ArtistName;

    public Song(Uri i_Image, String i_Song, String i_SongName, String i_ArtistName)
    {
        m_Image = i_Image;
        m_Song = i_Song;
        m_SongName = i_SongName;
        m_ArtistName = i_ArtistName;
    }

    //TODO Remove
    public Song(String i_Name)
    {
        m_SongName = i_Name;
    }

    //TODO Remove
    public Song(String i_Name, String i_Song)
    {
        m_SongName = i_Name;
        m_Song = i_Song;
    }


    public Uri getImage() {
        return m_Image;
    }

    public String getSong() {
        return m_Song;
    }

    public String getSongName() {
        return m_SongName;
    }

    public String getArtistName() {
        return m_ArtistName;
    }
}
