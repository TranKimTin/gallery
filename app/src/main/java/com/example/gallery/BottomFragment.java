package com.example.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BottomFragment extends Fragment implements View.OnClickListener {
    private String TAG = "GALLERY_LOG";
    private Button btnAll, btnAlbum;
    private static BottomFragment instance;

    public BottomFragment() {

    }

    public static BottomFragment getInstance() {
        if (instance == null) instance = new BottomFragment();
        return instance;
    }

    public static BottomFragment newInstance() {
        instance = new BottomFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAll = view.findViewById(R.id.btnAll);
        btnAlbum = view.findViewById(R.id.btnAlbum);

        btnAll.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager;
        Log.d(TAG, "CLICK");
        switch (v.getId()) {
            case R.id.btnAll:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentMain, AllFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();
                break;
            case R.id.btnAlbum:
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentMain, AlbumFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name") // name can be null
                        .commit();
                break;
        }
    }

}