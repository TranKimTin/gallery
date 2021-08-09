package com.example.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.gallery.modal.AllImage;
import com.example.gallery.modal.MyImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "GALLERY_LOG";
    public static TreeMap<String, List<MyImage>> hAlbum = new TreeMap<>();
    public static List<MyImage> listAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragmentMain, AllFragment.getInstance(), null)
                .commit();
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        }
    }


    public static void open_file(Context context, Uri uri) {

        // Get URI and MIME type of file
        String mime = context.getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void getData(Context context) {
        hAlbum.clear();
        listAll.clear();

//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.SIZE,
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.BUCKET_ID};

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        String orderBy = MediaStore.MediaColumns.DATE_MODIFIED;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection,
                null, orderBy + " DESC");

        int columName = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int columSize = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE);
        int columId = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
        int columDate = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int column_album_name = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);
        int column_album_id = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            MyImage image = new MyImage();
            image.setName(cursor.getString(columName));
            image.setSize(cursor.getLong(columSize));
            image.setDate(cursor.getLong(columDate) / 86400);
            image.setUri(ContentUris.withAppendedId(uri, cursor.getLong(columId)));
            image.setUrl(cursor.getString(column_index_data));
            image.setAlbumName(cursor.getString(column_album_name));
            image.setAlmumId(cursor.getLong(column_album_id));

            listAll.add(image);
            if (hAlbum.get(image.getAlbumName()) == null){
                hAlbum.put(image.getAlbumName(), new ArrayList<>());
            }

            hAlbum.get(image.getAlbumName()).add(image);
        }
    }
}