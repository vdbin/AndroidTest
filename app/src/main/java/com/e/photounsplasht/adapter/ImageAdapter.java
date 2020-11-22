package com.e.photounsplasht.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.e.photounsplasht.R;
import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.databinding.AdapterImageBinding;
import com.e.photounsplasht.view.model.ImageViewModel;

public class ImageAdapter extends PagedListAdapter<Image, ImageAdapter.PhotoViewHolder> {

    ImageViewModel imageViewModel;

    public ImageAdapter(ImageViewModel imageViewModel) {
        super(Image.CALLBACK);
        this.imageViewModel = imageViewModel;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        AdapterImageBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_image, viewGroup, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder photoViewHolder, int position) {
        photoViewHolder.bind(getItem(position));
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        AdapterImageBinding binding;

        public PhotoViewHolder(@NonNull AdapterImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Image image) {
            binding.setImage(image);
            binding.setImageViewModel(imageViewModel);
            binding.executePendingBindings();
        }
    }
}