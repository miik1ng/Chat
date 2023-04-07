package com.mik1ng.chat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.mik1ng.chat.R;
import com.mik1ng.chat.databinding.ItemChatBinding;
import com.mik1ng.chat.entity.ChatRecordEntity;
import com.mik1ng.chat.util.Constant;
import com.mik1ng.chat.util.DateUtils;
import com.mik1ng.chat.util.DensityUtils;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ViewHolder<ItemChatBinding>> {

    private LayoutInflater layoutInflater;
    private List<ChatRecordEntity> list;
    private Context context;

    public ChatAdapter(Context context, List<ChatRecordEntity> list) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder<ItemChatBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder<>(ItemChatBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemChatBinding> holder, int position) {
        setModeUI(holder, list.get(position).getRecordMode());
        setTypeUI(holder, list.get(position).getRecordType());

        if (list.get(position).getRecordMode() == Constant.CHAT_RECORD_MODE_RECEIVE) {
            //接收
            Glide.with(context)
                    .load(list.get(position).getAvatar())
                    .into(holder.bind.ivAvatarLeft);

            switch (list.get(position).getRecordType()) {
                case Constant.CHAT_RECORD_TYPE_TEXT:
                    //文字
                    holder.bind.tvTextLeft.setText(list.get(position).getText());
                    break;
                case Constant.CHAT_RECORD_TYPE_IMAGE:
                    //图片
                    if (list.get(position).getImgWidth() > 0) {
                        if (list.get(position).getImgWidth() < DensityUtils.dp2px(context, Constant.CHAT_IMAGE_MAX_WIDTH)) {
                            holder.bind.ivImgLeft.getLayoutParams().width = (int) list.get(position).getImgWidth();
                            holder.bind.ivImgLeft.getLayoutParams().height = (int) list.get(position).getImgHeight();
                        } else {
                            holder.bind.ivImgLeft.getLayoutParams().width = DensityUtils.dp2px(context, Constant.CHAT_IMAGE_MAX_WIDTH);
                            holder.bind.ivImgLeft.getLayoutParams().height = DensityUtils.dp2px(context, (Constant.CHAT_IMAGE_MAX_WIDTH * list.get(position).getImgHeight()) / list.get(position).getImgWidth());
                        }
                    }


                    Glide.with(context)
                            .load(list.get(position).getImage())
                            .into(holder.bind.ivImgLeft);
                    break;
                case Constant.CHAT_RECORD_TYPE_VOICE:
                    //语音条
                    holder.bind.tvVoiceSecondLeft.setText(context.getString(R.string.chat_item_voice_second, String.valueOf(list.get(position).getSecond())));
                    break;
                case Constant.CHAT_RECORD_TYPE_LOCATION:
                    //定位
                    holder.bind.tvLocationNameLeft.setText(list.get(position).getLocationName());
                    holder.bind.tvLocationAddressLeft.setText(list.get(position).getLocationAddress());
                    break;
            }
        } else if (list.get(position).getRecordMode() == Constant.CHAT_RECORD_MODE_SEND) {
            //发送
            Glide.with(context)
                    .load(list.get(position).getAvatar())
                    .into(holder.bind.ivAvatarRight);

            switch (list.get(position).getRecordType()) {
                case Constant.CHAT_RECORD_TYPE_TEXT:
                    //文字
                    holder.bind.tvTextRight.setText(list.get(position).getText());
                    break;
                case Constant.CHAT_RECORD_TYPE_IMAGE:
                    //图片
                    if (list.get(position).getImgWidth() > 0) {
                        if (list.get(position).getImgWidth() < DensityUtils.dp2px(context, Constant.CHAT_IMAGE_MAX_WIDTH)) {
                            holder.bind.ivImgRight.getLayoutParams().width = (int) list.get(position).getImgWidth();
                            holder.bind.ivImgRight.getLayoutParams().height = (int) list.get(position).getImgHeight();
                        } else {
                            holder.bind.ivImgRight.getLayoutParams().width = DensityUtils.dp2px(context, Constant.CHAT_IMAGE_MAX_WIDTH);
                            holder.bind.ivImgRight.getLayoutParams().height = DensityUtils.dp2px(context, (Constant.CHAT_IMAGE_MAX_WIDTH * list.get(position).getImgHeight()) / list.get(position).getImgWidth());
                        }
                    }

                    Glide.with(context)
                            .load(list.get(position).getImage())
                            .into(holder.bind.ivImgRight);
                    break;
                case Constant.CHAT_RECORD_TYPE_VOICE:
                    //语音条
                    holder.bind.tvVoiceSecondRight.setText(context.getString(R.string.chat_item_voice_second, String.valueOf(list.get(position).getSecond())));
                    break;
                case Constant.CHAT_RECORD_TYPE_LOCATION:
                    //定位
                    holder.bind.tvLocationNameRight.setText(list.get(position).getLocationName());
                    holder.bind.tvLocationAddressRight.setText(list.get(position).getLocationAddress());
                    break;
            }

        } else if (list.get(position).getRecordMode() == Constant.CHAT_RECORD_MODE_TIME) {
            //时间
            holder.bind.tvTimeCenter.setText(DateUtils.getMmDdHhMmSs(list.get(position).getTimestamp()));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<ItemChatBinding> holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setModeUI(ViewHolder<ItemChatBinding> holder, int mode) {
        holder.bind.layoutLeft.setVisibility(mode == Constant.CHAT_RECORD_MODE_RECEIVE ? View.VISIBLE : View.GONE);
        holder.bind.layoutRight.setVisibility(mode == Constant.CHAT_RECORD_MODE_SEND ? View.VISIBLE : View.GONE);
        holder.bind.tvTimeCenter.setVisibility(mode == Constant.CHAT_RECORD_MODE_TIME ? View.VISIBLE : View.GONE);
    }

    private void setTypeUI(ViewHolder<ItemChatBinding> holder, int type) {
        holder.bind.tvTextLeft.setVisibility(type == Constant.CHAT_RECORD_TYPE_TEXT ? View.VISIBLE : View.GONE);
        holder.bind.tvTextRight.setVisibility(type == Constant.CHAT_RECORD_TYPE_TEXT ? View.VISIBLE : View.GONE);
        holder.bind.ivImgLeft.setVisibility(type == Constant.CHAT_RECORD_TYPE_IMAGE ? View.VISIBLE : View.GONE);
        holder.bind.ivImgRight.setVisibility(type == Constant.CHAT_RECORD_TYPE_IMAGE ? View.VISIBLE : View.GONE);
        holder.bind.layoutVoiceLeft.setVisibility(type == Constant.CHAT_RECORD_TYPE_VOICE ? View.VISIBLE : View.GONE);
        holder.bind.layoutVoiceRight.setVisibility(type == Constant.CHAT_RECORD_TYPE_VOICE ? View.VISIBLE : View.GONE);
        holder.bind.layoutLocationLeft.setVisibility(type == Constant.CHAT_RECORD_TYPE_LOCATION ? View.VISIBLE : View.GONE);
        holder.bind.layoutLocationRight.setVisibility(type == Constant.CHAT_RECORD_TYPE_LOCATION ? View.VISIBLE : View.GONE);
    }
}
