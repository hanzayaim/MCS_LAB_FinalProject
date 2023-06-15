package com.example.mcs_lab_finalproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mcs_lab_finalproject.R;

public class MedicineDetailActivity extends AppCompatActivity {
    private TextView tvMedicineName;
    private TextView tvMedicineManufacturer;
    private TextView tvMedicinePrice;
    private ImageView ivMedicineImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineManufacturer = findViewById(R.id.tvMedicineManufacturer);
        tvMedicinePrice = findViewById(R.id.tvMedicinePrice);
        ivMedicineImage = findViewById(R.id.ivMedicineImage);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String manufacturer = intent.getStringExtra("manufacturer");
        int price = intent.getIntExtra("price", 0);
        String image = intent.getStringExtra("image");

        tvMedicineName.setText(name);
        tvMedicineManufacturer.setText(manufacturer);
        tvMedicinePrice.setText(String.valueOf(price));

        Glide.with(this)
                .load(image)
                .centerCrop()
                .into(ivMedicineImage);
    }
}