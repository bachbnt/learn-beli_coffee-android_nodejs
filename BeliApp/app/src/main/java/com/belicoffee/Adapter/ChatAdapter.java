package com.belicoffee.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.belicoffee.Model.ChatMessage;
import com.belicoffee.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity activity;
    ArrayList<ChatMessage> mChatMessages;
    FirebaseUser mUser;
    final int SEND = 1;
    final int RECEIVE = 0;
    String myName;
    String yourName;


    public ChatAdapter(Activity activity, ArrayList<ChatMessage> chatMessages, String myName, String yourName) {
        this.activity = activity;
        this.mChatMessages = chatMessages;
        this.yourName = yourName;
        this.myName = myName;
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateData(ArrayList<ChatMessage> chatMessages, String myName, String yourName) {
        this.mChatMessages = chatMessages;
        this.myName = myName;
        this.yourName = yourName;
    }

    private ChatMessage getItem(int i) {
        return mChatMessages.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatMessages.get(position).getSender().equals(mUser.getUid()))
            return SEND;
        else
            return RECEIVE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = null;
        if (viewType == SEND) {
            view = inflater.inflate(R.layout.item_message_send, viewGroup, false);
            return new SendHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_message_receive, viewGroup, false);
            return new ReceiveHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        final ChatMessage msg = getItem(i);

        if (msg.getSender().equals(mUser.getUid())) {
            ((SendHolder) viewHolder).tvMessage.setText(msg.getMessage());
            ((SendHolder) viewHolder).tvName.setVisibility(View.GONE);
//            if (i != 0 && msg.getSender().equals(getItem(i - 1).getSender())) {
//                ((SendHolder) viewHolder).tvName.setVisibility(View.GONE);
//            } else {
//                ((SendHolder) viewHolder).tvName.setVisibility(View.VISIBLE);
//                ((SendHolder) viewHolder).tvName.setText(myName);
//            }
            ((SendHolder) viewHolder).tvTime.setText(msg.getTime());
            if (msg.getAvatar() != null) {
                if (i != mChatMessages.size() - 1 && msg.getSender().equals(getItem(i + 1).getSender())) {
                    ((SendHolder) viewHolder).cvAvatar.setVisibility(View.INVISIBLE);
                } else {
                    ((SendHolder) viewHolder).cvAvatar.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(msg.getAvatar()).override(32, 32).centerCrop().into(((SendHolder) viewHolder).ivAvatar);
                }
            } else {
                if (i != mChatMessages.size() - 1 && msg.getSender().equals(getItem(i + 1).getSender())) {
                    ((SendHolder) viewHolder).cvAvatar.setVisibility(View.INVISIBLE);
                } else {
                    ((SendHolder) viewHolder).cvAvatar.setVisibility(View.VISIBLE);
                }
            }
        } else {
            ((ReceiveHolder) viewHolder).tvMessage.setText(msg.getMessage());
            ((ReceiveHolder) viewHolder).tvName.setVisibility(View.GONE);
//            if (i != 0 && msg.getReceiver().equals(getItem(i - 1).getReceiver())) {
//                ((ReceiveHolder) viewHolder).tvName.setVisibility(View.GONE);
//            } else {
//                ((ReceiveHolder) viewHolder).tvName.setVisibility(View.VISIBLE);
//                ((ReceiveHolder) viewHolder).tvName.setText(yourName);
//            }
            ((ReceiveHolder) viewHolder).tvTime.setText(msg.getTime());
            if (msg.getAvatar() != null) {
                if (i != mChatMessages.size() - 1 && msg.getReceiver().equals(getItem(i + 1).getReceiver())) {
                    ((ReceiveHolder) viewHolder).cvAvatar.setVisibility(View.INVISIBLE);
                } else {
                    ((ReceiveHolder) viewHolder).cvAvatar.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(msg.getAvatar()).override(32, 32).centerCrop().into(((ReceiveHolder) viewHolder).ivAvatar);
                }
            } else {
                if (i != mChatMessages.size() - 1 && msg.getReceiver().equals(getItem(i + 1).getReceiver())) {
                    ((ReceiveHolder) viewHolder).cvAvatar.setVisibility(View.INVISIBLE);
                } else {
                    ((ReceiveHolder) viewHolder).cvAvatar.setVisibility(View.VISIBLE);
                }

            }

        }
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    public static class SendHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvName;
        TextView tvTime;
        ImageView ivAvatar;
        CardView cvAvatar;

        public SendHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_content_send_message);
            tvName = itemView.findViewById(R.id.tv_name_send_message);
            tvTime = itemView.findViewById(R.id.tv_time_send_message);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_send_message);
            cvAvatar = itemView.findViewById(R.id.cv_avatar_send_message);
        }
    }

    public static class ReceiveHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvName;
        TextView tvTime;
        ImageView ivAvatar;
        CardView cvAvatar;

        public ReceiveHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_content_receive_message);
            tvName = itemView.findViewById(R.id.tv_name_receive_message);
            tvTime = itemView.findViewById(R.id.tv_time_receive_message);
            ivAvatar = itemView.findViewById(R.id.iv_avatar_receive_message);
            cvAvatar = itemView.findViewById(R.id.cv_avatar_receive_message);
        }
    }
}
