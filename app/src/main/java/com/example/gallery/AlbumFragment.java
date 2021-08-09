package com.example.gallery;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gallery.adapter.AlbumAdapter;
import com.example.gallery.modal.Album;
import com.example.gallery.modal.MyImage;

import java.util.ArrayList;
import java.util.List;

import static com.example.gallery.MainActivity.TAG;
import static com.example.gallery.MainActivity.hAlbum;

public class AlbumFragment extends Fragment implements View.OnClickListener {
    private static AlbumFragment instance;
    private RecyclerView rcvAlbum;
    private List<Album> list;
    private ImageView imvAdd;

    public AlbumFragment() {
    }

    public static AlbumFragment getInstance() {
        if (instance == null) instance = new AlbumFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvAlbum = view.findViewById(R.id.rcvAlbum);
        imvAdd = view.findViewById(R.id.imvAddAlbum);

        imvAdd.setOnClickListener(this);
        rcvAlbum.setLayoutManager(new GridLayoutManager(getContext(), 3));
        list = new ArrayList<>();
        for (String name : hAlbum.keySet()) {
            List<MyImage> l = hAlbum.get(name);
            list.add(new Album(name, l.size(), l.size() > 0 ? l.get(0).getUri() : null));
        }
        AlbumAdapter adapter = new AlbumAdapter(getContext(), list);
        rcvAlbum.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvAddAlbum:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Tên album");
// Set up the input
                final EditText input = new EditText(getContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        addAdbum(m_Text);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
        }
    }

    private void addAdbum(String name) {

    }
}