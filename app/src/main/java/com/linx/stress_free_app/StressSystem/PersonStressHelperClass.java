package com.linx.stress_free_app.StressSystem;

public class PersonStressHelperClass {

    float pain;
    float time;
    long  totalScreenTime;

    public float getPain(){return pain;}
    public void setPain(float pain){this.pain= pain ;}

    public float getTime(){ return time;}
    public void setTime(float time){this.time=time ;}

    public long getTotalScreenTime(){return totalScreenTime;}
    public void setTotalScreenTime(long totalScreenTime){this.totalScreenTime=totalScreenTime ;}


    public PersonStressHelperClass(float pain, float time, long totalScreenTime){
        this.pain= pain;
        this.time = time;
        this.totalScreenTime= totalScreenTime;
    }

    public PersonStressHelperClass(){

    }

}
