package com.mik1ng.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mik1ng.chat.R;
import com.mik1ng.chat.databinding.ItemMessageBinding;
import com.mik1ng.chat.entity.MessageBean;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<ViewHolder<ItemMessageBinding>> {

    private LayoutInflater layoutInflater;
    private List<MessageBean> list;

    public MessageAdapter(Context context, List<MessageBean> list) {
        layoutInflater = LayoutInflater.from(context);
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }

    @NonNull
    @Override
    public ViewHolder<ItemMessageBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemMessageBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemMessageBinding> holder, int position) {
        holder.bind.ivAvatar.setImageResource(R.mipmap.icon_app);
        holder.bind.tvName.setText(list.get(position).getName());
        holder.bind.tvDate.setText(list.get(position).getDate());
        holder.bind.tvContent.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
