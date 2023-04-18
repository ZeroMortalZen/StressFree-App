package com.linx.stress_free_app.OnlineLeaderboard;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

@GlideModule
public final class FirebaseGlideModule extends AppGlideModule {


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(StorageReference.class, InputStream.class, new FirebaseStorageLoader.Factory(context));
    }



}

