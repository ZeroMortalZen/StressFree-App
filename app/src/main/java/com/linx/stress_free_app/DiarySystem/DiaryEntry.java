package com.linx.stress_free_app.DiarySystem;

public class DiaryEntry {
    public String title;
    public String content;
    public String date;

    public DiaryEntry() {
        // Default constructor required for calls to DataSnapshot.getValue(DiaryEntry.class)
    }

    public DiaryEntry(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    //getters and setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
