package com.e.photounsplasht.data.source;

import androidx.paging.DataSource;

public class ImageDataSourceFactory extends DataSource.Factory {
    ImageDataSource imageDataSource;
    String searchQuery = "";

    public ImageDataSourceFactory() {
    }

    public ImageDataSourceFactory(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public DataSource create() {
        imageDataSource = new ImageDataSource(searchQuery);
        return imageDataSource;
    }

}
