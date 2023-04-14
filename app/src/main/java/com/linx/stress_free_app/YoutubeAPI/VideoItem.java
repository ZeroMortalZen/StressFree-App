package com.linx.stress_free_app.YoutubeAPI;

public class VideoItem {
    private String id;
    private String title;
    private String thumbnailUrl;

    public VideoItem(String id, String title, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
    }

    // Add getters and setters for the fields
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getThumbnailUrl() { return thumbnailUrl; }
}
