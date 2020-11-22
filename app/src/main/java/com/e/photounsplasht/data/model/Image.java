package com.e.photounsplasht.data.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    private String id;
    @SerializedName("color")
    private String color;
    @SerializedName("urls")
    private Urls urls;

    public Image(String id, String color, Urls urls) {
        this.id = id;
        this.color = color;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static final DiffUtil.ItemCallback<Image> CALLBACK = new DiffUtil.ItemCallback<Image>() {
        @Override
        public boolean areItemsTheSame(@NonNull Image image, @NonNull Image t1) {
            return image.id.equals(t1.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Image image, @NonNull Image t1) {
            return true;
        }
    };
}
