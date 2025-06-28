package com.spotme.spotme.deals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spotme.spotme.R;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<Deal> deals;

    @NonNull
    @Override
    public DealsAdapter.DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_card_item, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsAdapter.DealViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.name.setText(deal.getName());
        holder.amount.setText(deal.getAmount());
        holder.reason.setText(deal.getReason());
        holder.time.setText(deal.getTime());
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public void updateDeals(List<Deal> newDeals) {
        this.deals = newDeals;
        notifyDataSetChanged();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {
        TextView name, reason, amount, time;

        public DealViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            reason = itemView.findViewById(R.id.reason);
            amount = itemView.findViewById(R.id.amount);
            time = itemView.findViewById(R.id.time);
        }
    }
}
