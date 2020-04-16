
package com.sri.eko.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoansHistory {

    @SerializedName("err_code")
    @Expose
    private String errCode;
    @SerializedName("error_type")
    @Expose
    private String errorType;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<LoansData> data = null;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LoansData> getData() {
        return data;
    }

    public void setData(List<LoansData> data) {
        this.data = data;
    }

    public class LoansData implements Serializable {
        @SerializedName("remarks")
        @Expose
        private String remarks;
        @SerializedName("is_withdraw_request_placed")
        @Expose
        private String isWithdrawRequestPlaced;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("is_completed")
        @Expose
        private String isCompleted;
        @SerializedName("request_status")
        @Expose
        private String requestStatus;
        @SerializedName("request_date")
        @Expose
        private String requestDate;
        @SerializedName("is_amount_withdraw")
        @Expose
        private String isAmountWithdraw;
        @SerializedName("loan_id")
        @Expose
        private String loanId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("interest_percentage")
        @Expose
        private String interestPercentage;
        @SerializedName("processing_fee")
        @Expose
        private String processingFee;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("terms_and_conditions")
        @Expose
        private String termsAndConditions;
        @SerializedName("total_amount")
        @Expose
        private Integer totalAmount;

        @SerializedName("remain_payment")
        @Expose
        private Integer remainPayment;

        @SerializedName("next_due_date")
        @Expose
        private String nextDueDate;

        @SerializedName("payment_list")
        @Expose
        private ArrayList<PaymentList> paymentList = null;

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getIsWithdrawRequestPlaced() {
            return isWithdrawRequestPlaced;
        }

        public void setIsWithdrawRequestPlaced(String isWithdrawRequestPlaced) {
            this.isWithdrawRequestPlaced = isWithdrawRequestPlaced;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsCompleted() {
            return isCompleted;
        }

        public void setIsCompleted(String isCompleted) {
            this.isCompleted = isCompleted;
        }

        public String getRequestStatus() {
            return requestStatus;
        }

        public void setRequestStatus(String requestStatus) {
            this.requestStatus = requestStatus;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public String getIsAmountWithdraw() {
            return isAmountWithdraw;
        }

        public void setIsAmountWithdraw(String isAmountWithdraw) {
            this.isAmountWithdraw = isAmountWithdraw;
        }

        public String getLoanId() {
            return loanId;
        }

        public void setLoanId(String loanId) {
            this.loanId = loanId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getInterestPercentage() {
            return interestPercentage;
        }

        public void setInterestPercentage(String interestPercentage) {
            this.interestPercentage = interestPercentage;
        }

        public String getProcessingFee() {
            return processingFee;
        }

        public void setProcessingFee(String processingFee) {
            this.processingFee = processingFee;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTermsAndConditions() {
            return termsAndConditions;
        }

        public void setTermsAndConditions(String termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
        }

        public Integer getTotalAmount() {
            return totalAmount;
        }

        public void setRemainPayment(Integer remainPayment) {
            this.remainPayment = remainPayment;
        }

        public Integer getRemainPayment() {
            return remainPayment;
        }

        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getNextDueDate() {
            return nextDueDate;
        }

        public void setNextDueDate(String nextDueDate) {
            this.nextDueDate = nextDueDate;
        }

        public ArrayList<PaymentList> getPaymentList() {
            return paymentList;
        }

        public void setPaymentList(ArrayList<PaymentList> paymentList) {
            this.paymentList = paymentList;
        }
    }


    public class PaymentList {

        @SerializedName("payment_ref_id")
        @Expose
        private String paymentRefId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getPaymentRefId() {
            return paymentRefId;
        }

        public void setPaymentRefId(String paymentRefId) {
            this.paymentRefId = paymentRefId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
