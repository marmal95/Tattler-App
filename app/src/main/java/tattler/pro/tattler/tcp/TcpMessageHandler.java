package tattler.pro.tattler.tcp;

import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.sql.SQLException;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.ChatInvitation;
import tattler.pro.tattler.messages.CreateChatResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;

public class TcpMessageHandler implements ReceivedMessageCallback {
    private TcpConnectionService tcpConnectionService;
    private AppPreferences appPreferences;
    private DatabaseManager databaseManager;
    private MessageFactory messageFactory;

    TcpMessageHandler(TcpConnectionService tcpConnectionService, AppPreferences appPreferences,
            DatabaseManager databaseManager, MessageFactory messageFactory) {
        this.tcpConnectionService = tcpConnectionService;
        this.appPreferences = appPreferences;
        this.databaseManager = databaseManager;
        this.messageFactory = messageFactory;
    }

    @Override
    public void onMessageReceived(Message message) {
        Logger.d("Received Message: " + message.toString());
        switch (message.messageType) {
            case Message.Type.LOGIN_RESPONSE:
                handleReceivedLoginResponse((LoginResponse) message);
                break;
            case Message.Type.ADD_CONTACT_RESPONSE:
                handleReceivedAddContactResponse((AddContactResponse) message);
                break;
            case Message.Type.CREATE_CHAT_RESPONSE:
                handleReceivedCreateChatResponse((CreateChatResponse) message);
                break;
            default:
                Logger.e("Message not handled: " + message.toString());
        }
    }

    private void handleReceivedLoginResponse(LoginResponse message) {
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

    private void handleReceivedCreateChatResponse(CreateChatResponse message) {
        try {
            if (message.status == CreateChatResponse.Status.CHAT_CREATED ||
                    message.status == CreateChatResponse.Status.CHAT_ALREADY_EXISTS) {
                Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName);
                databaseManager.insertChat(chat, message.contacts);

                // TODO: Handle properly chat invitation when chat exist and is reinitialized
                Invitation invitation = new Invitation(chat, Util.getMyUserNumber(tcpConnectionService));
                databaseManager.insertInvitation(invitation);

                sendChatInvitation(message);
            }
            broadcastMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendChatInvitation(CreateChatResponse message) {
        ChatInvitation chatInvitation = messageFactory.createChatInvitation(message);
        tcpConnectionService.sendMessage(chatInvitation);
    }

    private void broadcastMessage(Message message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }
}