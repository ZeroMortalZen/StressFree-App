package com.linx.stress_free_app.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.app.Application;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


import java.util.Map;

public class SharedViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> yogaProgress;
    private MutableLiveData<Integer> meditationProgress;

    public SharedViewModel(Application application) {
        super(application);
        yogaProgress = new MutableLiveData<>();
        meditationProgress = new MutableLiveData<>();
    }

    public LiveData<Integer> getYogaProgress() {
        return yogaProgress;
    }

    public void setYogaProgress(int progress) {
        yogaProgress.setValue(progress);
    }

    public LiveData<Integer> getMeditationProgress() {
        return meditationProgress;
    }

    public void setMeditationProgress(int progress) {
        meditationProgress.setValue(progress);

    }





}
