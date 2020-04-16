
package com.sri.eko.Models;

import androidx.lifecycle.LiveData;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageDetails {
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
    private List<AmountMonths> data = null;

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

    public List<AmountMonths> getData() {
        return data;
    }

    public void setData(List<AmountMonths> data) {
        this.data = data;
    }


    public class AmountMonths {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("months")
        @Expose
        private List<Months> months = null;



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

        public List<Months> getMonths() {
            return months;
        }

        public void setMonths(List<Months> months) {
            this.months = months;
        }

    }


    public class Months {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
