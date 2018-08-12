package tattler.pro.tattler.chat.view_holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.models.Message;

public class MessageToMeViewHolder extends RecyclerView.ViewHolder implements MessageViewHolder {
    @BindView(R.id.messageTime)
    TextView messageTime;

    @BindView(R.id.messageContent)
    TextView messageContent;

    public MessageToMeViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setViewData(Message message) {
        messageTime.setText(message.getStringTime());
        switch (message.contentType) {
            case TEXT:
                messageContent.setText(new String(message.content));
                break;
        }
    }
}
