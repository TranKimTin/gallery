package com.example.gallery.modal;

import android.net.Uri;

public class MyImage {
    private Uri uri;
    private String name;

    public MyImage(Uri uri, String name) {
        this.uri = uri;
        this.name = name;
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
