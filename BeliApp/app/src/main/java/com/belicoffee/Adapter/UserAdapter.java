package com.belicoffee.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belicoffee.Model.ChatUser;
import com.belicoffee.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    ArrayList<ChatUser> chatUsers;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;
    boolean type;
    final boolean VERTICAL = true;
    final boolean HORIZONTAL = false;
    String myId;
    String myName;


    public UserAdapter(Context context, String myId, String myName, ArrayList<ChatUser> chatUsers, OnItemClickListener clickListener, OnItemLongClickListener longClickListener, boolean type) {
        this.context = context;
        this.myId = myId;
        this.myName = myName;
        this.chatUsers = chatUsers;
        this.onItemClickListener = clickListener;
        this.onItemLongClickListener = longClickListener;
        this.type = type;
    }

    public void updateData(ArrayList<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        if (type == VERTICAL) {
            view = inflater.inflate(R.layout.item_user_vertical, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.item_user_horizontal, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ChatUser chatUser = chatUsers.get(i);
        viewHolder.tvUsername.setText(chatUser.getUsername());
        if (chatUser.getAvatar() != null) {
            Glide.with(context).load(chatUser.getAvatar()).override(54, 54).centerCrop().into((viewHolder).ivAvatar);
        }
        if (type == VERTICAL) {
            viewHolder.tvMessage.setText(chatUser.getLastMessage());
            viewHolder.tvTime.setText(chatUser.getLastTime());
        }
        viewHolder.rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(chatUser);
            }
        });
        viewHolder.rlUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(myId, myName, chatUser);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        ImageView ivAvatar;
        TextView tvMessage;
        TextView tvTime;
        RelativeLayout rlUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if (type == VERTICAL) {
                tvUsername = itemView.findViewById(R.id.tv_username_user_ver);
                ivAvatar = itemView.findViewById(R.id.iv_avatar_user_ver);
                tvMessage = itemView.findViewById(R.id.tv_message_user_ver);
                tvTime = itemView.findViewById(R.id.tv_time_user_ver);
                rlUser = itemView.findViewById(R.id.rl_user_ver);
            } else {
                tvUsername = itemView.findViewById(R.id.tv_username_user_hor);
                ivAvatar = itemView.findViewById(R.id.iv_avatar_user_hor);
                rlUser = itemView.findViewById(R.id.rl_user_hor);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ChatUser chatUser);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(String myId, String myName, ChatUser chatUser);
    }
}
