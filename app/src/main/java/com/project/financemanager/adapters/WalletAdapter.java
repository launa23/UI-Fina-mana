package com.project.financemanager.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Wallet;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder>{
    private List<Wallet> walletList;
    private HandleClick handleClick;
    public WalletAdapter(List<Wallet> walletList, HandleClick handleClick) {
        this.walletList = walletList;
        this.handleClick = handleClick;
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

        private RelativeLayout relativeLayout;
        public WalletViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWallet = itemView.findViewById(R.id.imgWalletInWallet);
            txtWalletName = itemView.findViewById(R.id.txtWalletNameInWallet);
            relativeLayout = itemView.findViewById(R.id.rltWalletItem);
            itemView.setOnClickListener(v -> {
                handleClick.onItemClick(getAdapterPosition());
            });
        }
    }
    public interface HandleClick{
        void onItemClick(int position);
    }
}
