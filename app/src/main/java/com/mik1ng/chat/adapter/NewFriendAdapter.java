package com.mik1ng.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mik1ng.chat.R;
import com.mik1ng.chat.databinding.ItemNewFriendBinding;
import com.mik1ng.chat.entity.FriendApplyEntity;
import com.mik1ng.chat.util.Constant;

import java.util.List;

public class NewFriendAdapter extends RecyclerView.Adapter<ViewHolder<ItemNewFriendBinding>> {

    private LayoutInflater layoutInflater;
    private List<FriendApplyEntity.DataBean> list;
    private Context context;

    public NewFriendAdapter(Context context, List<FriendApplyEntity.DataBean> list) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder<ItemNewFriendBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemNewFriendBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemNewFriendBinding> holder, int position) {
        Glide.with(context)
                .load(list.get(position).getFromAvator())
                .into(holder.bind.ivAvatar);

        holder.bind.tvName.setText(list.get(position).getFromName());
        holder.bind.tvRemark.setText(list.get(position).getRemark());
        if (list.get(position).getState() == Constant.FRIEND_STATE_ADD) {
            holder.bind.tvState.setText(context.getString(R.string.new_friend_already));
            holder.bind.tvState.setBackgroundResource(R.drawable.shape_rectangle_eef_100);
            holder.bind.tvState.setTextColor(Color.parseColor("#999999"));
        } else if (list.get(position).getState() == Constant.FRIEND_STATE_ALREADY){
            holder.bind.tvState.setText(context.getString(R.string.new_friend_agree));
            holder.bind.tvState.setBackgroundResource(R.drawable.shape_rectangle_279_100);
            holder.bind.tvState.setTextColor(Color.WHITE);
        }

        holder.bind.getRoot().setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onItemClick(holder.getLayoutPosition());
            }
        });

        holder.bind.tvState.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onItemClick(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    public interface OnClickListener {
        void onBtnClick(int position);

        void onItemClick(int position);
    }
}
