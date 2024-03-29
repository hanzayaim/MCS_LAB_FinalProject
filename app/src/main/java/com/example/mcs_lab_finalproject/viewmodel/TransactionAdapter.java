package com.example.mcs_lab_finalproject.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.Medicines;
import com.example.mcs_lab_finalproject.model.MedicinesHelper;
import com.example.mcs_lab_finalproject.model.Transaction;
import com.example.mcs_lab_finalproject.model.TransactionsHelper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private TransactionInteractionListener listener;
    private MedicinesHelper medicinesHelper;

    private TransactionsHelper transactionsHelper;



    public TransactionAdapter(List<Transaction> transactionList, Context context, TransactionInteractionListener listener, MedicinesHelper medicinesHelper, TransactionsHelper transactionsHelper) {
        this.transactionList = transactionList;
        this.context = context;
        this.listener = listener;
        this.medicinesHelper = medicinesHelper;
        this.transactionsHelper = transactionsHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        Medicines medicine = medicinesHelper.getMedicineById(transaction.getMedicineID());

        holder.tvTransactionDate.setText(format.format(transaction.getTransactionDate()));
        holder.tvMedicineName.setText(String.valueOf(transaction.getMedicineName()));
        holder.tvMedicinePrice.setText(String.valueOf(transaction.getMedicinePrice()));
        holder.tvTransactionQuantity.setText(String.valueOf(transaction.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTransactionDate, tvMedicineName, tvMedicinePrice, tvTransactionQuantity;
        Button btnUpdate, btnDelete;

        public ViewHolder(View itemView, TransactionInteractionListener listener) {
            super(itemView);
            tvTransactionDate = itemView.findViewById(R.id.itemtrTransactionDate);
            tvMedicineName = itemView.findViewById(R.id.itemtrTransactionName);
            tvMedicinePrice = itemView.findViewById(R.id.itemtrTransactionPrice);
            tvTransactionQuantity = itemView.findViewById(R.id.itemtrTransactionQuantity);
            btnUpdate = itemView.findViewById(R.id.itembtnUpdate);
            btnDelete = itemView.findViewById(R.id.itembtnDelete);

            btnUpdate.setOnClickListener(v -> listener.onUpdateClick(getAdapterPosition()));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(getAdapterPosition()));
        }
    }

    public interface TransactionInteractionListener {
        void onUpdateClick(int position);
        void onDeleteClick(int position);
    }

    public void setTransactionList(List<Transaction> newTransactionList) {
        this.transactionList = newTransactionList;
        notifyDataSetChanged();
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactionList = newTransactions;
        notifyDataSetChanged();
    }
}