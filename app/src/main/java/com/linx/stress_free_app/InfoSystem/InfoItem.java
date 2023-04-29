package com.linx.stress_free_app.InfoSystem;

public class InfoItem {
    private int icon;
    private String title;
    private String description;
    private String url;

    public InfoItem(int icon, String title, String description, String url) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public int getIcon() { return icon; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
}
