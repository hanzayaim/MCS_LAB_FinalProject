package com.example.mcs_lab_finalproject.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;
import com.example.mcs_lab_finalproject.view.MedicineDetailActivity;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicines> medicinesList;

    public MedicineAdapter(Context context, List<Medicines> medicinesList) {
        this.context = context;
        this.medicinesList = medicinesList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.MedicineViewHolder holder, int position) {
        Medicines medicine = medicinesList.get(position);

        holder.tvMedicineName.setText(medicine.getName());
        holder.tvMedicineManufacturer.setText(medicine.getManufacturer());
        holder.tvMedicinePrice.setText(String.valueOf(medicine.getPrice()));
        Glide.with(context).load(medicine.getImage()).into(holder.ivMedicine);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicineDetailActivity.class);
                intent.putExtra("medicineId", medicine.getMedicineID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicinesList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMedicine;
        TextView tvMedicineName, tvMedicineManufacturer, tvMedicinePrice;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMedicine = itemView.findViewById(R.id.ivMedicine);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvMedicineManufacturer = itemView.findViewById(R.id.tvMedicineManusfacturer);
            tvMedicinePrice = itemView.findViewById(R.id.tvMedicinePrice);
        }
    }
}
