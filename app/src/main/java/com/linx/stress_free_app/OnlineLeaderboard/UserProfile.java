package com.linx.stress_free_app.OnlineLeaderboard;

public class UserProfile {
    private String profileImageUrl;
    private String email;
    private String rank;

    public UserProfile(String profileImageUrl, String email, String rank) {
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.rank = rank;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public String getRank() {
        return rank;
    }
}
