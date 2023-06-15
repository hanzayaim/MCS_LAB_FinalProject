package com.example.mcs_lab_finalproject.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private Context context;
    private List<Medicines> medicinesList;

    public MedicineAdapter(Context context, List<Medicines> medicinesList) {
        this.context = context;
        this.medicinesList = medicinesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicines currentItem = medicinesList.get(position);

        holder.tvMedicineName.setText(currentItem.getName());
        holder.tvMedicineManufacturer.setText(currentItem.getManufacturer());
        holder.tvMedicinePrice.setText(String.valueOf(currentItem.getPrice()));



        Glide.with(context)
                .load(currentItem.getImage())
                .centerCrop()
                .into(holder.ivMedicineImage);

    }

    @Override
    public int getItemCount() {
        return medicinesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMedicineName;
        public TextView tvMedicineManufacturer;
        public TextView tvMedicinePrice;
        public ImageView ivMedicineImage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMedicineName = itemView.findViewById(R.id.itemtvMedicineName);
            tvMedicineManufacturer = itemView.findViewById(R.id.itemtvMedicineManusfacturer);
            tvMedicinePrice = itemView.findViewById(R.id.itemtvMedicinePrice);
            ivMedicineImage = itemView.findViewById(R.id.itemimgMedicine);
        }
    }
}