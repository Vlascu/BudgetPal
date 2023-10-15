package com.example.budgetpal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.view_models.AccountsViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AccountsRecyclerAdapter extends RecyclerView.Adapter<AccountsRecyclerAdapter.MyViewHolder> {

    private final ArrayList<Revenue> data;

    private final int userId;

    private final AccountsViewModel accountsViewModel;

    private final int year;
    private final String month;
    private String accountName;

    private BigDecimal accountValue;

    private final LifecycleOwner lifecycleOwner;
    public AccountsRecyclerAdapter(ArrayList<Revenue> data, int userId, AccountsViewModel accountsViewModel, String month, int year
    , LifecycleOwner lifecycleOwner) {
        this.data = data;
        this.userId = userId;
        this.accountsViewModel = accountsViewModel;
        this.month = month;
        this.year = year;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_recycler_view,parent,false);
        return new MyViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item_name.setText(data.get(position).getAccount());
        holder.item_value.setText(String.format("%.2f",data.get(position).getAccount_amount()));
        accountName = holder.item_name.getText().toString();
        accountValue = new BigDecimal(holder.item_value.getText().toString().replace(",", "."));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView item_name, item_value;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name=itemView.findViewById(R.id.accounts_item_name);
            item_value=itemView.findViewById(R.id.accounts_item_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        accountsViewModel.deleteRevenue(userId, month, year, accountName);
                        LiveData<BigDecimal> dataReturned = accountsViewModel.getTotalMoney(userId);
                        dataReturned.observe(lifecycleOwner, new Observer<BigDecimal>() {
                            @Override
                            public void onChanged(BigDecimal bigDecimal) {
                                dataReturned.removeObserver(this);
                                bigDecimal = bigDecimal.subtract(accountValue);
                                accountsViewModel.updateTotalMoneyInDB(userId,bigDecimal);
                            }
                        });
                        notifyItemRemoved(adapterPosition);
                    }
                }
            });
        }
    }

}
