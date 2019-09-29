package com.reflexit.tastier.model;

import android.view.View;

public class MainMenuSelector {

    private String sectionName;
    private int imageResource;
    private View.OnClickListener onClickListener;

    public MainMenuSelector(int imageResource,String sectionName, View.OnClickListener onClickListener) {
        this.sectionName = sectionName;
        this.onClickListener = onClickListener;
        this.imageResource = imageResource;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
