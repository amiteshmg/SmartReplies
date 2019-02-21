package com.example.smartrepliesdemo;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MessageItemViewHolder>
{

    private List<String> messageList;
    private String mUserId;

    public ChatRecyclerAdapter(String messages)
    {
        if(messageList==null)
        {
            messageList=new ArrayList<>();
        }

        messageList.add(messages);
        notifyItemInserted(messageList.size()-1);
    }



    private List<String> getMessageList() {
        return this.messageList;
    }



    public ChatRecyclerAdapter(final List<String> messages, final String userId) {
        this.messageList = messages;
        this.mUserId = userId;
    }



    @Override
    public MessageItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.chat_item, viewGroup, false);
        return new MessageItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageItemViewHolder holder, int position) {
        String message = messageList.get(position);

        //final boolean isMe = messa.equals(mUserId);
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
    /*    if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        }*/

   /*     else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }*/
     /*   final SimpleDraweeView profileView = isMe ? holder.imageRight : holder.imageLeft;

        profileView.setImageURI(Uri.parse(getProfileUrl(mUserId)));
        holder.body.setText(message.getBody());*/

     holder.body.setText(message);

    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }


    @Override
    public int getItemCount() {
        return this.messageList.size();
    }

    public class MessageItemViewHolder extends RecyclerView.ViewHolder
    {
       /* public SimpleDraweeView imageLeft;
        public SimpleDraweeView imageRight;*/
        public TextView body;

        public MessageItemViewHolder(View itemView)
        {
            super(itemView);
           /* imageLeft = (SimpleDraweeView) itemView.findViewById(R.id.ivProfileLeft);
            imageRight = (SimpleDraweeView) itemView.findViewById(R.id.ivProfileRight);*/
            body = itemView.findViewById(R.id.message_text_view);
        }


    }

    // This method is used to update data for adapter and notify adapter that data has changed
    public void updateList(List<String> data) {
        messageList = data;
        notifyDataSetChanged();
    }

    public void addMessage(String message)
    {
        messageList.add(message);
        notifyItemInserted(messageList.size()-1);
    }
}