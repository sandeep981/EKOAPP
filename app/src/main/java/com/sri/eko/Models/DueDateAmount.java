
package com.sri.eko.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DueDateAmount {

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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
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
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("total_amount")
        @Expose
        private Integer totalAmount;
        @SerializedName("due_date")
        @Expose
        private String dueDate;
        @SerializedName("is_kyc_verified")
        @Expose
        private String isKycVerified;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Integer totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getIsKycVerified() {
            return isKycVerified;
        }

        public void setIsKycVerified(String isKycVerified) {
            this.isKycVerified = isKycVerified;
        }

    }

}
