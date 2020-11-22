package com.e.photounsplasht.data.disk;

public abstract class SaveImageCallback {
    public abstract void onStart(String imageId);

    public abstract void onFinish(String imageId, boolean isSuccess);
}
