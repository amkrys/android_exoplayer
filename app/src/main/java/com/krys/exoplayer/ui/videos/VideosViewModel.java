package com.krys.exoplayer.ui.videos;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.krys.exoplayer.model.ModelVideo;

import java.util.ArrayList;
import java.util.Locale;

public class VideosViewModel extends ViewModel {

    private MutableLiveData<ArrayList<ModelVideo>> videosList;
    private ArrayList<ModelVideo> arrayList = new ArrayList<ModelVideo>();

    public MutableLiveData<ArrayList<ModelVideo>> getVideosList(FragmentActivity activity) {
        return loadVideos(activity);
    }

    public VideosViewModel() {
        videosList = new MutableLiveData<>();
    }

    private MutableLiveData<ArrayList<ModelVideo>> loadVideos(FragmentActivity activity) {
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION};
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

        Cursor cursor = activity.getApplication().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);
        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleColumn);
                long id = cursor.getLong(idColumn);
                int duration = cursor.getInt(durationColumn);

                Uri data = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                String duration_formatted;
                int sec = (duration / 1000) % 60;
                int min = (duration / (1000 * 60)) % 60;
                int hrs = duration / (1000 * 60 * 60);

                if (hrs == 0) {
                    duration_formatted = String.valueOf(min).concat(":".concat(String.format(Locale.UK, "%02d", sec)));
                } else {
                    duration_formatted = String.valueOf(hrs).concat(":".concat(String.format(Locale.UK, "%02d", min).concat(":".concat(String.format(Locale.UK, "%02d", sec)))));
                }
                arrayList.add(new ModelVideo(id, data, title, duration_formatted));
                videosList.setValue(arrayList);
            }
        }
        return videosList;
    }
}