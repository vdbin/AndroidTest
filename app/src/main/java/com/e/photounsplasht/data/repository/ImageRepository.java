package com.e.photounsplasht.data.repository;

import com.e.photounsplasht.data.model.Image;

import java.util.List;

public interface ImageRepository {
    public void getImagesByPage(int page, int perPage, ImageRepositoryCallback imageRepositoryCallback);

    public void getImageSearchByPage(String query, int page, int perPage, ImageRepositoryCallback imageRepositoryCallback);

    public void getDownloadImage(String idImage, ImageRepositoryCallback imageRepositoryCallback);

    public abstract class ImageRepositoryCallback {
        public abstract void result(List<Image> list, int currentPage, Integer prevPage, Integer nextPage);
    }
}
