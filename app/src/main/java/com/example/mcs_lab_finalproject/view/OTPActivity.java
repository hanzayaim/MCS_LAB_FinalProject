package com.example.mcs_lab_finalproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.UsersHelper;

import java.util.Random;

public class OTPActivity extends AppCompatActivity {
    UsersHelper db;
    EditText etOTP;
    Button btnEnterOTP, btnResendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        db = new UsersHelper(this);

        etOTP = findViewById(R.id.etOTP);
        btnEnterOTP = findViewById(R.id.btnEnterOTP);
        btnResendOTP = findViewById(R.id.btnResendOTP);

        String email = getIntent().getStringExtra("email");

        btnEnterOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = etOTP.getText().toString().trim();

                if (otp.isEmpty()) {
                    Toast.makeText(OTPActivity.this, "Please enter the OTP!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor result = db.getDataByEmail(email);
                if (result.getCount() == 0) {
                    Toast.makeText(OTPActivity.this, "User not found. Please register!", Toast.LENGTH_SHORT).show();
                } else {
                    result.moveToFirst();
                    String storedOTP = result.getString(result.getColumnIndex(UsersHelper.COLUMN_OTP));
                    if (otp.equals(storedOTP)) {
                        Toast.makeText(OTPActivity.this, "OTP Verification Successful!", Toast.LENGTH_SHORT).show();
                        db.updateData(email, "true");

                        Intent moveToHome = new Intent(OTPActivity.this, HomeActivity.class);
                        startActivity(moveToHome);
                    } else {
                        Toast.makeText(OTPActivity.this, "Wrong OTP!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(OTPActivity.this,
                        Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OTPActivity.this,
                            new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    Cursor result = db.getDataByEmail(email);
                    if (result.getCount() > 0) {
                        result.moveToFirst();
                        String phone = result.getString(result.getColumnIndex(UsersHelper.COLUMN_PHONE));
                        String otp = generateOTP();

                        db.updateOTP(email, otp);

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phone, null, otp, null, null);
                        Toast.makeText(OTPActivity.this, "New OTP has been sent to your phone.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OTPActivity.this, "User not found. Please register!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}