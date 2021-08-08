package com.hod.mediaplayer.model;

import android.net.Uri;

import java.io.Serializable;

public class Song implements Serializable {
    private String m_ImagePathUriParsed;
    private String m_Song;
    private String m_SongName;
    private String m_ArtistName;

    public Song(String i_ImagePath, String i_Song, String i_SongName, String i_ArtistName)
    {
        if(i_ImagePath != null)
        m_ImagePathUriParsed = Uri.parse(i_ImagePath).toString();
        m_Song = i_Song;
        m_SongName = i_SongName;
        m_ArtistName = i_ArtistName;
    }

    public String getImageUriParsed() {
        return m_ImagePathUriParsed;
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
