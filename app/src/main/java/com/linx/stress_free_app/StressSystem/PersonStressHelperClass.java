package com.linx.stress_free_app.StressSystem;

public class PersonStressHelperClass {

    float pain;
    float time;
    long  totalScreenTime;
    long  totalAppUsage;


    public float getPain(){return pain;}
    public void setPain(float pain){this.pain= pain ;}

    public float getTime(){ return time;}
    public void setTime(float time){this.time=time ;}

    public long getTotalScreenTime(){return totalScreenTime;}
    public void setTotalScreenTime(long totalScreenTime){this.totalScreenTime=totalScreenTime ;}

    public long getTotalAppUsage(){return totalAppUsage;}
    public void setTotalAppUsage(long totalAppUsage){this.totalAppUsage=totalAppUsage ;}


    public PersonStressHelperClass(float pain, float time, long totalScreenTime,long totalAppUsage){
        this.pain= pain;
        this.time = time;
        this.totalScreenTime= totalScreenTime;
        this.totalAppUsage=totalAppUsage;
    }

    public PersonStressHelperClass(){

    }

}
