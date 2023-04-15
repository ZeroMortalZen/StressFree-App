package com.linx.stress_free_app.OnlineLeaderboard;

public class User {
    private String email;
    private String profilePicPath;
    private String medalRank;

    public User() {
    }

    public User(String email, String profilePicPath, String medalRank) {
        this.email = email;
        this.profilePicPath = profilePicPath;
        this.medalRank = medalRank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getMedalRank() {
        return medalRank;
    }

    public void setMedalRank(String medalRank) {
        this.medalRank = medalRank;
    }
}
