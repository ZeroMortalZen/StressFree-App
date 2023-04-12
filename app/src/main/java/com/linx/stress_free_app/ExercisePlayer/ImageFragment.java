package com.linx.stress_free_app.ExercisePlayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.linx.stress_free_app.R;

public class ImageFragment extends Fragment {

    private static final String IMAGE_URL = "image_url";

    private String imageUrl;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(String imageUrl) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        // Create a new layout file `fragment_image.xml` for the ImageFragment
        ImageView imageView = view.findViewById(R.id.image_view);
        if (imageUrl != null) {
            // Use a library like Glide or Picasso to load the image from the URL
            // In this example, I'll use Glide
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        return view;
    }
}



