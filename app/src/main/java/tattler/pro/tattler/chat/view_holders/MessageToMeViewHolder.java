package tattler.pro.tattler.chat.view_holders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.models.Message;
import tattler.pro.tattler.models.Participant;

public class MessageToMeViewHolder extends RecyclerView.ViewHolder implements MessageViewHolder {
    @BindView(R.id.userAvatar)
    CircleImageView userAvatar;

    @BindView(R.id.userInitials)
    TextView userInitials;

    @BindView(R.id.messageTime)
    TextView messageTime;

    @BindView(R.id.messageContent)
    TextView messageContent;

    private Context context;

    public MessageToMeViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setViewData(Message message) {
        setUserAvatarData(message);
        setMessageData(message);
    }

    private void setUserAvatarData(Message message) {
        Optional<Participant> optSenderUser =
                message.chat.participants.stream().filter(
                        participant -> participant.contactNumber == message.senderId).findFirst();
        if (optSenderUser.isPresent()) {
            Participant senderUser = optSenderUser.get();
            ColorDrawable userNameColorDrawable = new ColorDrawable(Color.parseColor(Util.pickHexColor(context, senderUser.contactName)));
            userAvatar.setImageDrawable(userNameColorDrawable);
            userInitials.setText(Util.extractUserInitials(senderUser.contactName));
        }
    }

    private void setMessageData(Message message) {
        messageTime.setText(message.getStringTime());
        switch (message.contentType) {
            case TEXT:
                messageContent.setText(new String(message.content));
                break;
        }
    }
}
