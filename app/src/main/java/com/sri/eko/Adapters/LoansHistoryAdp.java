package com.sri.eko.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sri.eko.Models.UserLoansHistory;
import com.sri.eko.R;

import java.util.ArrayList;


public class LoansHistoryAdp extends RecyclerView.Adapter<LoansHistoryAdp.ViewHolder> {
    private ArrayList<UserLoansHistory.LoansData> primaryCategoriesList;
    private Context mcontext;

    public LoansHistoryAdp(Context mcontext, ArrayList<UserLoansHistory.LoansData> primaryCategoriesList) {
        this.mcontext = mcontext;
        this.primaryCategoriesList = primaryCategoriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_history, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserLoansHistory.LoansData model = primaryCategoriesList.get(position);
        holder.txt_requested_date.setText(model.getRequestDate());
        holder.txt_due_date.setText(model.getNextDueDate());
        holder.txt_loan_amount.setText("" + model.getTotalAmount());
        holder.txt_status.setText(model.getRequestStatus());

        holder.linear_remark.setVisibility(View.GONE);


        if (model.getRequestStatus().equals("rejected")) {
            holder.btn_withdraw.setVisibility(View.GONE);
            holder.btn_upload_kyc.setVisibility(View.VISIBLE);
            holder.linear_remark.setVisibility(View.VISIBLE);
            holder.txt_remark.setText(model.getRemarks());
        } else if (model.getRequestStatus().equals("approved")) {
            holder.btn_upload_kyc.setVisibility(View.GONE);
            holder.btn_withdraw.setVisibility(View.VISIBLE);
        }

        if (model.getIsWithdrawRequestPlaced().equals("yes")) {
            holder.btn_withdraw.setVisibility(View.GONE);
            holder.linear_remark.setVisibility(View.VISIBLE);
            holder.txt_remark.setText("Withdraw request is Under process");
        }

        if (model.getIsAmountWithdraw().equals("yes")) {
            holder.btn_withdraw.setVisibility(View.GONE);
            holder.btn_upload_kyc.setVisibility(View.GONE);
            holder.btn_payment.setVisibility(View.VISIBLE);
        }

        if (model.getIsCompleted().equals("yes")) {
            holder.btn_withdraw.setVisibility(View.GONE);
            holder.btn_upload_kyc.setVisibility(View.GONE);
            holder.btn_payment.setVisibility(View.GONE);
        }

        holder.btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("loanId", model.getId());
                Navigation.findNavController(v).navigate(R.id.action_trackLoansFragment_to_bankDetailsFragment, bundle);
            }
        });

        holder.btn_upload_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        holder.btn_payment.setVisibility(View.VISIBLE);

        holder.btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", model);
                Navigation.findNavController(v).navigate(R.id.action_trackLoansFragment_to_loanPaymentsFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return primaryCategoriesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_requested_date, txt_due_date, txt_loan_amount, txt_status, txt_remark;
        Button btn_withdraw, btn_upload_kyc, btn_payment;
        LinearLayout linear_remark;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_requested_date = itemView.findViewById(R.id.txt_requested_date);
            txt_due_date = itemView.findViewById(R.id.txt_due_date);
            txt_loan_amount = itemView.findViewById(R.id.txt_loan_amount);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_remark = itemView.findViewById(R.id.txt_remark);
            linear_remark = itemView.findViewById(R.id.linear_remark);
            btn_withdraw = itemView.findViewById(R.id.btn_withdraw);
            btn_upload_kyc = itemView.findViewById(R.id.btn_upload_kyc);
            btn_payment = itemView.findViewById(R.id.btn_payment);
        }
    }
}
