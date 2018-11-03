package com.nsit.safaiapp.Calls;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;
import com.nsit.safaiapp.Interface.ApiInterface;
import com.nsit.safaiapp.LoginActivity;
import com.nsit.safaiapp.MainScreenActivity;

import java.io.IOException;

import retrofit2.Call;

public class LoginCall extends AsyncTask<Void, Void, Void> {

    private Context context;
    private ProgressDialog progressDialog;
    private String username;

    public LoginCall(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait while we log you in!");
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance();
        Call<JsonObject> registerUser = apiInterface.loginUser("check2","check2");
        try {
            JsonObject jsonObject = registerUser.execute().body();
            assert jsonObject != null;
            username = jsonObject.get("username").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        if (username != null){
            Intent intent = new Intent(context,MainScreenActivity.class);
            context.startActivity(intent);
        }
        else{
            Toast.makeText(context,"No user exists with the entered username or password might be wrong",Toast.LENGTH_LONG).show();
        }
    }
}
