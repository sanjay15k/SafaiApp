package com.nsit.safaiapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nsit.safaiapp.Calls.RegisterCall;
import com.nsit.safaiapp.CommonUtils.RetrofitInstance;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText phoneNumberEditText;
    private CardView adhaarCardView;
    private TextView adhaarCardStatusTextView;
    private ImageView adhaarCardImageView;
    private EditText houseNoEditText;
    private EditText localityEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText pincodeEditText;
    private Button registerButton;
    private TextView loginTextView;
    private String adhaarCardURL = null;
    private static int ADHAAR_CARD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.usernameEditText);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        adhaarCardView = findViewById(R.id.adhaarCardView);
        adhaarCardStatusTextView = findViewById(R.id.adhaarCardStatusTextView);
        adhaarCardImageView = findViewById(R.id.adhaarCardImageView);

        houseNoEditText = findViewById(R.id.houseNoEditText);
        localityEditText = findViewById(R.id.localityEditText);
        cityEditText = findViewById(R.id.cityEditText);
        stateEditText = findViewById(R.id.stateEditText);
        pincodeEditText = findViewById(R.id.pincodeEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        adhaarCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ADHAAR_CARD);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullName = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String houseNo = houseNoEditText.getText().toString();
                String locality = localityEditText.getText().toString();
                String city = cityEditText.getText().toString();
                String state = stateEditText.getText().toString();
                String pinCode = pincodeEditText.getText().toString();
                String completeAddress = houseNo + ", " + locality + ", " + city + ", " + state + ", " + pinCode;

                RegisterCall register = new RegisterCall(RegistrationActivity.this);
                register.execute();

//                if (adhaarCardURL !=null && licenseCardURL !=null){
//                    LoginCall register = new LoginCall(Registeration.this);
//                    register.execute();
//                }
//                else{
//                    Toast.makeText(Registeration.this,"Adhaar or License missing!",Toast.LENGTH_LONG).show();
//                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADHAAR_CARD) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            adhaarCardImageView.setImageBitmap(image);
            adhaarCardStatusTextView.setText("STATUS : ACCEPTED");
            adhaarCardStatusTextView.setTextColor(Color.parseColor("#ff0000"));
        }
    }

}
