package com.example.mcs_lab_finalproject.viewmodel;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mcs_lab_finalproject.R;
import com.example.mcs_lab_finalproject.model.MedicinesHelper;
import com.example.mcs_lab_finalproject.model.Transaction;
import com.example.mcs_lab_finalproject.model.TransactionsHelper;

import java.util.List;

public class TransactionFragment extends Fragment implements TransactionAdapter.TransactionInteractionListener {

    private RecyclerView rvTransactions;
    private TransactionsHelper transactionsHelper;
    private MedicinesHelper medicinesHelper;
    private int currentUserId;

    private TransactionAdapter adapter;


    private static final int REQUEST_CODE_MEDICINE_DETAIL = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_section, container, false);

        transactionsHelper = new TransactionsHelper(getContext());
        medicinesHelper = new MedicinesHelper(getContext());
        rvTransactions = view.findViewById(R.id.rvTransaction);

        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("userID", 0);

        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        Log.d("TransactionFragment", "Transaction List Size: " + transactionList.size());

        adapter = new TransactionAdapter(transactionList, getContext(), this, medicinesHelper, transactionsHelper);
        adapter.setTransactionList(transactionList);
        rvTransactions.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;

    }

    // Override onActivityResult()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MEDICINE_DETAIL && resultCode == RESULT_OK) {
            refreshData();
        }
    }


    @Override
    public void onUpdateClick(int position) {
        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        Transaction transaction = transactionList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Quantity");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quantityStr = input.getText().toString();
                if (quantityStr.isEmpty()) {
                    Toast.makeText(getContext(), "Quantity must be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    Toast.makeText(getContext(), "Quantity must be more than 0!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    transactionsHelper.updateTransaction(transaction.getTransactionID(), quantity);
                    Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();

                    List<Transaction> updatedTransactionList = transactionsHelper.getAllDataByUser(currentUserId);
                    adapter.setTransactionList(updatedTransactionList); // Mengatur daftar transaksi yang diperbarui
                    adapter.notifyDataSetChanged(); // Memperbarui tampilan daftar transaksi setelah pembaruan
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Update failed due to an error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("UPDATE_ERROR", "Error occurred during update", e);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void refreshData() {
        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        adapter.setTransactionList(transactionList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDeleteClick(int position) {
        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        Transaction transaction = transactionList.get(position);
        try {
            transactionsHelper.deleteTransaction(transaction.getTransactionID());
            Toast.makeText(getContext(), "Delete successful!", Toast.LENGTH_SHORT).show();

            List<Transaction> updatedTransactionList = transactionsHelper.getAllDataByUser(currentUserId);
            adapter.setTransactionList(updatedTransactionList);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Delete failed due to an error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("DELETE_ERROR", "Error occurred during delete", e);
        }

    }
}