package com.sri.eko.Views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sri.eko.Models.BankDetails;
import com.sri.eko.Models.UserDetails;
import com.sri.eko.R;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.sri.eko.Utilities.Constants.PREFS;
import static com.sri.eko.Utilities.Constants.USER_ID;


public class BankDetailsFragment extends Fragment {

    TextInputEditText upi_addess, user_name, bank_name, ifsc_code, account_num;
    TextInputLayout til_upi_addess, til_username, til_bankname, til_ifsc_code, til_account;
    LinearLayout linear_upi, linear_bank;
    String payTo_str = "upi";
    String loanRequestId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanRequestId = getArguments().getString("loanId", "");
        }

    }

    View rootView;
    private SharedPreferences preferences;
    private String user_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_bank_details, container, false);

        preferences = getActivity().getSharedPreferences(PREFS, MODE_PRIVATE);
        user_ID = preferences.getString(USER_ID, null);

        til_upi_addess = rootView.findViewById(R.id.til_upi_addess);
        til_username = rootView.findViewById(R.id.til_username);
        til_bankname = rootView.findViewById(R.id.til_bankname);
        til_ifsc_code = rootView.findViewById(R.id.til_ifsc_code);
        til_account = rootView.findViewById(R.id.til_account);

        upi_addess = rootView.findViewById(R.id.upi_addess);
        user_name = rootView.findViewById(R.id.user_name);
        bank_name = rootView.findViewById(R.id.bank_name);
        ifsc_code = rootView.findViewById(R.id.ifsc_code);
        account_num = rootView.findViewById(R.id.account_num);

        linear_upi = rootView.findViewById(R.id.linear_upi);
        linear_bank = rootView.findViewById(R.id.linear_bank);

        linear_upi.setVisibility(View.VISIBLE);
        linear_bank.setVisibility(View.GONE);

        RadioGroup rg_paytype = rootView.findViewById(R.id.rg_paytype);

        rg_paytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_upi) {
                    payTo_str = "upi";

                    linear_upi.setVisibility(View.VISIBLE);
                    linear_bank.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Selected UPI", Toast.LENGTH_SHORT).show();
                } else {
                    payTo_str = "bank";

                    linear_bank.setVisibility(View.VISIBLE);
                    linear_upi.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Selected BANK", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button btn_submit = rootView.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payTo_str.equals("upi")) {
                    if (String.valueOf(upi_addess.getText()).isEmpty()) {
                        til_upi_addess.setError("This Field is mandatory");
                    } else {
                        sendDataRetrofit();
                    }
                } else {
                    if (check()) {
                        sendDataRetrofit();
                    }
                }
            }
        });

        getBankDetails();

        return rootView;
    }


    private boolean check() throws NullPointerException {
        boolean status = true;

        if (String.valueOf(user_name.getText()).isEmpty()) {
            til_username.setError("This Field is mandatory");
            status = false;
        } else if (bank_name.getText().toString().isEmpty()) {
            til_bankname.setError("This Field is mandatory");
            status = false;
        } else if (ifsc_code.getText().toString().isEmpty()) {
            til_ifsc_code.setError("This Field is mandatory");
            status = false;
        } else if (account_num.getText().toString().isEmpty()) {
            til_account.setError("This Field is mandatory");
            status = false;
        }

        return status;
    }

    private void sendDataRetrofit() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        String name_str = String.valueOf(user_name.getText()).length() > 0 ? user_name.getText().toString() : "";
        String bank_str = bank_name.getText().toString().length() > 0 ? bank_name.getText().toString() : "";
        String ifsc_code_str = ifsc_code.getText().toString().length() > 0 ? ifsc_code.getText().toString() : "";
        String account_Num_str = account_num.getText().toString().length() > 0 ? account_num.getText().toString() : "";
        String upi_adress_str = upi_addess.getText().toString().length() > 0 ? upi_addess.getText().toString() : "";

        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<UserDetails> call = apiInterface.ApiBankDetails(user_ID, payTo_str, name_str, bank_str, ifsc_code_str, account_Num_str, upi_adress_str);
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        if (response.body().getErrCode().equals("valid")) {
                            withdrawRequest();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDetails> call, Throwable t) {
                    alertMessages.hideProgressDialog();
                }

            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
    }

    private void getBankDetails() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<BankDetails> call = apiInterface.ApiGetBankDetails(user_ID);
            call.enqueue(new Callback<BankDetails>() {
                @Override
                public void onResponse(Call<BankDetails> call, Response<BankDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getErrCode().equals("valid")) {
                            loadDataIntoView(response.body());
                        }
                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BankDetails> call, Throwable t) {
                    alertMessages.hideProgressDialog();
                }

            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
    }

    private void loadDataIntoView(BankDetails model) {

        user_name.setText(model.getData().getAccountantName());
        bank_name.setText(model.getData().getBankName());
        ifsc_code.setText(model.getData().getIfsc());
        account_num.setText(model.getData().getAccountNumber());
        upi_addess.setText(model.getData().getUpi());

    }


    private void withdrawRequest() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<BankDetails> call = apiInterface.ApiLoanWithDraw(user_ID, loanRequestId);
            call.enqueue(new Callback<BankDetails>() {
                @Override
                public void onResponse(Call<BankDetails> call, Response<BankDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        if (response.body().getErrCode().equals("valid")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_bankDetailsFragment_to_trackLoansFragment);
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BankDetails> call, Throwable t) {
                    alertMessages.hideProgressDialog();
                }

            });
        } catch (Exception ex) {
            alertMessages.hideProgressDialog();
            ex.printStackTrace();
        }
    }

}
