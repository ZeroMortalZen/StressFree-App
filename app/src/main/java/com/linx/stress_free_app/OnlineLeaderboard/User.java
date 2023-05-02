package com.linx.stress_free_app.OnlineLeaderboard;

import com.google.firebase.storage.StorageReference;

public class User {
    private String email;

    private String medalRank;
    private String profilePicRef;
    private String profilePicUrl;
    private String userId;
    private double exerciseLevel;
    private double musicLevel;
    private double medLevel;

    public User() {
    }

    public User(String email, String profilePicRef, String medalRank , String profilePicUrl) {
        this.email = email;
        this.profilePicRef = profilePicRef;
        this.medalRank = medalRank;
        this.profilePicUrl =profilePicUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Add getter and setter methods
    public String getProfilePicRef() {
        return profilePicRef;
    }



    public String getMedalRank() {
        return medalRank;
    }

    public void setMedalRank(String medalRank) {
        this.medalRank = medalRank;
    }

    public void setProfilePicRef(String profilePicRef) {
        this.profilePicRef = profilePicRef;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public double getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(double exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }

    public double getMusicLevel() {
        return musicLevel;
    }

    public void setMusicLevel(double musicLevel) {
        this.musicLevel = musicLevel;
    }

    public double getMedLevel() {
        return medLevel;
    }

    public void setMedLevel(double medLevel) {
        this.medLevel = medLevel;
    }
}
