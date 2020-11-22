package com.e.photounsplasht.data.disk;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.e.photounsplasht.R;
import com.e.photounsplasht.data.model.Image;
import com.e.photounsplasht.utils.GlobalApplication;

import java.io.File;
import java.io.FileOutputStream;

public class SaveImage extends AsyncTask<Image, Void, Boolean> {

    private SaveImageCallback saveImageCallback;
    private Image toSave;

    public SaveImage(SaveImageCallback saveImageCallback) {
        this.saveImageCallback = saveImageCallback;
    }

    public SaveImage() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Image... images) {
        toSave = images[0];
        if (saveImageCallback != null) {
            saveImageCallback.onStart(toSave.getId());
        }
        try {
            String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GlobalApplication.getAppContext().getString(R.string.app_name) + "/";
            File file = new File(dir, toSave.getId() + ".jpg");

            if (!file.exists()) {
                File directory = new File(dir);
                directory.mkdirs();
                file = new File(dir, toSave.getId() + ".jpg");
            }

            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            Bitmap bitmap = GlobalApplication.getImageLoader().loadImageSync(toSave.getUrls().getFull());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.flush();
            ostream.close();

            MediaScannerConnection.scanFile(GlobalApplication.getAppContext(),
                    new String[]{file.toString()}, null, (path, uri) -> Log.d("TAG", "Add to gallery " + path));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isSaved) {
        super.onPostExecute(isSaved);
        if (saveImageCallback != null && toSave != null) {
            saveImageCallback.onFinish(toSave.getId(), isSaved);
        }
    }
}
