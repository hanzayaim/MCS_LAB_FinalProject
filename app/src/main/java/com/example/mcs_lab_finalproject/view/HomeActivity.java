package com.example.mcs_lab_finalproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;
import com.example.mcs_lab_finalproject.model.MedicinesHelper;
import com.example.mcs_lab_finalproject.viewmodel.MedicineAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String url = "https://mocki.io/v1/ae13b04b-13df-4023-88a5-7346d5d3c7eb";
    private ImageView logoApp;
    private RecyclerView rvHomeSection;
    private List<Medicines> medicinesList;
    private MedicineAdapter medicineAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logoApp = findViewById(R.id.logoApp);
        Glide.with(this).load(R.drawable.logo_app).into(logoApp);

        rvHomeSection = findViewById(R.id.rvHomeSection);
        rvHomeSection.setLayoutManager(new LinearLayoutManager(this));
        medicinesList = new ArrayList<>();

        medicineAdapter = new MedicineAdapter(HomeActivity.this, medicinesList);
        rvHomeSection.setAdapter(medicineAdapter);

        requestQueue = Volley.newRequestQueue(this);
        getMedicineData();

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getMedicineData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("medicines");
                            // Now you can iterate over the jsonArray as you initially intended
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void setupRecyclerView() {
        MedicinesHelper medicinesHelper = new MedicinesHelper(this);
        Cursor cursor = medicinesHelper.getAllData();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MedicinesHelper.COLUMN_MEDICINEID));
            String name = cursor.getString(cursor.getColumnIndex(MedicinesHelper.COLUMN_NAME));
            String manufacturer = cursor.getString(cursor.getColumnIndex(MedicinesHelper.COLUMN_MANUFACTURER));
            int price = cursor.getInt(cursor.getColumnIndex(MedicinesHelper.COLUMN_PRICE));
            String image = cursor.getString(cursor.getColumnIndex(MedicinesHelper.COLUMN_IMAGE));
            String description = cursor.getString(cursor.getColumnIndex(MedicinesHelper.COLUMN_DESCRIPTION));

            Medicines medicine = new Medicines(id, name, manufacturer, price, image, description);
            medicinesList.add(medicine);
        }

        medicineAdapter.notifyDataSetChanged();

        cursor.close();
        medicinesHelper.close();
    }
}