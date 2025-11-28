package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ItemDepositBinding;
import java.util.List;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.DepositViewHolder> {
    private List<Deposit> depositList;
    private OnDepositClickListener onDepositClickListener;

    public interface OnDepositClickListener {
        void onDepositClick(Deposit deposit);
    }

    public DepositAdapter(List<Deposit> depositList) {
        this.depositList = depositList;
    }

    public DepositAdapter(List<Deposit> depositList, OnDepositClickListener listener) {
        this.depositList = depositList;
        this.onDepositClickListener = listener;
    }

    public void setOnDepositClickListener(OnDepositClickListener listener) {
        this.onDepositClickListener = listener;
    }

    @Override
    public DepositViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDepositBinding binding = ItemDepositBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DepositViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DepositViewHolder holder, int position) {
        Deposit deposit = depositList.get(position);
        holder.bind(deposit, onDepositClickListener);
    }

    @Override
    public int getItemCount() {
        return depositList.size();
    }

    public void updateList(List<Deposit> newList) {
        this.depositList = newList;
        notifyDataSetChanged();
    }

    static class DepositViewHolder extends RecyclerView.ViewHolder {
        private ItemDepositBinding binding;

        DepositViewHolder(ItemDepositBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Deposit deposit, OnDepositClickListener listener) {
            binding.depositPackagingType.setText(deposit.getPackagingType());
            binding.depositValue.setText("Value: " + deposit.getDepositValue() + "$");
            binding.depositBarcode.setText("Barcode: " + deposit.getBarcode());
            binding.depositAddedDate.setText("Added: " + deposit.getAddedDate());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDepositClick(deposit);
                }
            });
        }
    }
}