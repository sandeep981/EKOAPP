package com.sri.eko.Views.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.sri.eko.Adapters.MonthsSpnAdp;
import com.sri.eko.Adapters.SpinnerAdp;
import com.sri.eko.Models.DueDateAmount;
import com.sri.eko.Models.PackageDetails;
import com.sri.eko.Models.UserDetails;
import com.sri.eko.R;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.ViewModels.LoansViewModel;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;

import java.net.UnknownHostException;
import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
import static com.sri.eko.Utilities.Constants.PREFS;
import static com.sri.eko.Utilities.Constants.USER_ID;


public class LoansFragment extends Fragment {

    private LoansViewModel loansViewModel;
    private Spinner spn_amount, spn_months;
    private TextView txt_due_date, txt_due_amount;
    private Button btn_request;
    private ArrayList<PackageDetails.AmountMonths> packageslist = new ArrayList<>();
    private String TAG = getClass().getSimpleName();
    private String amountId = "", monthsId = "", isKYCVerified = "no",
            termsConditions = "", description = "", totalAMount = "", dueDate;


    private SharedPreferences preferences;
    private String user_ID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loans, container, false);

        preferences = getActivity().getSharedPreferences(PREFS, MODE_PRIVATE);
        user_ID = preferences.getString(USER_ID, null);

        spn_amount = root.findViewById(R.id.spn_amount);
        spn_months = root.findViewById(R.id.spn_months);
        txt_due_date = root.findViewById(R.id.txt_due_date);
        txt_due_amount = root.findViewById(R.id.txt_due_amount);
        final CheckBox ch_terms_conditions = root.findViewById(R.id.ch_terms_conditions);
        TextView txt_terms_conditions = root.findViewById(R.id.txt_terms_conditions);
        btn_request = root.findViewById(R.id.btn_request);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch_terms_conditions.isChecked()) {
                    if (isKYCVerified.equals("no")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("user_ID", user_ID);
                        bundle.putString("amountId", amountId);
                        bundle.putString("monthsId", monthsId);
                        bundle.putString("totalAMount", totalAMount);
                        bundle.putString("dueDate", dueDate);
                        Navigation.findNavController(v).navigate(R.id.action_nav_loans_to_uploadKycFragment, bundle);
                    } else {
                        requestLoan();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Accept Terms and Conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_terms_conditions.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<u>Terms &amp; Conditions</u>";
        txt_terms_conditions.setText(Html.fromHtml(text));
        txt_terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("terms", termsConditions);
                bundle.putString("description", description);
                Navigation.findNavController(view).navigate(R.id.action_nav_loans_to_termsConditionsFragment, bundle);
            }
        });

        getPackageResponse();

        Button btn_track = root.findViewById(R.id.btn_track);
        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_loans_to_trackLoansFragment);
            }
        });


/*      loansViewModel = ViewModelProviders.of(this).get(LoansViewModel.class);
        loansViewModel.init(getActivity());
        loansViewModel.getPackaesList().observe(getViewLifecycleOwner(), new Observer<List<PackageDetails.AmountMonths>>() {
            @Override
            public void onChanged(List<PackageDetails.AmountMonths> amountMonths) {
                packageslist.addAll(amountMonths);
                loadPackageSpinner();
            }
        });*/

        return root;
    }

    private void getPackageResponse() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<PackageDetails> call = apiInterface.ApiLoanPackages();
            call.enqueue(new Callback<PackageDetails>() {
                @Override
                public void onResponse(Call<PackageDetails> call, retrofit2.Response<PackageDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        List<PackageDetails.AmountMonths> list = response.body().getData();
                        packageslist.addAll(list);
                        loadPackageSpinner();
                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PackageDetails> call, Throwable error) {
                    alertMessages.hideProgressDialog();
                    if (error instanceof UnknownHostException) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }

    }

    private void loadPackageSpinner() {
        SpinnerAdp spinnerArrayAdapter = new SpinnerAdp(getActivity(), R.layout.spinner_item, packageslist); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_amount.setAdapter(spinnerArrayAdapter);
        spn_amount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                amountId = packageslist.get(position).getId();
                ArrayList<PackageDetails.Months> monthslist = new ArrayList<>(packageslist.get(position).getMonths());
                loadMonthsSpinner(monthslist);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadMonthsSpinner(final ArrayList<PackageDetails.Months> monthslist) {
        MonthsSpnAdp spinnerArrayAdapter = new MonthsSpnAdp(getActivity(), R.layout.spinner_item, monthslist); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_months.setAdapter(spinnerArrayAdapter);
        spn_months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monthsId = String.valueOf(monthslist.get(position).getId());
                getDueDateAmount();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDueDateAmount() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<DueDateAmount> call = apiInterface.ApiDeuDateAmount(user_ID, amountId, monthsId);
            call.enqueue(new Callback<DueDateAmount>() {
                @Override
                public void onResponse(Call<DueDateAmount> call, retrofit2.Response<DueDateAmount> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        isKYCVerified = response.body().getData().getIsKycVerified();
                        termsConditions = response.body().getData().getTermsAndConditions();
                        description = response.body().getData().getDescription();
                        totalAMount = String.valueOf(response.body().getData().getTotalAmount());
                        dueDate = response.body().getData().getDueDate();

                        String amount_str = "\u20B9 " + response.body().getData().getTotalAmount();
                        String date_str = response.body().getData().getDueDate();

                        txt_due_date.setText(date_str);
                        txt_due_amount.setText(amount_str);

                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<DueDateAmount> call, Throwable error) {
                    alertMessages.hideProgressDialog();
                    if (error instanceof UnknownHostException) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
    }

    private void requestLoan() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());
        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<UserDetails> call = apiInterface.ApiRequestLoan(user_ID, amountId, monthsId, totalAMount
                    , dueDate, "", "", "", "", "");
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, retrofit2.Response<UserDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        if (response.body().getErrCode().equals("valid")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_nav_loans_to_nav_home);
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable error) {
                    alertMessages.hideProgressDialog();
                    if (error instanceof UnknownHostException) {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Network connection error! Check your internet Setting", Toast.LENGTH_LONG).show();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
    }
}