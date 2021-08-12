package com.example.gallery.modal;

import android.net.Uri;

import java.util.List;

public class Album {
    private String name;
    private int count;
    private Uri uri;
    private String url;

    public Album(String name, int count, Uri uri, String url) {
        this.name = name;
        this.count = count;
        this.uri = uri;
        this.url = url.substring(0, url.lastIndexOf('/'));
    }

    public Album() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        url = url;
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
