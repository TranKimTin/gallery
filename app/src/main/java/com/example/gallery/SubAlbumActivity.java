package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gallery.adapter.SubAlbumAdapter;
import com.example.gallery.modal.MyImage;

import java.util.List;

import static com.example.gallery.MainActivity.*;

public class SubAlbumActivity extends AppCompatActivity {
    private TextView tvNameAlbum;
    private RecyclerView rcvSubAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_album);

        tvNameAlbum = findViewById(R.id.tvNameSubAlbum);
        rcvSubAlbum = findViewById(R.id.rcvSubAlbum);
        rcvSubAlbum.setHasFixedSize(true);
        rcvSubAlbum.setLayoutManager(new GridLayoutManager(this, 4));

        String album = getIntent().getStringExtra("album_name");
        List<MyImage> list = hAlbum.get(album);
        SubAlbumAdapter adapter = new SubAlbumAdapter(this, list);

        tvNameAlbum.setText(album);
        rcvSubAlbum.setAdapter(adapter);
    }
}