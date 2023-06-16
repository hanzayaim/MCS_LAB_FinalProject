package com.example.mcs_lab_finalproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;
import com.example.mcs_lab_finalproject.model.TransactionsHelper;

public class MedicineDetailActivity extends AppCompatActivity {
    private TextView tvMedicineName;
    private TextView tvMedicineManufacturer;
    private TextView tvMedicinePrice;
    private ImageView ivMedicineImage;

    private TextView tvMedicineDescription;

    private EditText etQuantity;
    private Button btnBuy;
    private TransactionsHelper transactionsHelper;
    private Medicines currentMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineManufacturer = findViewById(R.id.tvMedicineManufacturer);
        tvMedicinePrice = findViewById(R.id.tvMedicinePrice);
        tvMedicineDescription = findViewById(R.id.tvMedicineDesc);
        ivMedicineImage = findViewById(R.id.ivMedicineImage);
        etQuantity = findViewById(R.id.etQuantity);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String manufacturer = intent.getStringExtra("manufacturer");
        int price = intent.getIntExtra("price", 0);
        String image = intent.getStringExtra("image");
        String description = intent.getStringExtra("description");


        tvMedicineName.setText(name);
        tvMedicineManufacturer.setText(manufacturer);
        tvMedicineDescription.setText(description);
        tvMedicinePrice.setText(String.valueOf(price));

        Glide.with(this)
                .load(image)
                .centerCrop()
                .into(ivMedicineImage);

        transactionsHelper = new TransactionsHelper(this);
        btnBuy = findViewById(R.id.btnBuy);

        currentMedicine = new Medicines(
                intent.getIntExtra("medicineID", 0),
                intent.getStringExtra("name"),
                intent.getStringExtra("manufacturer"),
                intent.getIntExtra("price", 0),
                intent.getStringExtra("image"),
                intent.getStringExtra("description")
        );

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int currentUserId = sharedPreferences.getInt("userID", 0);

        btnBuy.setOnClickListener(v -> {
            String quantityStr = etQuantity.getText().toString();
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Quantity must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be more than 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                transactionsHelper.buyTransaction(currentMedicine.getMedicineID(),currentUserId,quantity,currentMedicine.getName(),currentMedicine.getPrice());
                Log.d("Debug", "currentUserId: " + currentUserId);
                Log.d("Debug", "currentMedicineID: " + currentMedicine.getMedicineID());
                Log.d("Debug", "quantity: " + quantity);
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Purchase failed due to an error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("PURCHASE_ERROR", "Error occurred during purchase", e);
            }
        });
    }
}