package com.example.gallery.modal;

import android.net.Uri;

import java.util.List;

public class Album {
    private String name;
    private int count;
    private Uri uri;

    public Album(String name, int count, Uri uri) {
        this.name = name;
        this.count = count;
        this.uri = uri;
    }

    public Album() {
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
