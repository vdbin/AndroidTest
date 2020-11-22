package com.e.photounsplasht.data.net;

import com.e.photounsplasht.data.model.DownloadImage;
import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.data.model.SearchImageResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataUnsplashService {

    @GET("/photos")
    Call<List<Image>> getAllImages(@Query("page") int page, @Query("per_page") Integer perPage);

    @GET("photos/{id}/download")
    Call<DownloadImage> getDownloadImageLink(@Path("id") String id);

    @GET("search/photos")
    Call<SearchImageResult> searchImages(@Query("query") String query, @Query("page") int page, @Query("per_page") Integer perPage);
}
