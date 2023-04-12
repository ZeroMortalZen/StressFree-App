package com.linx.stress_free_app.ExercisePlayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linx.stress_free_app.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ImageFragment extends Fragment {

    private static final String IMAGE_URL = "image_url";

    private String imageUrl;
    private MediaPlayer mediaPlayerLocal;
    private MediaPlayer mediaPlayerRemote;
    private Handler handler;
    private long elapsedTime;
    private long targetTime;
    private ProgressBar progressBar;
    private Runnable updateElapsedTime;
    private static final String AUDIO_URL = "audio_url";

    private String audioUrl;


    //Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String userId = currentUser.getUid();
    DatabaseReference userRef = database.getReference("users").child(userId);

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String imageUrl, String audioUrl) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);
        args.putString(AUDIO_URL, audioUrl);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
            audioUrl = getArguments().getString(AUDIO_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        // Create a new layout file `fragment_image.xml` for the ImageFragment
        ImageView imageView = view.findViewById(R.id.image_view);
        ProgressBar progressBar = view.findViewById(R.id.progressBar4);
        if (imageUrl != null) {
            // Use a library like Glide or Picasso to load the image from the URL
            // In this example, I'll use Glide
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        mediaPlayerLocal = MediaPlayer.create(getActivity(), R.raw.afternoonbreathing);
        handler = new Handler();
        startPlaying();
        elapsedTime = 0;
        targetTime = TimeUnit.SECONDS.toMillis(40); // Set the target time, e.g., 10 seconds

        // Set the MediaPlayer to loop the audio clip
        mediaPlayerLocal.setLooping(true);

        return view;
    }


    private void startPlaying() {
        if (audioUrl != null) {
            mediaPlayerRemote = new MediaPlayer();
            try {
                mediaPlayerRemote.setDataSource(audioUrl);
                mediaPlayerRemote.prepareAsync();
                mediaPlayerRemote.setOnPreparedListener(mp -> mp.start());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mediaPlayerLocal != null) {
                mediaPlayerLocal.start();
            }
        }
        updateElapsedTime = new Runnable() {
            @Override
            public void run() {
                elapsedTime += 1000;

                if (elapsedTime >= targetTime) {



                    // Store data in Firebase when the target time is reached
                    storeDataInFirebase(elapsedTime);

                    // Reset the elapsedTime after storing the data
                    elapsedTime = 0;
                    //mediaPlayerLocal.pause();
                } else {
                    // Store data in Firebase periodically (e.g., every 5 seconds)
                    if (elapsedTime % 5000 == 0) {
                        storeDataInFirebase(elapsedTime);
                    }
                    //progressBar.setProgress((int) elapsedTime);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.post(updateElapsedTime);
    }

    private void stopPlaying() {
        if (mediaPlayerLocal != null) {
            mediaPlayerLocal.stop();
        }
        if (mediaPlayerRemote != null) {
            mediaPlayerRemote.stop();
        }
        handler.removeCallbacks(updateElapsedTime);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayerLocal != null) {
            mediaPlayerLocal.release();
            mediaPlayerLocal = null;
        }
        if (mediaPlayerRemote != null) {
            mediaPlayerRemote.release();
            mediaPlayerRemote = null;
        }
    }


    private void storeDataInFirebase(long elapsedTime) {
        userRef.child("exerciseTime").setValue(elapsedTime);
    }
}



