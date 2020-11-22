package com.e.photounsplasht.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.e.photounsplasht.R;
import com.e.photounsplasht.databinding.ActivityMainBinding;
import com.e.photounsplasht.utils.GlobalApplication;
import com.e.photounsplasht.view.model.ImageViewModel;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ImageViewModel imageViewModel;
    private SearchView searchView;
    private String savedSearchQuery;
    private static final String SEARCH_KEY = "search";
    private Handler handler;
    private Runnable runnable;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 312;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        binding.setImageViewModel(imageViewModel);
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);

        if (savedInstanceState != null) {
            savedSearchQuery = savedInstanceState.getString(SEARCH_KEY);
        }

        permissionInit();
        recyclerViewInit();
    }

    private void recyclerViewInit() {
        binding.recylerview.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recylerview.setAdapter(imageViewModel.getImageAdapter());
        imageViewModel.getPagedListLiveData().observe(this, image -> {
            imageViewModel.getImageAdapter().submitList(image);
        });
    }

    public void permissionInit() {
        imageViewModel.setCheckPermission(() -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                return false;
            } else {
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        if (savedSearchQuery != null && !savedSearchQuery.isEmpty()) {
            searchItem.expandActionView();
            searchView.setQuery(savedSearchQuery, true);
            searchView.clearFocus();
        }
        imageViewModel.setFullImageCallback(isOpen -> {
            if (isOpen && searchView != null) {
                searchView.clearFocus();
            }
        });
        handler = new Handler();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                imageViewModel.searchImage(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                runnable = () -> imageViewModel.searchImage(newText);
                handler.postDelayed(runnable, 1000);
                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                imageViewModel.searchImage("");
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchView != null) {
            savedSearchQuery = searchView.getQuery().toString();
            outState.putString(SEARCH_KEY, savedSearchQuery);
        }
    }

    @Override
    public void onBackPressed() {
        if (imageViewModel.isShowFullImage.get()) {
            imageViewModel.setFullImage(null);
        } else {
            super.onBackPressed();
        }
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageViewModel.saveCurrentImage();
            } else {
                Toast.makeText(this, GlobalApplication.getAppContext().getString(R.string.noStoragePermission), Toast.LENGTH_LONG).show();
            }
        }
    }
}