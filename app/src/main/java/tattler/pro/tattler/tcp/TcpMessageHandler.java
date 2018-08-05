package tattler.pro.tattler.tcp;

import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.sql.SQLException;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.ChatInvitation;
import tattler.pro.tattler.messages.ChatInvitationResponse;
import tattler.pro.tattler.messages.CreateChatResponse;
import tattler.pro.tattler.messages.InitializeChatIndication;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;
import tattler.pro.tattler.security.AesCrypto;
import tattler.pro.tattler.security.RsaCrypto;

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
            case Message.Type.CHAT_INVITATION:
                handleChatInvitation((ChatInvitation) message);
                break;
            case Message.Type.CHAT_INVITATION_RESPONSE:
                handleChatInvitationResponse((ChatInvitationResponse) message);
                break;
            case Message.Type.INITIALIZE_CHAT_INDICATION:
                handleInitializeChatIndication((InitializeChatIndication) message);
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
                Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
                chat.chatName = chat.chatName == null ? Util.generateChatName(message.contacts) : chat.chatName;
                chat.chatKey = new AesCrypto().generateAesKey();
                chat.isInitialized = true;
                databaseManager.insertChat(chat, message.contacts);

                // TODO: Handle properly chat invitation when chat exist and is reinitialized
                ChatInvitation chatInvitation = messageFactory.createChatInvitation(message);
                Invitation invitation = new Invitation(chat, Util.getMyUserNumber(tcpConnectionService),
                        chatInvitation.messageId, Invitation.State.PENDING_FOR_RESPONSE);

                databaseManager.insertInvitation(invitation);
                tcpConnectionService.sendMessage(chatInvitation);
            }
            broadcastMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleChatInvitation(ChatInvitation message) {
        try {
            KeyPair rsaKeyPair = new RsaCrypto().generateRsaKeyPair();

            Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
            chat.chatName = chat.chatName == null ? Util.generateChatName(message.chatContacts) : chat.chatName;
            chat.publicKey = rsaKeyPair.getPublic().getEncoded();
            chat.privateKey = rsaKeyPair.getPrivate().getEncoded();
            databaseManager.insertChat(chat, message.chatContacts);

            Invitation invitation = new Invitation(
                    chat, message.senderId, message.messageId, Invitation.State.PENDING_FOR_REACTION);
            databaseManager.insertInvitation(invitation);
            broadcastMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleChatInvitationResponse(ChatInvitationResponse message) {
        try {
            Chat chat = databaseManager.selectChatById(message.chatId);
            InitializeChatIndication initChatInd = messageFactory.createInitializeChatIndication(
                    message, chat, new RsaCrypto());
            tcpConnectionService.sendMessage(initChatInd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInitializeChatIndication(InitializeChatIndication message) {
        try {
            Chat chat = databaseManager.selectChatById(message.chatId);

            RsaCrypto rsaCrypto = new RsaCrypto();
            byte[] chatKey = rsaCrypto.decrypt(message.chatEncryptedKey, chat.privateKey);

            AesCrypto aesCrypto = new AesCrypto();
            aesCrypto.init(chatKey);

            chat.chatKey = chatKey;
            chat.isInitialized = true;

            databaseManager.updateChat(chat);
        } catch (SQLException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }


    private void broadcastMessage(Message message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }
}