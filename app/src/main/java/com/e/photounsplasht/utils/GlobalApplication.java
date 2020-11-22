package com.e.photounsplasht.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.e.photounsplasht.data.net.HeaderInterceptor;
import com.e.photounsplasht.data.repository.ImageNetworkRepository;
import com.e.photounsplasht.data.repository.ImageRepository;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalApplication extends Application {
    private static Context appContext;
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.unsplash.com";
    private static final String ACCESS_KEY = "t7LUxdUAeqb-3p9M9rVXWOAd5zjYjApoXXsUykD9RJo";
    //    private static final String ACCESS_KEY = "1a28e59e586593faf822eb102154d46e8f56c830d3e5d896a0293804233f991a";
    private static ImageRepository imageRepository;

    public static ImageRepository getImageRepositoryInstance() {
        if (imageRepository == null) {
            imageRepository = new ImageNetworkRepository();
        }
        return imageRepository;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HeaderInterceptor(ACCESS_KEY))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ImageLoader getImageLoader() {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(getAppContext())
                    .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .resetViewBeforeLoading(false)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build())
                    .build());
        }
        return ImageLoader.getInstance();
    }

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

}
