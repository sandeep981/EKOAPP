package com.sri.eko.Views.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sri.eko.Models.UserLoansHistory;
import com.sri.eko.R;


public class LoanPaymentsFragment extends Fragment {

    // TODO: Rename and change types of parameters
    private UserLoansHistory.LoansData model;
    View rootView;
    TextView txt_requested_date, txt_due_date, txt_loan_amount, txt_due_amount;
    EditText edt_amount;


    public LoanPaymentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (UserLoansHistory.LoansData) getArguments().getSerializable("model");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_loan_payments, container, false);

        txt_requested_date = rootView.findViewById(R.id.txt_requested_date);
        txt_due_date = rootView.findViewById(R.id.txt_due_date);
        txt_loan_amount = rootView.findViewById(R.id.txt_loan_amount);
        txt_due_amount = rootView.findViewById(R.id.txt_due_amount);
        edt_amount = rootView.findViewById(R.id.edt_amount);
        Button btn_submit = rootView.findViewById(R.id.btn_submit);

        txt_requested_date.setText(model.getRequestDate());
        txt_loan_amount.setText("" + model.getTotalAmount());
        txt_due_date.setText(model.getNextDueDate());
        txt_due_amount.setText("" + model.getRemainPayment());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_amount.getText().toString().length() > 0) {

                    double enteredAmount = Double.parseDouble(edt_amount.getText().toString());
                    double actualAmount = model.getRemainPayment();
                    if (enteredAmount > actualAmount) {
                        Toast.makeText(getActivity(), "Your are paying more than due amount", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Valid Amount", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private void loadPrimaryCategory() {
        RecyclerView rv_payments = rootView.findViewById(R.id.rv_payments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_payments.setLayoutManager(linearLayoutManager);
        rv_payments.setHasFixedSize(true);
        // loansHistoryAdp = new LoansHistoryAdp(getActivity(), primaryCategoriesList);
        // rv_payments.setAdapter(loansHistoryAdp);
    }

}
