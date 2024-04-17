package com.project.financemanager.adapters;

import com.project.financemanager.models.Transaction;

public interface RvItemClickListener {
    void onChildItemClick(int parentPosition, int childPosition, Transaction item);
}
