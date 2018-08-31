package tattler.pro.tattler.chat.view_holders;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tattler.pro.tattler.R;
import tattler.pro.tattler.common.OnItemClickListener;
import tattler.pro.tattler.models.Message;

public class MessageByMeViewHolder extends RecyclerView.ViewHolder implements MessageViewHolder {
    @BindView(R.id.messageTime)
    TextView messageTime;

    @BindView(R.id.messageContent)
    TextView messageContent;

    @BindView(R.id.messageImage)
    ImageView messageImage;

    public MessageByMeViewHolder(Context context, View itemView, OnItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(v -> listener.onItemClick(getAdapterPosition()));
    }

    @Override
    public void setViewData(Message message) {
        messageTime.setText(message.getStringTime());
        switch (message.contentType) {
            case TEXT:
                messageContent.setText(new String(message.content));
                messageContent.setVisibility(View.VISIBLE);
                messageImage.setVisibility(View.GONE);
                break;
            case PHOTO:
                Bitmap bitmap = BitmapFactory.decodeByteArray(message.content, 0, message.content.length);
                messageImage.setImageBitmap(bitmap);
                messageImage.setVisibility(View.VISIBLE);
                messageContent.setVisibility(View.GONE);
                break;
        }
    }
}
