package com.linx.stress_free_app.StressSystem;

public class HelperClass {



    float pain;
    float time;
    long  totalScreenTime;
    long  totalAppUsage;
    float stressLevelScore;
    float MediationTime;
    int MedLevel;
    float MusicTime;
    int MusicLevel;
    float exerciseTime;
    int exerciselevel;
    boolean hasStresslevel ;

    public HelperClass(float pain, float time, long totalScreenTime, long totalAppUsage, float stressLevelScore, float mediationTime, float mediationTime1, int medLevel, float musicTime, int musicLevel, float exerciseTime, int exerciselevel, boolean hasStresslevel) {
        this.pain = pain;
        this.time = time;
        this.totalScreenTime = totalScreenTime;
        this.totalAppUsage = totalAppUsage;
        this.stressLevelScore = stressLevelScore;
        MediationTime = mediationTime;
        MedLevel = medLevel;
        MusicTime = musicTime;
        MusicLevel = musicLevel;
        this.exerciseTime = exerciseTime;
        this.exerciselevel = exerciselevel;
        this.hasStresslevel = hasStresslevel;

    }

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
        return MediationTime;
    }

    public void setMediationTime(float mediationTime) {
        MediationTime = mediationTime;
    }

    public int getMedLevel() {
        return MedLevel;
    }

    public void setMedLevel(int medLevel) {
        MedLevel = medLevel;
    }

    public float getMusicTime() {
        return MusicTime;
    }

    public void setMusicTime(float musicTime) {
        MusicTime = musicTime;
    }

    public int getMusicLevel() {
        return MusicLevel;
    }

    public void setMusicLevel(int musicLevel) {
        MusicLevel = musicLevel;
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