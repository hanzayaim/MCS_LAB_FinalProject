package com.example.mcs_lab_finalproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.UsersHelper;

import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    UsersHelper db;
    EditText etName, etEmail, etPassword, etConfirmPassword, etPhone;
    Button btnRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new UsersHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                if (TextUtils.isEmpty(name) || name.length() < 5) {
                    Toast.makeText(RegisterActivity.this, "Please enter name with minimum 5 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!email.endsWith(".com") || !email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password) || !password.matches("^[a-zA-Z0-9]*$")) {
                    Toast.makeText(RegisterActivity.this, "Password must be alphanumeric!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(RegisterActivity.this, "Please enter a phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String otp = generateOTP();

                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            1);
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, otp, null, null);
                }

                boolean isInserted = db.insertData(name, email, password, phone, otp);
                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent moveToOTP = new Intent(RegisterActivity.this, OTPActivity.class);
                    moveToOTP.putExtra("email", email);
                    startActivity(moveToOTP);
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(moveToLogin);
            }
        });
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}