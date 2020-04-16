package com.sri.eko.ViewModels;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class SplashActivityVM extends AndroidViewModel {

    private MutableLiveData<Boolean> istimeOut = new MutableLiveData<>();

    public SplashActivityVM(@NonNull Application application) {
        super(application);
    }



    public void timeDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2300);
    }

}
