package com.example.budgetpal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.activities.Accounts;
import com.example.budgetpal.data_models.SpendingsModel;
import com.example.budgetpal.model.tables.Revenue;
import com.example.budgetpal.view_models.AccountsViewModel;
import com.example.budgetpal.view_models.MainActivityViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AccountsRecyclerAdapter extends RecyclerView.Adapter<AccountsRecyclerAdapter.MyViewHolder> {

    private ArrayList<Revenue> data;

    private int user_id;

    private AccountsViewModel accountsViewModel;

    private int year;
    private String month, account_name;

    private BigDecimal account_value;

    private LifecycleOwner lifecycleOwner;
    public AccountsRecyclerAdapter(ArrayList<Revenue> data, int user_id, AccountsViewModel accountsViewModel,String month, int year
    , LifecycleOwner lifecycleOwner) {
        this.data = data;
        this.user_id = user_id;
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
        account_name = holder.item_name.getText().toString();
        account_value = new BigDecimal(holder.item_value.getText().toString().replace(",", "."));
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
                        accountsViewModel.deleteRevenue(user_id, month, year, account_name);
                        LiveData<BigDecimal> dataReturned = accountsViewModel.getTotalMoney(user_id);
                        dataReturned.observe(lifecycleOwner, new Observer<BigDecimal>() {
                            @Override
                            public void onChanged(BigDecimal bigDecimal) {
                                dataReturned.removeObserver(this);
                                bigDecimal = bigDecimal.subtract(account_value);
                                accountsViewModel.updateTotalMoneyInDB(user_id,bigDecimal);
                            }
                        });
                        notifyItemRemoved(adapterPosition);
                    }
                }
            });
        }
    }

}
