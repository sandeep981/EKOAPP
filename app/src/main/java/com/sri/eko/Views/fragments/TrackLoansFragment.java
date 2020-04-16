package com.sri.eko.Views.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sri.eko.Adapters.LoansHistoryAdp;
import com.sri.eko.Models.UserLoansHistory;
import com.sri.eko.R;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;

import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static com.sri.eko.Utilities.Constants.PREFS;
import static com.sri.eko.Utilities.Constants.USER_ID;

public class TrackLoansFragment extends Fragment {

    public TrackLoansFragment() {
        // Required empty public constructor
    }

    View rootView;
    RecyclerView rv_track_loans;
    private ArrayList<UserLoansHistory.LoansData> primaryCategoriesList = new ArrayList<>();
    private LoansHistoryAdp loansHistoryAdp;

    private SharedPreferences preferences;
    private String user_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_track_loans, container, false);

        preferences = getActivity().getSharedPreferences(PREFS, MODE_PRIVATE);
        user_ID = preferences.getString(USER_ID, null);

        fetchCategoriesRetrofit();

        return rootView;
    }


    private void fetchCategoriesRetrofit() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());
        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<UserLoansHistory> call = apiInterface.ApiGetUserLoansHistory(user_ID);
            call.enqueue(new Callback<UserLoansHistory>() {
                @Override
                public void onResponse(Call<UserLoansHistory> call, retrofit2.Response<UserLoansHistory> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        UserLoansHistory model = response.body();
                        String status = model.getErrCode();
                        if (status.equals("valid")) {
                            primaryCategoriesList.addAll(model.getData());
                            loadPrimaryCategory();
                        } else {
                            alertMessages.alertMsgBox("No Items Found", getActivity());
                        }
                    } else {
                        alertMessages.alertMsgBox("Server Not Responding", getActivity());
                    }
                }

                @Override
                public void onFailure(Call<UserLoansHistory> call, Throwable error) {
                    alertMessages.hideProgressDialog();
                    if (error instanceof UnknownHostException) {
                        Toast.makeText(getActivity(), "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }

    }

    private void loadPrimaryCategory() {
        rv_track_loans = rootView.findViewById(R.id.rv_track_loans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_track_loans.setLayoutManager(linearLayoutManager);
        rv_track_loans.setHasFixedSize(true);
        loansHistoryAdp = new LoansHistoryAdp(getActivity(), primaryCategoriesList);
        rv_track_loans.setAdapter(loansHistoryAdp);
    }

}
