package com.sri.eko.app;

import com.sri.eko.Models.BankDetails;
import com.sri.eko.Models.DueDateAmount;
import com.sri.eko.Models.UploadImage;
import com.sri.eko.Models.UserDetails;
import com.sri.eko.Models.PackageDetails;
import com.sri.eko.Models.UserLoansHistory;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.sri.eko.Utilities.Constants.FORGET_PASSWORD;
import static com.sri.eko.Utilities.Constants.GET_BANK_DETAILS;
import static com.sri.eko.Utilities.Constants.GET_LOAN_DUE_DATE_AMOUNT;
import static com.sri.eko.Utilities.Constants.GET_LOAN_PACKAGES;
import static com.sri.eko.Utilities.Constants.GET_USER_LOANS;
import static com.sri.eko.Utilities.Constants.LOAN_REQUEST;
import static com.sri.eko.Utilities.Constants.LOAN_WITHDRAW_AMOUNT;
import static com.sri.eko.Utilities.Constants.LOGIN;
import static com.sri.eko.Utilities.Constants.OTP_VERIFY;
import static com.sri.eko.Utilities.Constants.SIGNUP;
import static com.sri.eko.Utilities.Constants.UPDATE_BANK_DETAIS;
import static com.sri.eko.Utilities.Constants.UPLOAD_IMAGE;

public interface ApIInterface {
    @FormUrlEncoded
    @POST(SIGNUP)
    Call<UserDetails> ApiSignUp(
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("email") String email,
            @Field("address") String address,
            @Field("password") String passsord);


    @FormUrlEncoded
    @POST(OTP_VERIFY)
    Call<UserDetails> ApiVerifyOTP(
            @Field("user_id") String mobile,
            @Field("otp") String otp);


    @FormUrlEncoded
    @POST(LOGIN)
    Call<UserDetails> ApiLogin(
            @Field("username") String name,
            @Field("password") String passsord);

    @FormUrlEncoded
    @POST(FORGET_PASSWORD)
    Call<UserDetails> ApiForgetPassword(
            @Field("mobile") String mobile);


    @GET(GET_LOAN_PACKAGES)
    Call<PackageDetails> ApiLoanPackages();

    @FormUrlEncoded
    @POST(GET_LOAN_DUE_DATE_AMOUNT)
    Call<DueDateAmount> ApiDeuDateAmount(
            @Field("user_id") String userID,
            @Field("loan_id") String loan_id,
            @Field("months_id") String months_id);

    @FormUrlEncoded
    @POST(LOAN_REQUEST)
    Call<UserDetails> ApiRequestLoan(
            @Field("user_id") String userID,
            @Field("loan_id") String loan_id,
            @Field("months_id") String months_id,
            @Field("amount_to_paid") String amount_to_paid,
            @Field("due_date") String due_date,
            @Field("aadharcard_front") String aadharcard_front,
            @Field("aadharcard_back") String aadharcard_back,
            @Field("pancard") String pancard,
            @Field("student_information") String personal_information,
            @Field("selfie") String selfie);

    @Multipart
    @POST(UPLOAD_IMAGE)
    Call<UploadImage> ApiUploadImage(
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userID);

    @FormUrlEncoded
    @POST(GET_USER_LOANS)
    Call<UserLoansHistory> ApiGetUserLoansHistory(
            @Field("user_id") String userID);

    @FormUrlEncoded
    @POST(GET_BANK_DETAILS)
    Call<BankDetails> ApiGetBankDetails(
            @Field("user_id") String userID);

    @FormUrlEncoded
    @POST(LOAN_WITHDRAW_AMOUNT)
    Call<BankDetails> ApiLoanWithDraw(
            @Field("user_id") String userID,
            @Field("loan_request_id") String loanRequestId);

    @FormUrlEncoded
    @POST(UPDATE_BANK_DETAIS)
    Call<UserDetails> ApiBankDetails(
            @Field("user_id") String user_id,
            @Field("pay_to") String payTo,
            @Field("accountant_name") String accountant_name,
            @Field("bank_name") String name,
            @Field("ifsc") String ifsc,
            @Field("account_number") String account_number,
            @Field("upi") String upi);

}
