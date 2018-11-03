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

public class SendLocationOfUnmarkedBinCall extends AsyncTask<Void, Void, Void> {
    private Context context;
    private ProgressDialog progressDialog;
    private double latitude;
    private double longitude;
    private JsonObject jsonObject;

    public SendLocationOfUnmarkedBinCall(Context context, double latitude, double longitude){
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while we send this location to our server!");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
        Call<JsonObject> newBinNotMarked = apiInterface.newBinNotMarked(latitude, longitude);
        try {
            jsonObject = newBinNotMarked.execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        if (jsonObject != null){
            Toast.makeText(context,"Location has been added for verification and will be shown on map after that!",Toast.LENGTH_LONG).show();
        }
    }
}
