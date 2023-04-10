package com.mik1ng.chat.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.mik1ng.chat.R;
import com.mik1ng.chat.databinding.ItemFriendBinding;
import com.mik1ng.chat.entity.FriendEntity;
import com.mik1ng.chat.util.CharUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FriendsAdapter extends RecyclerView.Adapter<ViewHolder<ItemFriendBinding>> {

    private LayoutInflater layoutInflater;
    private List<FriendEntity> list = new ArrayList<>();
    private Context context;

    public static final int TYPE_FRIEND = 0;
    public static final int TYPE_CATEGORY = 1;

    public FriendsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder<ItemFriendBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemFriendBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemFriendBinding> holder, int position) {
        if (list.get(position).getType() == TYPE_FRIEND) {
            holder.bind.layoutFriend.setVisibility(View.VISIBLE);
            holder.bind.tvCategory.setVisibility(View.GONE);

            Glide.with(context)
                    .load(list.get(position).getAvatar())
                    .format(DecodeFormat.PREFER_RGB_565)
                    .into(holder.bind.ivAvatar);

            holder.bind.tvName.setText(TextUtils.isEmpty(list.get(position).getRemark()) ? list.get(position).getNickName() : list.get(position).getRemark());
            holder.bind.getRoot().setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(list.get(position).getPosition());
                }
            });
        } else {
            holder.bind.layoutFriend.setVisibility(View.GONE);
            holder.bind.tvCategory.setVisibility(View.VISIBLE);

            holder.bind.tvCategory.setText(list.get(position).getFirstChar());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<FriendEntity> list) {
        initData(list);
    }

    private void initData(List<FriendEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list.clear();

        HashMap<String, List<FriendEntity>> map = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setFirstChar(CharUtils.getFirstCharFromChinese(TextUtils.isEmpty(list.get(i).getRemark()) ? list.get(i).getNickName() : list.get(i).getRemark()));
            list.get(i).setType(TYPE_FRIEND);
            list.get(i).setPosition(i);
            if (map.containsKey(list.get(i).getFirstChar())) {
                List<FriendEntity> temList = map.get(list.get(i).getFirstChar());
                if (temList != null) {
                    temList.add(list.get(i));
                }
            } else {
                List<FriendEntity> temList = new ArrayList<>();
                temList.add(list.get(i));
                map.put(list.get(i).getFirstChar(), temList);
            }
        }



        for (String l : context.getResources().getStringArray(R.array.all_letters)) {
            if (map.containsKey(l)) {
                FriendEntity entity = new FriendEntity(l, FriendsAdapter.TYPE_CATEGORY);
                this.list.add(entity);
                this.list.addAll(Objects.requireNonNull(map.get(l)));
            }
        }
        notifyItemRangeChanged(0, this.list.size());
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
