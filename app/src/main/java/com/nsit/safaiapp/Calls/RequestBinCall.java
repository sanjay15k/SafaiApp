package com.nsit.safaiapp.Calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;
import com.nsit.safaiapp.Interface.ApiInterface;

import java.io.IOException;

import retrofit2.Call;

public class RequestBinCall extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog progressDialog;
    private String address;
    private String username;
    private String message;

    public RequestBinCall(Context context, String address, String username){
        this.context = context;
        this.username = username;
        this.address = address;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while we register your request!");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
        Call<JsonObject> requestBin = apiInterface.requestBin(address, username);
        try {
            JsonObject jsonObject = requestBin.execute().body();
            assert jsonObject != null;
            message = jsonObject.get("message").getAsString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
