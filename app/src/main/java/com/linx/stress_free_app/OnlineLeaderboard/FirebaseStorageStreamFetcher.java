package com.linx.stress_free_app.OnlineLeaderboard;
import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseStorageStreamFetcher implements DataFetcher<InputStream> {

    private StorageReference storageReference;
    private StreamDownloadTask.TaskSnapshot taskSnapshot;

    public FirebaseStorageStreamFetcher(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        StreamDownloadTask task = storageReference.getStream();
        task.addOnSuccessListener(snapshot -> {
            taskSnapshot = snapshot;
            callback.onDataReady(snapshot.getStream());
        }).addOnFailureListener(exception -> {
            callback.onLoadFailed(exception);
        });
    }

    @Override
    public void cleanup() {
        if (taskSnapshot != null && taskSnapshot.getStream() != null) {
            try {
                taskSnapshot.getStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancel() {
        // Cancel the task if needed
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
