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

    List<MyImage> getListImage() {
        Uri uri;
        Cursor cursor;
        List<MyImage> listOfAllImages = new ArrayList<MyImage>();
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.DATA};

        String orderBy = MediaStore.Images.Media.DATE_MODIFIED;
        cursor = getActivity().getContentResolver().query(uri, projection, null,
                null, orderBy + " DESC");
        int columName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int columSize = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        int columId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int columDate = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        while (cursor.moveToNext()) {
            MyImage image = new MyImage();
            image.setName(cursor.getString(columName));
            image.setSize(cursor.getLong(columSize));
            image.setDate(cursor.getLong(columDate) / 86400);
            image.setUri(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getLong(columId)));
            image.setUrl(cursor.getString(column_index_data));
            listOfAllImages.add(image);
//            if(listOfAllImages.size() > 10) break;;
        }
        return listOfAllImages;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "1111");
        rcvAll = view.findViewById(R.id.rcvAll);
//        rcvAll.setHasFixedSize(true);

        list = new ArrayList<>();

        List<MyImage> all = getListImage();
        List<MyImage> item = new ArrayList<>();
        for(int i=0; i<all.size(); i++){
            if(i > 0 && all.get(i).getDate() != all.get(i-1).getDate()){
                list.add(new AllImage(new Date(all.get(i-1).getDate() * 86400000), item));
                item = new ArrayList<>();
            }
            item.add(all.get(i));
        }
        if(item.size() > 0) list.add(new AllImage(new Date(item.get(0).getDate() * 86400000), item));
        adapter = new AllAdapter(getContext(), list);
        rcvAll.setAdapter(adapter);
        rcvAll.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}