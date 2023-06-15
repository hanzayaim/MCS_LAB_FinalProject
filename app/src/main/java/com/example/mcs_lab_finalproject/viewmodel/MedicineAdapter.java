package com.example.mcs_lab_finalproject.viewmodel;

import android.content.Context;
import android.content.Intent;
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
import com.example.mcs_lab_finalproject.view.MedicineDetailActivity;

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicineDetailActivity.class);

                intent.putExtra("medicineID", currentItem.getMedicineID());
                intent.putExtra("name", currentItem.getName());
                intent.putExtra("manufacturer", currentItem.getManufacturer());
                intent.putExtra("price", currentItem.getPrice());
                intent.putExtra("image", currentItem.getImage());
                intent.putExtra("description", currentItem.getDescription());

                context.startActivity(intent);
            }
        });
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