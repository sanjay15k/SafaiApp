package com.nsit.safaiapp.Calls;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;
import com.nsit.safaiapp.Interface.ApiInterface;
import com.nsit.safaiapp.LoginActivity;

import java.io.IOException;

import retrofit2.Call;

public class RegisterCall extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog progressDialog;

    public RegisterCall(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage("Please wait while we register you!");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
        Call<JsonObject> registerUser = apiInterface.registerUser("sanjayk","sanjay123@gmail.com","Sanjay Kumar","+91-7678366446","sanjay123","2638901848593758","DL105492","RX-224, Street No-3 janakpuri New Delhi-110046");
        try {
            JsonObject jsonObject = registerUser.execute().body();
            System.out.println("OUTPUT is : "+jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
