package com.hod.mediaplayer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hod.mediaplayer.R;
import com.hod.mediaplayer.model.Song;

public class DeleteDialogFragment extends DialogFragment
{
    private IDialogFragmentListener m_Callback;
    private int m_SelectedSong;
    private RecyclerView m_RecyclerView;

    public interface IDialogFragmentListener
    {
        public void onDialogClicked(int i_SongPosition, RecyclerView i_RecyclerView,boolean i_IsDelete);
    }

    public DeleteDialogFragment(int i_SongPosition, RecyclerView i_RecyclerView)
    {
        m_SelectedSong = i_SongPosition;
        m_RecyclerView = i_RecyclerView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme())
        {
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                m_Callback.onDialogClicked(m_SelectedSong, m_RecyclerView, false);
            }
        };

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.dialog_fragment_delete, container,false);
        TextView yesTv = rootView.findViewById(R.id.dialog_fragment_delete_yes_tv);
        TextView noTv = rootView.findViewById(R.id.dialog_fragment_delete_no_tv);

        yesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Callback.onDialogClicked(m_SelectedSong, m_RecyclerView, true);
                getDialog().dismiss();
            }
        });

        noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_Callback.onDialogClicked(m_SelectedSong, m_RecyclerView, false);
                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        try
        {
            m_Callback = (IDialogFragmentListener)context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("Activity does not implement interface: IDialogFragmentListener.");
        }
    }


}
