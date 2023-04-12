package com.linx.stress_free_app.ExercisePlayer;

public class ImageData {
    private String name;
    private String imageUrl;
    private String thumbnailUrl;

    public ImageData(String name, String imageUrl, String thumbnailUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
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
}

