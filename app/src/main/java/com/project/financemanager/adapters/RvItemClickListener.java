package com.project.financemanager.adapters;

import com.project.financemanager.models.Transaction;

public interface RvItemClickListener<T> {
    void onChildItemClick(int parentPosition, int childPosition, T item);
}
