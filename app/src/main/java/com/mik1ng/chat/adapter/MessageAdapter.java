package com.mik1ng.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.databinding.ItemMessageBinding;
import com.mik1ng.chat.entity.MessageEntity;
import com.mik1ng.chat.interfaces.OnItemClickListener;
import com.mik1ng.chat.interfaces.OnItemLongClickListener;
import com.mik1ng.chat.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<ViewHolder<ItemMessageBinding>> {

    private LayoutInflater layoutInflater;
    private List<MessageEntity> list;
    private Context context;

    public MessageAdapter(Context context, List<MessageEntity> list) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder<ItemMessageBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemMessageBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemMessageBinding> holder, int position) {

        Glide.with(context)
                .load(list.get(position).getAvatar())
                .into(holder.bind.ivAvatar);

        holder.bind.tvName.setText(list.get(position).getName());
        holder.bind.tvDate.setText(list.get(position).getDate());
        holder.bind.tvContent.setText(list.get(position).getContent());
        if (list.get(position).getCount() > 0) {
            holder.bind.tvCount.setText(String.valueOf(list.get(position).getCount()));
            holder.bind.tvCount.setVisibility(View.VISIBLE);
        } else {
            holder.bind.tvCount.setText(String.valueOf(list.get(position).getCount()));
            holder.bind.tvCount.setVisibility(View.GONE);
        }
        holder.bind.getRoot().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(holder.getLayoutPosition());
            }
        });

        holder.bind.getRoot().setOnLongClickListener(view -> {
            if (onItemLongClickListener == null) return false;
            return onItemLongClickListener.onLongClick(holder.getLayoutPosition());
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemMessageBinding> holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        for (Object o : payloads) {
            switch ((int) o) {
                case Constant.ADAPTER_PAYLOADS_0:               //改变红点状态
                    if (list.get(position).getCount() > 0) {
                        holder.bind.tvCount.setText(String.valueOf(list.get(position).getCount()));
                        holder.bind.tvCount.setVisibility(View.VISIBLE);
                    } else {
                        holder.bind.tvCount.setText(String.valueOf(list.get(position).getCount()));
                        holder.bind.tvCount.setVisibility(View.GONE);
                    }
                    break;
                case Constant.ADAPTER_PAYLOADS_1:               //改变时间
                    holder.bind.tvDate.setText(list.get(position).getDate());
                    break;
                case Constant.ADAPTER_PAYLOADS_2:               //改变内容
                    holder.bind.tvContent.setText(list.get(position).getContent());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
}
