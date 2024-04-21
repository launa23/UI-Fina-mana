package com.project.financemanager.common;

import com.project.financemanager.models.Transaction;

public interface RvItemClickListener<T> {
    void onChildItemClick(int parentPosition, int childPosition, T item);
}
