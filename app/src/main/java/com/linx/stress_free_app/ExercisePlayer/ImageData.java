package com.linx.stress_free_app.ExercisePlayer;

public class ImageData {
    private String name;
    private String imageUrl;
    private String thumbnailUrl;
    private String audioUrl; // Add this field for the audio URL

    public ImageData(String name, String imageUrl, String thumbnailUrl, String audioUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.audioUrl = audioUrl; // Set the audio URL
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAudioUrl() { // Add this getter for the audio URL
        return audioUrl;
    }
}

