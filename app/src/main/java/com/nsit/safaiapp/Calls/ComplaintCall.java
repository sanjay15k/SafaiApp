package com.nsit.safaiapp.Calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;
import com.nsit.safaiapp.Interface.ApiInterface;
import com.nsit.safaiapp.MainScreenActivity;

import java.io.IOException;

import retrofit2.Call;

public class ComplaintCall extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog progressDialog;
    private String username;
    private double latitude;
    private double longitude;
    private String comments;
    private String responseDate;

    public ComplaintCall(Context context, double latitude, double longitude, String username, String comments){
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.username = username;
        this.comments = comments;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while we file your complaint!");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
        Call<JsonObject> fileAComplaint = apiInterface.fileAComplaint(latitude,longitude,username,comments);
        try {
            JsonObject jsonObject = fileAComplaint.execute().body();
            assert jsonObject != null;
            responseDate = jsonObject.get("date").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();

        Toast.makeText(context,"Your complaint has been successfully filed on "+responseDate,Toast.LENGTH_LONG).show();

    }

}
