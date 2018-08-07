package tattler.pro.tattler.tcp;

import android.content.Intent;
import android.support.annotation.NonNull;

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
                handleLoginResponse((LoginResponse) message);
                break;
            case Message.Type.ADD_CONTACT_RESPONSE:
                handleAddContactResponse((AddContactResponse) message);
                break;
            case Message.Type.CREATE_CHAT_RESPONSE:
                handleCreateChatResponse((CreateChatResponse) message);
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

    private void handleLoginResponse(LoginResponse message) {
        try {
            if (message.status == LoginResponse.Status.LOGIN_SUCCESSFUL) {
                appPreferences.put(AppPreferences.Key.USER_NUMBER, message.phoneId);
                appPreferences.put(AppPreferences.Key.IS_FIRST_LAUNCH, false);

                if (!message.contacts.isEmpty()) {
                    Logger.d("Received contacts from Server, size: " + message.contacts.size());
                    databaseManager.updateContacts(message.contacts);
                }
                broadcastMessage(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleAddContactResponse(AddContactResponse message) {
        try {
            if (message.status == AddContactResponse.Status.CONTACT_ADDED ||
                    message.status == AddContactResponse.Status.CONTACT_ALREADY_ADDED) {
                Contact contact = new Contact(message.userName, message.userNumber);
                databaseManager.insertContact(contact);
            }
            broadcastMessage(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateChatResponse(CreateChatResponse message) {
        try {
            if (message.status == CreateChatResponse.Status.CHAT_CREATED ||
                    message.status == CreateChatResponse.Status.CHAT_ALREADY_EXISTS) {
                Chat chat = prepareChat(message);
                databaseManager.insertChat(chat, message.contacts);

                ChatInvitation chatInvitation = messageFactory.createChatInvitation(message);
                Invitation invitation = prepareInvitation(message, chat, chatInvitation);

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
            Chat chat = prepareChat(message, rsaKeyPair);
            databaseManager.insertChat(chat, message.chatContacts);

            Invitation invitation = prepareInvitation(message, chat);
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

            chat.chatKey = rsaCrypto.decrypt(message.chatEncryptedKey, chat.privateKey);
            chat.isInitialized = true;

            databaseManager.updateChat(chat);
        } catch (SQLException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private Chat prepareChat(CreateChatResponse message) {
        Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
        chat.chatName = chat.chatName == null ? Util.generateChatName(message.contacts) : chat.chatName;
        chat.chatKey = new AesCrypto().generateAesKey();
        chat.isInitialized = true;
        return chat;
    }

    @NonNull
    private Chat prepareChat(ChatInvitation message, KeyPair rsaKeyPair) {
        Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
        chat.chatName = chat.chatName == null ? Util.generateChatName(message.chatContacts) : chat.chatName;
        chat.publicKey = rsaKeyPair.getPublic().getEncoded();
        chat.privateKey = rsaKeyPair.getPrivate().getEncoded();
        return chat;
    }

    @NonNull
    private Invitation prepareInvitation(ChatInvitation message, Chat chat) {
        return new Invitation(
                chat, message.senderId, message.messageId, Invitation.State.PENDING_FOR_REACTION);
    }

    @NonNull
    private Invitation prepareInvitation(CreateChatResponse message, Chat chat, ChatInvitation chatInvitation) {
        return new Invitation(chat, message.receiverId,
                chatInvitation.messageId, Invitation.State.PENDING_FOR_RESPONSE);
    }

    private void broadcastMessage(Message message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }
}