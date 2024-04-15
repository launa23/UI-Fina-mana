package com.project.financemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Wallet;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder>{
    private List<Wallet> walletList;

    public WalletAdapter(List<Wallet> walletList) {
        this.walletList = walletList;
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item, parent, false);
        return new WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        Wallet wallet = walletList.get(position);
        holder.txtWalletName.setText(wallet.getName());

    }

    @Override
    public int getItemCount() {
        return walletList.size();
    }

    class WalletViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgWallet;
        private TextView txtWalletName;
        public WalletViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWallet = itemView.findViewById(R.id.imgWalletInWallet);
            txtWalletName = itemView.findViewById(R.id.txtWalletNameInWallet);
        }
    }
}
