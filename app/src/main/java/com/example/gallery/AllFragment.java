package com.example.gallery;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gallery.adapter.AllAdapter;
import com.example.gallery.modal.AllImage;
import com.example.gallery.modal.MyImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.gallery.MainActivity.*;

public class AllFragment extends Fragment {
    private static AllFragment instance;
    private RecyclerView rcvAll;
    private List<AllImage> list;
    private AllAdapter adapter;

    public AllFragment() {

    }

    public static AllFragment getInstance() {
        if (instance == null) instance = new AllFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvAll = view.findViewById(R.id.rcvAll);
        rcvAll.setNestedScrollingEnabled(false);

//        rcvAll.setHasFixedSize(true);

        list = new ArrayList<>();
        getData(getContext());

        List<MyImage> item = new ArrayList<>();
        for(int i=0; i<listAll.size(); i++){
            if(i > 0 && listAll.get(i).getDate() != listAll.get(i-1).getDate()){
                list.add(new AllImage(new Date(listAll.get(i-1).getDate() * 86400000), item));
                item = new ArrayList<>();
            }
            item.add(listAll.get(i));
        }
        if(item.size() > 0) list.add(new AllImage(new Date(item.get(0).getDate() * 86400000), item));
        adapter = new AllAdapter(getContext(), list);
        rcvAll.setAdapter(adapter);
        rcvAll.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}