package com.mik1ng.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mik1ng.chat.databinding.ItemChatBinding;
import com.mik1ng.chat.entity.ChatEntity;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ViewHolder<ItemChatBinding>> {

    private LayoutInflater layoutInflater;
    private List<ChatEntity> list;

    private ChatAdapter(Context context, List<ChatEntity> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder<ItemChatBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemChatBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemChatBinding> holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
