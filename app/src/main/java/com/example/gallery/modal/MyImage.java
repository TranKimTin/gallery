package com.example.gallery.modal;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.Date;

public class MyImage {
    private Uri uri;
    private String name;
    private long size;
    private long date;
    private String url;
    private String albumName;
    private long almumId;
    boolean checked;

    public MyImage(Uri uri, String name, long size, long date, String url, String albumName, long almumId, boolean checked) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.date = date;
        this.url = url;
        this.albumName = albumName;
        this.almumId = almumId;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Bitmap toBitmap(Context context){
        try {
            return MediaStore.Images.Media.getBitmap(context.getContentResolver() , uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getAlmumId() {
        return almumId;
    }

    public void setAlmumId(long almumId) {
        this.almumId = almumId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MyImage() {
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
