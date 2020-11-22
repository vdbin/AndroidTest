package com.e.photounsplasht.data.source;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.data.repository.ImageRepository;
import com.e.photounsplasht.utils.GlobalApplication;

import java.util.List;

public class ImageDataSource extends PageKeyedDataSource<Integer, Image> {

    String searchQuery = "";

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public ImageDataSource(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Image> callback) {
        makeRequest(1, params.requestedLoadSize, new ImageRepository.ImageRepositoryCallback() {
            @Override
            public void result(List<Image> list, int currentPage, Integer prevPage, Integer nextPage) {
                callback.onResult(list, prevPage, nextPage);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Image> callback) {
        makeRequest(params.key, params.requestedLoadSize, new ImageRepository.ImageRepositoryCallback() {
            @Override
            public void result(List<Image> list, int currentPage, Integer prevPage, Integer nextPage) {
                callback.onResult(list, prevPage);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Image> callback) {
        makeRequest(params.key, params.requestedLoadSize, new ImageRepository.ImageRepositoryCallback() {
            @Override
            public void result(List<Image> list, int currentPage, Integer prevPage, Integer nextPage) {
                callback.onResult(list, nextPage);
            }
        });
    }

    private void makeRequest(int page, int perPage, ImageRepository.ImageRepositoryCallback imageRepositoryCallback) {
        if (getSearchQuery().isEmpty()) {
            GlobalApplication.getImageRepositoryInstance().getImagesByPage(page, perPage, imageRepositoryCallback);
        } else {
            GlobalApplication.getImageRepositoryInstance().getImageSearchByPage(getSearchQuery(), page, perPage, imageRepositoryCallback);
        }
    }
}
