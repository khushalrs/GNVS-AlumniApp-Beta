package com.GNVS.AlumniApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = 2;
    private Context mContext;
    private ArrayList<MessageList> mMessage;
    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    public int getItemViewType(int position){
        if (Objects.equals(mMessage.get(position).getEmail(), email)){
            return 1;
        }
        else {
            return 2;
        }
    }

    public MessageAdapter(Context context, ArrayList<MessageList> list){
        mContext = context;
        mMessage = list;
    }

    public MessageAdapter(){}

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
            return new SentMessageHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageList message = mMessage.get(position);
        //Log.i("Adapter", Integer.toString(holder.getItemViewType()));
        switch (holder.getItemViewType()) {
            case MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }

    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText;
        SentMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_me);
            timeText = itemView.findViewById(R.id.text_timestamp_me);
        }
        void bind(MessageList message){
            messageText.setText(message.getMessageText());
            timeText.setText((message.getTime()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText, nameText;
        ReceivedMessageHolder(View itemView){
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_other);
            timeText = itemView.findViewById(R.id.text_timestamp_other);
            nameText = itemView.findViewById(R.id.text_user_other);
        }
        void bind(MessageList message){
            //Log.i("Message", message.getMessageText());
            messageText.setText(message.getMessageText());
            timeText.setText((message.getTime()));
            nameText.setText(message.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }
}
