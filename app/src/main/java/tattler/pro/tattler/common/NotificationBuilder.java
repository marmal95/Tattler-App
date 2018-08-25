package tattler.pro.tattler.common;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;

import com.orhanobut.logger.Logger;

import java.sql.SQLException;

import tattler.pro.tattler.R;
import tattler.pro.tattler.chat.ChatActivity;
import tattler.pro.tattler.main.MainActivity;
import tattler.pro.tattler.messages.ChatMessage;
import tattler.pro.tattler.models.Chat;

public class NotificationBuilder extends ContextWrapper {
    public static final String KEY_TEXT_REPLY = "NOTIFICATION_TEXT_REPLY";
    public static String CHANNEL_ID = "PRIVATE_MESSAGES_CHANNEL_ID";
    private DatabaseManager databaseManager;

    public NotificationBuilder(Context context, DatabaseManager databaseManager) {
        super(context);
        this.databaseManager = databaseManager;
    }

    public Notification build(ChatMessage chatMessage) {
        try {
            return prepareNotification(chatMessage);
        } catch (SQLException e) {
            Logger.e("Exception occurred while building notification.\n" +
                    "The notification won't be displayed.");
            return null;
        }
    }

    private Notification prepareNotification(ChatMessage chatMessage) throws SQLException {
        Chat chat = databaseManager.selectChatById(chatMessage.chatId).get();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentText(getMessageContent(chatMessage));
        builder.setContentIntent(createNotificationIntent(chat));
        builder.setSmallIcon(R.drawable.ic_message);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setAutoCancel(true);
        builder.setColor(getColor(R.color.colorPrimary));
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.addAction(createReplyNotificationAction(
                createReplyPendingIntent(chatMessage, chat), createRemoveInput()));
        return builder.build();
    }

    private String getMessageContent(ChatMessage message) {
        switch (message.contentType) {
            case TEXT:
                return new String(message.content);
            case IMAGE:
                return getString(R.string.photo);
            default:
                return "";
        }
    }

    private PendingIntent createNotificationIntent(Chat chat) {
        Intent resultIntent = prepareChatActivityIntent(chat);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent prepareChatActivityIntent(Chat chat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(IntentKey.CHAT.name(), chat);
        return intent;
    }

    @NonNull
    private NotificationCompat.Action createReplyNotificationAction(PendingIntent replyPendingIntent, RemoteInput remoteInput) {
        return new NotificationCompat.Action.Builder(R.drawable.ic_send,
                "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }

    private PendingIntent createReplyPendingIntent(ChatMessage chatMessage, Chat chat) {
        Intent intent = new Intent(IntentKey.BROADCAST_NOTIFICATION.name());
        intent.putExtra(IntentKey.CHAT.name(), chat);

        return PendingIntent.getBroadcast(
                getApplicationContext(),
                chatMessage.chatId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    private RemoteInput createRemoveInput() {
        return new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel("Enter message...")
                .build();
    }
}
