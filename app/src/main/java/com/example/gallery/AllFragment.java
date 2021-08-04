package com.example.gallery;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AllFragment extends Fragment {
    private  static AllFragment instance;
    public AllFragment() {

    }

    public static AllFragment newInstance() {
        instance = new AllFragment();
        return instance;
    }

    public static AllFragment getInstance() {
        if(instance == null) instance = new AllFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all, container, false);
    }
}