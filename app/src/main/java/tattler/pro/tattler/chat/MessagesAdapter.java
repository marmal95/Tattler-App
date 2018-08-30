package tattler.pro.tattler.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

import tattler.pro.tattler.R;
import tattler.pro.tattler.chat.view_holders.MessageByMeViewHolder;
import tattler.pro.tattler.chat.view_holders.MessageToMeViewHolder;
import tattler.pro.tattler.chat.view_holders.MessageViewHolder;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Message;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Message> messages;

    MessagesAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ChatViewType.SENT_BY_ME:
                View viewByMe = layoutInflater.inflate(R.layout.recycler_view_message_by_me_item, parent, false);
                viewHolder = new MessageByMeViewHolder(context, viewByMe);
                break;
            case ChatViewType.SENT_TO_ME:
                View viewToMe = layoutInflater.inflate(R.layout.recycler_view_message_to_me_item, parent, false);
                viewHolder = new MessageToMeViewHolder(context, viewToMe);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
        messageViewHolder.setViewData(messages.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message == null) {
            return ChatViewType.NONE;
        }
        if (Util.isMessageSentByMe(context, message.senderId)) {
            return ChatViewType.SENT_BY_ME;
        } else {
            return ChatViewType.SENT_TO_ME;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public Message getMessage(int position) {
        return messages.get(position);
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void addMessages(Collection<Message> messages) {
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    private class ChatViewType {
        static final int NONE = -1;
        static final int SENT_TO_ME = 1;
        static final int SENT_BY_ME = 2;
    }

}