package com.mik1ng.chat.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class ViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
    public T bind;
    public ViewHolder(@NonNull T bind) {
        super(bind.getRoot());
        this.bind = bind;
    }
}
