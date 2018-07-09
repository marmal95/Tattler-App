package tattler.pro.tattler.tcp;

import android.content.Intent;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;

public class MessageHandler {
    private TcpConnectionService tcpConnectionService;
    private AppPreferences appPreferences;
    private DatabaseManager databaseManager;

    MessageHandler(TcpConnectionService tcpConnectionService, AppPreferences appPreferences, DatabaseManager databaseManager) {
        this.tcpConnectionService = tcpConnectionService;
        this.appPreferences = appPreferences;
        this.databaseManager = databaseManager;
    }

    public void handleReceivedMessage(Message message) {
        switch (message.messageType) {
            case Message.Type.LOGIN_RESPONSE:
                handleReceivedLoginResponse((LoginResponse) message);
                break;
            case Message.Type.ADD_CONTACT_RESPONSE:
                handleReceivedAddContactResponse((AddContactResponse) message);
                break;
        }
    }

    private void handleReceivedLoginResponse(LoginResponse message) {
        Logger.d("Received Message: " + message.toString());
        appPreferences.put(AppPreferences.Key.USER_NUMBER, message.phoneId);
    }

    private void handleReceivedAddContactResponse(AddContactResponse message) {
        Logger.d("Received Message: " + message.toString());
        broadcastMessage(message);
    }

    private void broadcastMessage(Message message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }
}