package com.example.gallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumFragment extends Fragment {
    private  static AlbumFragment instance;
    public AlbumFragment() {
    }

    public static AlbumFragment newInstance() {
        instance = new AlbumFragment();
        return instance;
    }
    public static AlbumFragment getInstance() {
        if(instance == null) instance = new AlbumFragment();
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}