package com.sri.eko.ViewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.sri.eko.Models.PackageDetails;
import com.sri.eko.Repositories.GetPackagesRepo;

import java.util.List;
import java.util.Objects;

public class LoansViewModel extends ViewModel {

    private MutableLiveData<PackageDetails> apiResponse;

    public void init(Context mcontext) {
        if (apiResponse != null) return;
        GetPackagesRepo getPackagesRepoInstance = GetPackagesRepo.getInstance();
        apiResponse = getPackagesRepoInstance.getPackageResponse(mcontext);

    }

    public LiveData<List<PackageDetails.AmountMonths>> getPackaesList() {
        MutableLiveData<List<PackageDetails.AmountMonths>> apiResponselist = new MutableLiveData<>();
        apiResponselist.setValue(Objects.requireNonNull(apiResponse.getValue()).getData());
        return apiResponselist;
    }

}