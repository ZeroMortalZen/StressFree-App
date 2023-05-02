package com.linx.stress_free_app.StressSystem;

public class HelperClass {
    String email;
    boolean isFirstTime;
    String medalRank;
    String profilePicUrl;
    float pain;
    float time;
    long totalScreenTime;
    long totalAppUsage;
    float stressLevelScore;
    float mediationTime;
    int medLevel;
    float musicTime;
    int musicLevel;
    float exerciseTime;
    int exerciselevel;
    boolean hasStresslevel;

    // Constructor for setting default values
    public HelperClass(String email) {
        this.email = email;
        this.isFirstTime = true;
        this.medalRank = "no rank";
        this.profilePicUrl = "https://firebasestorage.googleapis.com/v0/b/stress-free-app-df840.appspot.com/o/profile_pictures%2FZmRjmQCBIpZbezCyCNmZLkYKQts2.jpg?alt=media&token=19aea6ad-667c-4f65-8d08-ff659327bd6c";
        this.stressLevelScore = 1;
        this.hasStresslevel = true;
    }

    // Getters and setters for email, isFirstTime, medalRank, and profilePicUrl
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsFirstTime() {
        return isFirstTime;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public String getMedalRank() {
        return medalRank;
    }

    public void setMedalRank(String medalRank) {
        this.medalRank = medalRank;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    // Existing getters and setters
    public float getPain() {
        return pain;
    }

    public void setPain(float pain) {
        this.pain = pain;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public long getTotalScreenTime() {
        return totalScreenTime;
    }

    public void setTotalScreenTime(long totalScreenTime) {
        this.totalScreenTime = totalScreenTime;
    }

    public long getTotalAppUsage() {
        return totalAppUsage;
    }

    public void setTotalAppUsage(long totalAppUsage) {
        this.totalAppUsage = totalAppUsage;
    }

    public float getStressLevelScore() {
        return stressLevelScore;
    }

    public void setStressLevelScore(float stressLevelScore) {
        this.stressLevelScore = stressLevelScore;
    }

    public float getMediationTime() {
        return mediationTime;
    }

    public void setMediationTime(float mediationTime) {
        this.mediationTime = mediationTime;
    }

    public int getMedLevel() {
        return medLevel;
    }

    public void setMedLevel(int medLevel) {
        this.medLevel = medLevel;
    }

    public float getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(float musicTime) {
        this.musicTime = musicTime;
    }

    public int getMusicLevel() {
        return musicLevel;
    }

    public void setMusicLevel(int musicLevel) {
        this.musicLevel = musicLevel;
    }

    public float getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(float exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public int getExerciselevel() {
        return exerciselevel;
    }

    public void setExerciselevel(int exerciselevel) {
        this.exerciselevel = exerciselevel;
    }

    public boolean isHasStresslevel() {
        return hasStresslevel;
    }

    public void setHasStresslevel(boolean hasStresslevel) {
        this.hasStresslevel = hasStresslevel;
    }

    public HelperClass() {
    }
}
