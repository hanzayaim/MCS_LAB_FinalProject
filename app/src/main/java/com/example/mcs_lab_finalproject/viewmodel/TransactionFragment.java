package com.example.mcs_lab_finalproject.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        rvTransactions.setAdapter(new TransactionAdapter(transactionList, getContext(), this, medicinesHelper));

        return view;
    }

    @Override
    public void onUpdateClick(int position) {
        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        Transaction transaction = transactionList.get(position);
        Toast.makeText(getContext(), "Update clicked for position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        List<Transaction> transactionList = transactionsHelper.getAllDataByUser(currentUserId);
        Transaction transaction = transactionList.get(position);
        Toast.makeText(getContext(), "Delete clicked for position: " + position, Toast.LENGTH_SHORT).show();
    }
}