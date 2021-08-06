package com.hod.mediaplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSongFragment extends Fragment
{
    private View m_RootView;
    private ImageView m_SongIv;
    private EditText m_SongLinkEt;
    private EditText m_SongNameEt;
    private EditText m_ArtistNameEt;
    private IAddSongFragmentListener m_Callback;
    private NavController m_NavController;
    private ActivityResultLauncher<Intent> m_ChooseFromUserGalleryLauncher;
    private ActivityResultLauncher<Intent> m_TakePictureLauncher;
    private File m_CapturedPictureFile;
    private Uri m_CapturedPicture;
    private String m_PicturePath;

    public interface IAddSongFragmentListener
    {
        public void onSaveSongExecute(Song i_Song);
        public void onCannotSave();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        m_RootView = inflater.inflate(R.layout.fragment_add_song, container, false);
        m_SongIv = m_RootView.findViewById(R.id.fragment_add_song_image);
        ImageView addFromAlbumIv = m_RootView.findViewById(R.id.fragment_add_song_add_from_gallery);
        ImageView addFromCameraIv = m_RootView.findViewById(R.id.fragment_add_song_add_camera_image);
        m_SongLinkEt = m_RootView.findViewById(R.id.fragment_add_song_link_et);
        m_SongNameEt = m_RootView.findViewById(R.id.fragment_add_song_name_et);
        m_ArtistNameEt = m_RootView.findViewById(R.id.fragment_add_song_artist_name_et);
        Button saveBtn = m_RootView.findViewById(R.id.fragment_add_song_save_btn);
        m_NavController = NavHostFragment.findNavController(this);

        m_CapturedPictureFile = new File(
                ((Activity)m_Callback).getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+ ".jpg");

        m_TakePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Glide.with(((Activity)m_Callback).getBaseContext()).load(m_CapturedPicture).into(m_SongIv);
                    m_PicturePath = m_CapturedPicture.toString();
                }
            }
        });

        m_ChooseFromUserGalleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Context context = ((Activity)m_Callback).getBaseContext();
                    Uri pic = result.getData().getData();
                    Glide.with(context).load(pic).into(m_SongIv);
                    context.getContentResolver().takePersistableUriPermission(
                            pic, result.getData().getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            + Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    );
                    m_PicturePath = pic.toString();
                }
            }
        });

        saveBtn.setOnClickListener(v -> saveWasPressed());
        addFromAlbumIv.setOnClickListener(v -> takeImageFromGallery());
        addFromCameraIv.setOnClickListener(v -> takePicture());

        return m_RootView;
    }

    private void takeImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        m_ChooseFromUserGalleryLauncher.launch(intent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try
        {
            m_Callback = (IAddSongFragmentListener)context;
        }
        catch (ClassCastException ex)
        {
            throw new ClassCastException("Activity does not implement interface IAddSongFragmentListener");
        }
    }

    private void saveWasPressed()
    {
        if(checkIfSaveIsPossible())
        {
            Song song = new Song(
                    m_PicturePath,
                    m_SongLinkEt.getText().toString(),
                    m_SongNameEt.getText().toString(),
                    m_ArtistNameEt.getText().toString());

            m_Callback.onSaveSongExecute(song);
            m_NavController.popBackStack();
        }
        else
        {
            m_Callback.onCannotSave();
        }
    }

    private boolean checkIfSaveIsPossible()
    {
        boolean isSavePossible = true;
        if(
                        m_SongLinkEt.getText().toString().isEmpty() ||
                        m_SongNameEt.getText().toString().isEmpty() ||
                        m_ArtistNameEt.getText().toString().isEmpty()
        )
        {
            isSavePossible = false;
        }

        return isSavePossible;
    }

    private void takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        m_CapturedPicture = FileProvider.getUriForFile(((Activity)m_Callback).getBaseContext(), ((Activity)m_Callback).getPackageName() + ".provider", m_CapturedPictureFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, m_CapturedPicture);
        m_TakePictureLauncher.launch(intent);
    }
}
