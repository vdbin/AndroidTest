package com.e.photounsplasht.data.repository;

import android.widget.Toast;

import com.e.photounsplasht.data.model.DownloadImage;
import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.data.model.SearchImageResult;
import com.e.photounsplasht.data.net.GetDataUnsplashService;
import com.e.photounsplasht.utils.GlobalApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageNetworkRepository implements ImageRepository {

    @Override
    public void getImagesByPage(int page, int perPage, ImageRepositoryCallback imageRepositoryCallback) {
        Call<List<Image>> data = GlobalApplication.getRetrofitInstance()
                .create(GetDataUnsplashService.class)
                .getAllImages(page, perPage);

        data.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                List<Image> imagesList = response.body();
                if (imagesList != null) {
                    imageRepositoryCallback.result(imagesList, page, page - 1 < 1 ? null : page - 1, page + 1);
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Toast.makeText(GlobalApplication.getAppContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void getImageSearchByPage(String query, int page, int perPage, ImageRepositoryCallback imageRepositoryCallback) {
        Call<SearchImageResult> searchImageResultCall = GlobalApplication
                .getRetrofitInstance()
                .create(GetDataUnsplashService.class)
                .searchImages(query, page, perPage);

        searchImageResultCall.enqueue(new Callback<SearchImageResult>() {
            @Override
            public void onResponse(Call<SearchImageResult> call, Response<SearchImageResult> response) {
                SearchImageResult results = response.body();
                List<Image> imagesList = new ArrayList<>();
                if (results != null) {
                    imagesList = results.getResults();
                }
                imageRepositoryCallback.result(imagesList, page, page - 1 < 1 ? null : page - 1, page + 1);
            }

            @Override
            public void onFailure(Call<SearchImageResult> call, Throwable t) {
                Toast.makeText(GlobalApplication.getAppContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void getDownloadImage(String idImage, ImageRepositoryCallback imageRepositoryCallback) {
        Call<DownloadImage> downloadImageCall = GlobalApplication
                .getRetrofitInstance()
                .create(GetDataUnsplashService.class)
                .getDownloadImageLink(idImage);
        downloadImageCall.enqueue(new Callback<DownloadImage>() {
            @Override
            public void onResponse(Call<DownloadImage> call, Response<DownloadImage> response) {
                DownloadImage result = response.body();
//                При необходимости можно получить и сохранить изображение по ссылке загрузки
            }

            @Override
            public void onFailure(Call<DownloadImage> call, Throwable t) {
                Toast.makeText(GlobalApplication.getAppContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
