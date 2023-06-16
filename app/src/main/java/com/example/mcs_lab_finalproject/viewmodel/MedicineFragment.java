package com.example.mcs_lab_finalproject.viewmodel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MedicineFragment extends Fragment {
    private RecyclerView rvMedicine;
    private MedicineAdapter adapter;
    private ArrayList<Medicines> medicinesList;
    private ProgressDialog pDialog;
    private static final String URL_MEDICINES = "https://mocki.io/v1/ae13b04b-13df-4023-88a5-7346d5d3c7eb";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_medicine_section, container, false);

        rvMedicine = (RecyclerView) rootView.findViewById(R.id.rvMedicine);
        medicinesList = new ArrayList<>();
        adapter = new MedicineAdapter(getActivity(), medicinesList);

        rvMedicine.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMedicine.setAdapter(adapter);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        fetchJSON();

        return rootView;
    }

    private void fetchJSON() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_MEDICINES, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            JSONArray medicines = response.getJSONArray("medicines");
                            for (int i = 0; i < medicines.length(); i++) {
                                JSONObject medicine = medicines.getJSONObject(i);
                                int id = i + 1;
                                String name = medicine.getString("name");
                                String manufacturer = medicine.getString("manufacturer");
                                int price = medicine.getInt("price");
                                String imageUrl = medicine.getString("image");
                                String description = medicine.getString("description");

                                Medicines med = new Medicines(id, name, manufacturer, price, imageUrl, description);
                                medicinesList.add(med);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(req);
    }
}