package com.linx.stress_free_app.StressSystem;

public class PersonStressHelperClass {

    float pain;
    float time;

    public float getPain(){return pain;}
    public void setPain(float pain){this.pain= pain ;}

    public float getTime(){ return time;}
    public void setTime(float time){this.time=time ;}

    public PersonStressHelperClass(float pain, float time){
        this.pain= pain;
        this.time = time;
    }

    public PersonStressHelperClass(){

    }

}
