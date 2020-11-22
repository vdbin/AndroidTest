package com.e.photounsplasht.view.model;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.e.photounsplasht.R;
import com.e.photounsplasht.adapter.ImageAdapter;
import com.e.photounsplasht.data.disk.CheckPermission;
import com.e.photounsplasht.data.disk.SaveImage;
import com.e.photounsplasht.data.disk.SaveImageCallback;
import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.data.source.ImageDataSourceFactory;
import com.e.photounsplasht.utils.GlobalApplication;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ImageViewModel extends AndroidViewModel {
    private CheckPermission checkPermission;
    private FullImageCallback fullImageCallback;
    private ImageDataSourceFactory imageDataSourceFactory;
    private LiveData<PagedList<Image>> pagedListLiveData;
    private ImageAdapter imageAdapter;
    public ObservableField<Image> imageObservableField = new ObservableField<>();
    public ObservableBoolean isShowFullImage = new ObservableBoolean(false);
    public ObservableBoolean isSaveProcessImage = new ObservableBoolean(false);
    private ArrayList<String> inSaveProcessImages = new ArrayList<>();

    public ImageViewModel(@NonNull Application application) {
        super(application);
    }

    private void initPagedList() {
        imageDataSourceFactory = new ImageDataSourceFactory();
        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .setPageSize(20)
                .build();
        pagedListLiveData = (new LivePagedListBuilder<Long, Image>(imageDataSourceFactory, config))
                .setFetchExecutor(Executors.newFixedThreadPool(5))
                .build();
    }

    public LiveData<PagedList<Image>> getPagedListLiveData() {
        if (pagedListLiveData == null) {
            initPagedList();
        }
        return pagedListLiveData;
    }

    public ImageAdapter getImageAdapter() {
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(this);
        }
        return imageAdapter;
    }

    public void searchImage(String searchQuery) {
        if (imageDataSourceFactory != null && pagedListLiveData != null) {
            if (searchQuery.isEmpty() && imageDataSourceFactory.getSearchQuery().isEmpty()) {
                return;
            }

            imageDataSourceFactory.setSearchQuery(searchQuery);
            PagedList<Image> imagePagedList = pagedListLiveData.getValue();
            if (imagePagedList != null) {
                imagePagedList.getDataSource().invalidate();
            }
        }
    }

    public void setFullImage(Image image) {
        imageObservableField.set(image);
        if (image != null) {
            isSaveProcessImage.set(isInSaveProcessFullImage(image.getId()));
        }
        isShowFullImage.set(image != null);
        if (fullImageCallback != null) {
            fullImageCallback.openFullImage(image != null);
        }
    }

    public boolean isInSaveProcessFullImage(String imageId) {
        for (String inProcessId : inSaveProcessImages) {
            if (inProcessId.equals(imageId)) {
                return true;
            }
        }
        return false;
    }

    public void setIsSaveProcessImage(String imageId, boolean inProcess) {
        if (inProcess) {
            inSaveProcessImages.add(imageId);
        } else {
            inSaveProcessImages.remove(imageId);
        }
        if (imageObservableField.get() != null && imageObservableField.get().getId().equals(imageId)) {
            isSaveProcessImage.set(inProcess);
        }
    }

    public void saveCurrentImage() {
        Image toSave = imageObservableField.get();
        if (toSave != null && checkPermission != null && checkPermission.check()) {
            (new SaveImage(new SaveImageCallback() {
                @Override
                public void onStart(String imageId) {
                    setIsSaveProcessImage(imageId, true);
                }

                @Override
                public void onFinish(String imageId, boolean isSuccess) {
                    setIsSaveProcessImage(imageId, false);
                    if (isSuccess) {
                        Toast.makeText(GlobalApplication.getAppContext(), GlobalApplication.getAppContext().getString(R.string.savedToGallery), Toast.LENGTH_SHORT).show();
                    }
                }
            })).execute(toSave);
        }
    }

    public void setCheckPermission(CheckPermission checkPermission) {
        this.checkPermission = checkPermission;
    }

    public CheckPermission getCheckPermission() {
        return checkPermission;
    }

    public FullImageCallback getFullImageCallback() {
        return fullImageCallback;
    }

    public void setFullImageCallback(FullImageCallback fullImageCallback) {
        this.fullImageCallback = fullImageCallback;
    }

    public interface FullImageCallback {
        public void openFullImage(boolean isOpen);
    }
}
