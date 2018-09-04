package tattler.pro.tattler.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;

import java.sql.SQLException;

import tattler.pro.tattler.messages.ChatMessage;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Message;
import tattler.pro.tattler.tcp.TcpConnectionService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private TcpConnectionService connectionService;
    private DatabaseManager databaseManager;
    private MessageFactory messageFactory;

    public NotificationBroadcastReceiver(
            TcpConnectionService connectionService,
            DatabaseManager databaseManager,
            MessageFactory messageFactory) {
        this.connectionService = connectionService;
        this.databaseManager = databaseManager;
        this.messageFactory = messageFactory;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence message = getReplyMessage(intent);
        if (message == null) {
            return;
        }

        Chat chat = (Chat) intent.getSerializableExtra(IntentKey.CHAT.name());
        ChatMessage chatMessage = messageFactory.createChatMessage(chat, message.toString().getBytes());
        insertMessageToDb(chat, chatMessage);
        connectionService.sendMessage(chatMessage);
    }

    private void insertMessageToDb(Chat chat, ChatMessage chatMessage) {
        try {
            databaseManager.insertMessage(new Message(chatMessage, chat));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public IntentFilter createBroadcastIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentKey.BROADCAST_NOTIFICATION.name());
        return intentFilter;
    }

    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(NotificationBuilder.KEY_TEXT_REPLY);
        }
        return null;
    }
}
