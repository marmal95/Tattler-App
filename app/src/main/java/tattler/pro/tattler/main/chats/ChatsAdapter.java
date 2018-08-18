package tattler.pro.tattler.main.chats;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.common.SelectableAdapter;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Message;

public class ChatsAdapter extends SelectableAdapter<ChatsAdapter.ViewHolder, Chat> {
    private Context context;
    private List<Chat> chats;
    private OnItemClickListener clickListener;

    private int lastPosition;

    ChatsAdapter(Context context, List<Chat> chats, OnItemClickListener clickListener) {
        this.context = context;
        this.chats = chats;
        this.clickListener = clickListener;
        this.lastPosition = -1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = getChat(position);
        holder.chatName.setText(chat.chatName);
        holder.chatAvatar.setColorFilter(Color.parseColor(Util.pickHexColor(context, chat.chatName)));

        if (isSelected(position)) {
            holder.chatInitials.setText("");
            holder.chatAvatar.setImageDrawable(VectorDrawableCompat.create(context.getResources(),
                    R.drawable.ic_check_circle, context.getTheme()));
        } else {
            ColorDrawable chatNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(context, chat.chatName)));
            holder.chatAvatar.setImageDrawable(chatNameColorDrawable);
            holder.chatInitials.setText(Util.extractUserInitials(chat.chatName));
        }

        if (chat.messages != null) {
            displayLastMessageData(holder, chat);
        }

        setAnimation(holder.itemView, position);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public List<Chat> getSelectedItems() {
        List<Chat> selectedChats = new ArrayList<>(getSelectedItemsCount());
        getSelectedPositions().forEach(index -> selectedChats.add(getChat(index)));
        return selectedChats;
    }

    public void addChat(Chat chat) {
        chats.add(chat);
        notifyDataSetChanged();
    }

    public void addChats(List<Chat> chats) {
        this.chats.addAll(chats);
        notifyDataSetChanged();
    }

    public void clearChats() {
        chats.clear();
        notifyDataSetChanged();
    }

    public void removeChat(int position) {
        chats.remove(position);
        notifyItemRemoved(position);
    }

    public void removeChat(Chat chat) {
        OptionalInt indexOpt = IntStream.range(0, chats.size()).filter(
                index -> chat.chatId == chats.get(index).chatId).findFirst();

        if (indexOpt.isPresent()) {
            removeChat(indexOpt.getAsInt());
        }
    }

    public Chat getChat(int position) {
        return chats.get(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void displayLastMessageData(@NonNull ViewHolder holder, Chat chat) {
        List<Message> chatMessages = new ArrayList<>(chat.messages);
        if (chatMessages.isEmpty())
            return;

        Message lastMessage = chatMessages.get(chatMessages.size() - 1);
        String lastMessageContent = Util.isMessageSentByMe(context, lastMessage.senderId) ?
                context.getString(R.string.lastMessageByMe, new String(lastMessage.content)) :
                context.getString(R.string.lastMessageToMe, new String(lastMessage.content));
        holder.chatLastMessage.setText(lastMessageContent);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatAvatar)
        CircleImageView chatAvatar;

        @BindView(R.id.chatInitials)
        TextView chatInitials;

        @BindView(R.id.chatName)
        TextView chatName;

        @BindView(R.id.chatLastMessage)
        TextView chatLastMessage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> clickListener.onItemClick(getAdapterPosition()));
            itemView.setOnLongClickListener(v -> clickListener.onItemLongClick(getAdapterPosition()));
        }
    }
}
