package com.example.gallery.modal;

import android.net.Uri;

import java.util.Date;

public class MyImage {
    private Uri uri;
    private String name;
    private long size;
    private long date;
    private String url;

    public MyImage(Uri uri, String name, long size, long date, String url) {
        this.uri = uri;
        this.name = name;
        this.size = size;
        this.date = date;
        this.url = url;
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
