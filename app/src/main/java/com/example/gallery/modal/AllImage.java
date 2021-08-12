package com.example.gallery.modal;

import java.util.Date;
import java.util.List;

public class AllImage {
    private Date date;
    private List<MyImage> list;



    public AllImage() {
    }

    public AllImage(Date date, List<MyImage> list) {
        this.date = date;
        this.list = list;
        fillChecked(false);
    }

    public void fillChecked(boolean checked) {
        for (int i = 0; i < list.size(); i++)
            list.get(i).setChecked(checked);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<MyImage> getList() {
        return list;
    }

    public void setList(List<MyImage> list) {
        this.list = list;
    }
}
