package com.e.photounsplasht.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.e.photounsplasht.data.model.Image;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class Bindings {
    @BindingAdapter({"app:setImage"})
    public static void setImage(ImageView imageView, Image image) {
        imageView.setImageBitmap(null);
        if (image != null) {
            imageView.setBackgroundColor(Color.parseColor(image.getColor()));
            GlobalApplication.getImageLoader().displayImage(image.getUrls().getSmall(), imageView);
        }
    }

    @BindingAdapter({"app:setFullImage"})
    public static void setFullImage(ImageView imageView, Image image) {
        if (image != null) {
            GlobalApplication.getImageLoader().displayImage(
                    image.getUrls().getThumb(),
                    imageView,
                    new DisplayImageOptions.Builder()
                            .displayer(new FadeInBitmapDisplayer(100))
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build(),
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            GlobalApplication.getImageLoader().displayImage(image.getUrls().getRegular(), imageView);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
        } else {
            imageView.setImageBitmap(null);
        }
    }
}
