package tattler.pro.tattler.tcp;

import android.content.Intent;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.models.Contact;

import java.sql.SQLException;

public class TcpMessageHandler implements ReceivedMessageCallback {
    private TcpConnectionService tcpConnectionService;
    private AppPreferences appPreferences;
    private DatabaseManager databaseManager;

    TcpMessageHandler(TcpConnectionService tcpConnectionService, AppPreferences appPreferences, DatabaseManager databaseManager) {
        this.tcpConnectionService = tcpConnectionService;
        this.appPreferences = appPreferences;
        this.databaseManager = databaseManager;
    }

    @Override
    public void onMessageReceived(Message message) {
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
        if (message.status == LoginResponse.Status.LOGIN_SUCCESSFUL) {
            appPreferences.put(AppPreferences.Key.USER_NUMBER, message.phoneId);
            appPreferences.put(AppPreferences.Key.IS_FIRST_LAUNCH, false);

            if (!message.contacts.isEmpty()) {
                Logger.d("Received contacts from Server, size: " + message.contacts.size());
                databaseManager.updateContacts(message.contacts);
            }

            broadcastMessage(message);
        }
    }

    private void handleReceivedAddContactResponse(AddContactResponse message) {
        Logger.d("Received Message: " + message.toString());
        try {
            if (message.status == AddContactResponse.Status.CONTACT_ADDED) {
                Contact contact = new Contact(message.userName, message.userNumber);
                databaseManager.insertContact(contact);
            }
            broadcastMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void broadcastMessage(Message message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }
}