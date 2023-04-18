package com.linx.stress_free_app.OnlineLeaderboard;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class FirebaseStorageLoader implements ModelLoader<StorageReference, InputStream> {

    private Context context;

    public FirebaseStorageLoader(Context context) {
        this.context = context;
    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull StorageReference storageReference, int width, int height, @NonNull Options options) {
        return new LoadData<>(new ObjectKey(storageReference), new FirebaseStorageStreamFetcher(storageReference));
    }

    @Override
    public boolean handles(@NonNull StorageReference storageReference) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<StorageReference, InputStream> {

        private Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ModelLoader<StorageReference, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
            return new FirebaseStorageLoader(context);
        }

        @Override
        public void teardown() {
            // No teardown needed
        }
    }
}
