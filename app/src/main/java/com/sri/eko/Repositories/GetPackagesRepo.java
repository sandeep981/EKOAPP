package com.sri.eko.Repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.sri.eko.Models.PackageDetails;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;

public class GetPackagesRepo {

    String TAG = getClass().getSimpleName();
    private static GetPackagesRepo getPackagesRepo;

    public static GetPackagesRepo getInstance() {
        if (getPackagesRepo == null) {
            getPackagesRepo = new GetPackagesRepo();
        }
        return getPackagesRepo;
    }


    public MutableLiveData<PackageDetails> getPackageResponse(final Context mcontext) {

        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(mcontext);

        final MutableLiveData<PackageDetails> apiResponse = new MutableLiveData<>();
        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<PackageDetails> call = apiInterface.ApiLoanPackages();
            call.enqueue(new Callback<PackageDetails>() {
                @Override
                public void onResponse(Call<PackageDetails> call, retrofit2.Response<PackageDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        apiResponse.setValue(response.body());
                    } else {
                        Toast.makeText(mcontext, "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PackageDetails> call, Throwable error) {
                    alertMessages.hideProgressDialog();
                    apiResponse.setValue(null);
                    if (error instanceof UnknownHostException) {
                        Toast.makeText(mcontext, "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mcontext, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
        return apiResponse;
    }

}
