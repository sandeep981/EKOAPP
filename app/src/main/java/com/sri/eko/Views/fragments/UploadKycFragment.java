package com.sri.eko.Views.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sri.eko.Models.UploadImage;
import com.sri.eko.Models.UserDetails;
import com.sri.eko.R;
import com.sri.eko.Utilities.AlertMessages;
import com.sri.eko.Utilities.FilePath;
import com.sri.eko.app.ApIInterface;
import com.sri.eko.app.ApiClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;


public class UploadKycFragment extends Fragment {

    private String amountId, totalAMount, userId, monthsId, dueDate;
    LinearLayout linear_aadhar_front, linear_aadhar_back, linear_pancard, linear_personal_info, linear_selfie;
    ImageView img_aadhar_front, img_aadhar_back, img_pancard, img_peronal_info, img_selfie;
    View rootView;
    String imageType = "";
    String TAG = getClass().getSimpleName();

    private String aadharFront_str, aadharBack_str, panCard_str, personalInfo_str, selfie_str;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("user_ID");
            amountId = getArguments().getString("amountId");
            monthsId = getArguments().getString("monthsId");
            totalAMount = getArguments().getString("totalAMount");
            dueDate = getArguments().getString("dueDate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload_kyc, container, false);

        linear_aadhar_front = rootView.findViewById(R.id.linear_aadhar_front);
        linear_aadhar_back = rootView.findViewById(R.id.linear_aadhar_back);
        linear_pancard = rootView.findViewById(R.id.linear_pancard);
        linear_personal_info = rootView.findViewById(R.id.linear_personal_info);
        linear_selfie = rootView.findViewById(R.id.linear_selfie);

        img_aadhar_front = rootView.findViewById(R.id.img_aadhar_front);
        img_aadhar_back = rootView.findViewById(R.id.img_aadhar_back);
        img_pancard = rootView.findViewById(R.id.img_pancard);
        img_peronal_info = rootView.findViewById(R.id.img_peronal_info);
        img_selfie = rootView.findViewById(R.id.img_selfie);

        linear_aadhar_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "img_aadhar_front";
                selectImage();
            }
        });
        linear_aadhar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "img_aadhar_back";
                selectImage();
            }
        });
        linear_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "img_pancard";
                selectImage();
            }
        });
        linear_personal_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "img_peronal_info";
                selectImage();
            }
        });
        linear_selfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageType = "img_selfie";
                selectImage();
            }
        });

        Button btn_submit = rootView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aadharFront_str == null) {
                    Toast.makeText(getActivity(), "Capture Aadhar Card Front", Toast.LENGTH_SHORT).show();
                    return;
                } else if (aadharBack_str == null) {
                    Toast.makeText(getActivity(), "Capture Aadhar Card Back", Toast.LENGTH_SHORT).show();
                    return;
                } else if (panCard_str == null) {
                    Toast.makeText(getActivity(), "Capture Pancard", Toast.LENGTH_SHORT).show();
                    return;
                } else if (personalInfo_str == null) {
                    Toast.makeText(getActivity(), "Capture Student ID Card", Toast.LENGTH_SHORT).show();
                    return;
                }else if (selfie_str == null) {
                    Toast.makeText(getActivity(), "Capture Selfie", Toast.LENGTH_SHORT).show();
                    return;
                }
                requestLoan();

            }
        });

        return rootView;
    }


    private void selectImage() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getActivity(), this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                upload_image(resultUri);
                Log.d("RESULT", resultUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void upload_image(Uri imageUri) {

        switch (imageType) {
            case "img_aadhar_front":
                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(img_aadhar_front);
                break;
            case "img_aadhar_back":
                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(img_aadhar_back);
                break;
            case "img_pancard":
                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(img_pancard);
                break;
            case "img_peronal_info":
                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(img_peronal_info);
                break;
            case "img_selfie":
                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(img_selfie);
                break;
        }

        uploadImageToServer(imageUri);
    }


    private void uploadImageToServer(Uri imageUri) {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());

        try {
            String path = FilePath.getPath(getActivity(), imageUri);
            File file = new File(path);

            try {
                file = new Compressor(getActivity()).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("post_image", file.getName(), reqFile);
            RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), userId);

            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<UploadImage> call = apiInterface.ApiUploadImage(body, Id);
            call.enqueue(new Callback<UploadImage>() {
                @Override
                public void onResponse(Call<UploadImage> call, retrofit2.Response<UploadImage> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getErrorCode().equals("valid")) {
                            saveImageFileNames(response.body());
                            String message = response.body().getMessage();
                            alertMessages.alertMsgBox(message, getActivity());

                        } else {
                            Toast.makeText(getActivity(), "Failed, Recapture the image", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Server not responding", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadImage> call, Throwable error) {
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

    private void saveImageFileNames(UploadImage model) {
        switch (imageType) {
            case "img_aadhar_front":
                aadharFront_str = model.getData().getFileName();
                break;
            case "img_aadhar_back":
                aadharBack_str = model.getData().getFileName();
                break;
            case "img_pancard":
                panCard_str = model.getData().getFileName();
                break;
            case "img_peronal_info":
                personalInfo_str = model.getData().getFileName();
                break;
            case "img_selfie":
                selfie_str = model.getData().getFileName();
                break;
        }
    }


    private void requestLoan() {
        final AlertMessages alertMessages = new AlertMessages();
        alertMessages.showProgressDialog(getActivity());
        try {
            ApIInterface apiInterface = ApiClient.getClient().create(ApIInterface.class);
            Call<UserDetails> call = apiInterface.ApiRequestLoan(userId, amountId, monthsId, totalAMount
                    , dueDate, aadharFront_str, aadharBack_str, panCard_str, personalInfo_str, selfie_str);
            call.enqueue(new Callback<UserDetails>() {
                @Override
                public void onResponse(Call<UserDetails> call, retrofit2.Response<UserDetails> response) {
                    alertMessages.hideProgressDialog();
                    if (response.isSuccessful()) {
                        String message = response.body().getMessage();
                        if (response.body().getErrCode().equals("valid")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_uploadKycFragment_to_nav_home);
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
