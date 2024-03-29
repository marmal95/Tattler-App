package tattler.pro.tattler.tcp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.NotificationBuilder;
import tattler.pro.tattler.common.Util;
import tattler.pro.tattler.internal_messages.ChatsUpdate;
import tattler.pro.tattler.internal_messages.ContactsUpdate;
import tattler.pro.tattler.internal_messages.InternalMessage;
import tattler.pro.tattler.internal_messages.InvitationsUpdate;
import tattler.pro.tattler.internal_messages.MessagesUpdate;
import tattler.pro.tattler.internal_messages.UserInfoUpdate;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.ChatInvitation;
import tattler.pro.tattler.messages.ChatInvitationResponse;
import tattler.pro.tattler.messages.ChatMessage;
import tattler.pro.tattler.messages.CreateChatResponse;
import tattler.pro.tattler.messages.InitializeChatIndication;
import tattler.pro.tattler.messages.LeaveChatIndication;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.messages.OnlineContactsIndication;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;
import tattler.pro.tattler.security.AesCrypto;
import tattler.pro.tattler.security.RsaCrypto;

public class TcpMessageHandler {
    private TcpConnectionService tcpConnectionService;
    private AppPreferences appPreferences;
    private DatabaseManager databaseManager;
    private MessageFactory messageFactory;
    private NotificationBuilder notificationBuilder;
    private NotificationManager notificationManager;
    private ChatsManager chatsManager;

    TcpMessageHandler(
            TcpConnectionService tcpConnectionService,
            AppPreferences appPreferences,
            DatabaseManager databaseManager,
            MessageFactory messageFactory,
            NotificationBuilder notificationBuilder,
            NotificationManager notificationManager,
            ChatsManager chatsManager) {
        this.tcpConnectionService = tcpConnectionService;
        this.appPreferences = appPreferences;
        this.databaseManager = databaseManager;
        this.messageFactory = messageFactory;
        this.notificationBuilder = notificationBuilder;
        this.notificationManager = notificationManager;
        this.chatsManager = chatsManager;
    }

    public void handle(Message message) {
        Logger.d("Handling Message: " + message.toString());
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
            case Message.Type.CHAT_MESSAGE:
                handleChatMessage((ChatMessage) message);
                break;
            case Message.Type.LEAVE_CHAT_INDICATION:
                handleLeaveChatIndication((LeaveChatIndication) message);
                break;
            case Message.Type.ONLINE_CONTACTS_INDICATION:
                handleOnlineContactsIndication((OnlineContactsIndication) message);
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

                UserInfoUpdate userInfoUpdate = new UserInfoUpdate();
                userInfoUpdate.userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
                userInfoUpdate.userPhoneId = message.phoneId;
                userInfoUpdate.reason = UserInfoUpdate.Reason.LOGIN_SUCCESSFUL;
                broadcastMessage(userInfoUpdate);

                if (!message.contacts.isEmpty()) {
                    Logger.d("Received contacts from Server, size: " + message.contacts.size());

                    ContactsUpdate contactsUpdate = new ContactsUpdate();
                    contactsUpdate.contacts = databaseManager.updateContacts(message.contacts);
                    contactsUpdate.reason = ContactsUpdate.Reason.ALL_CONTACTS_UPDATE;

                    broadcastMessage(contactsUpdate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleAddContactResponse(AddContactResponse message) {
        try {
            ContactsUpdate contactsUpdate = new ContactsUpdate();

            if (message.status == AddContactResponse.Status.CONTACT_ADDED) {
                Contact contact = new Contact(message.userName, message.userNumber);
                databaseManager.insertContact(contact);
                contactsUpdate.reason = ContactsUpdate.Reason.NEW_CONTACT_ADDED;
                contactsUpdate.contacts.add(contact);
            } else if (message.status == AddContactResponse.Status.CONTACT_ALREADY_ADDED) {
                Contact contact = new Contact(message.userName, message.userNumber);
                databaseManager.insertContact(contact);
                contactsUpdate.reason = ContactsUpdate.Reason.CONTACT_ALREADY_ADDED;
                contactsUpdate.contacts.add(contact);
            } else if (message.status == AddContactResponse.Status.CONTACT_NOT_EXIST) {
                contactsUpdate.reason = ContactsUpdate.Reason.CONTACT_NOT_EXIST;
            }

            broadcastMessage(contactsUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCreateChatResponse(CreateChatResponse message) {
        try {
            if (message.status == CreateChatResponse.Status.CHAT_CREATED) {
                Chat chat = prepareChat(message);
                databaseManager.insertChat(chat, message.contacts);

                List<ChatInvitation> chatInvitations = messageFactory.createChatInvitations(message);
                chatInvitations.forEach(chatInvitation -> {
                    Invitation invitation = prepareInvitationByMe(chat, chatInvitation);
                    chatsManager.addInvitation(chat, invitation);
                    tcpConnectionService.sendMessage(chatInvitation);
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleChatInvitation(ChatInvitation message) {
        try {
            KeyPair rsaKeyPair = new RsaCrypto().generateRsaKeyPair();
            Chat chat = prepareChat(message, rsaKeyPair);
            databaseManager.insertChat(chat, message.chatContacts);

            Invitation invitation = prepareInvitationToMe(chat, message);
            chat.invitations.add(invitation);
            databaseManager.updateChat(chat);

            InvitationsUpdate invitationsUpdate = new InvitationsUpdate();
            invitationsUpdate.invitations.add(invitation);
            invitationsUpdate.reason = InvitationsUpdate.Reason.NEW_INVITATION;
            broadcastMessage(invitationsUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleChatInvitationResponse(ChatInvitationResponse message) {
        try {
            Optional<Chat> optionalChat = databaseManager.selectChatById(message.chatId);
            if (!optionalChat.isPresent()) {
                Logger.e("Received ChatInvitationResponse for not existing chat.");
                return;
            }

            Chat chat = optionalChat.get();
            if (message.status == ChatInvitationResponse.Status.INVITATION_ACCEPTED) {
                handleInvitationAccepted(message, chat);
            } else if (message.status == ChatInvitationResponse.Status.INVITATION_REJECTED) {
                handleInvitationRejected(message, chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleInvitationAccepted(ChatInvitationResponse message, Chat chat) throws Exception {
        InitializeChatIndication initChatInd = messageFactory.createInitializeChatIndication(
                message, chat, new RsaCrypto());
        tcpConnectionService.sendMessage(initChatInd);

        List<Invitation> invitationsToRemove = chat.invitations.stream().filter(
                invitation -> invitation.receiverId == message.senderId).
                collect(Collectors.toList());
        broadcastInvitationsRemoveUpdate(invitationsToRemove);

        chat.isInitialized = true;
        chat.invitations.removeAll(invitationsToRemove);
        databaseManager.updateChat(chat);
        broadcastChatModifiedUpdate(chat);
    }

    private void handleInvitationRejected(ChatInvitationResponse message, Chat chat) {
        chat.participants.removeIf(participant -> participant.contactNumber == message.senderId);
        if (chat.participants.size() < 2) {
            chatsManager.removeChat(chat);
            broadcastChatRemoveUpdate(chat);
        }
    }

    private void handleInitializeChatIndication(InitializeChatIndication message) {
        try {
            Optional<Chat> optionalChat = databaseManager.selectChatById(message.chatId);
            if (optionalChat.isPresent()) {
                Chat chat = optionalChat.get();
                RsaCrypto rsaCrypto = new RsaCrypto();

                chat.chatKey = rsaCrypto.decrypt(message.chatEncryptedKey, chat.privateKey);
                chat.isInitialized = true;
                databaseManager.updateChat(chat);

                List<Invitation> invitationsToRemove = chat.invitations.stream().filter(
                        invitation -> invitation.senderId == message.senderId).
                        collect(Collectors.toList());
                broadcastInvitationsRemoveUpdate(invitationsToRemove);
                broadcastChatModifiedUpdate(chat);

                chat.invitations.removeAll(invitationsToRemove);
                databaseManager.updateChat(chat);
            }
        } catch (SQLException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private void handleChatMessage(ChatMessage message) {
        try {
            Optional<Chat> optionalChat = databaseManager.selectChatById(message.chatId);
            if (optionalChat.isPresent()) {
                Chat chat = optionalChat.get();
                if (chat.isBlocked) {
                    Logger.d("Chat is blocked. Ignoring message: " + message.toString());
                    return;
                }

                tattler.pro.tattler.models.Message dbMessage =
                        new tattler.pro.tattler.models.Message(message, chat);
                databaseManager.insertMessage(dbMessage);

                MessagesUpdate messagesUpdate = new MessagesUpdate();
                messagesUpdate.reason = MessagesUpdate.Reason.NEW_MESSAGE_RECEIVED;
                messagesUpdate.messages.add(dbMessage);
                broadcastMessage(messagesUpdate);

                sendNotificationWhenEnabled(chat, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleLeaveChatIndication(LeaveChatIndication message) {
        try {
            Optional<Chat> optionalChat = databaseManager.selectChatById(message.chatId);
            if (optionalChat.isPresent()) {
                Chat chat = optionalChat.get();
                chat.participants.removeIf(participant ->
                        participant.contactNumber == message.leavingUserNumber);
                if (chat.participants.isEmpty()) {
                    chat.isInitialized = false;
                    broadcastChatModifiedUpdate(chat);
                }
                databaseManager.updateChat(chat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleOnlineContactsIndication(OnlineContactsIndication message) {
        ContactsUpdate contactsUpdate = new ContactsUpdate();
        contactsUpdate.reason = ContactsUpdate.Reason.ONLINE_STATUS_UPDATE;
        message.contactNumbers.forEach(contactNumber ->
                contactsUpdate.contacts.add(new Contact(null, contactNumber)));
        broadcastMessage(contactsUpdate);
    }

    @NonNull
    private Chat prepareChat(CreateChatResponse message) {
        Chat chat = new Chat(message.chatId, message.isGroupChat, message.chatName, false);
        chat.chatName = chat.chatName == null ? Util.generateChatName(message.contacts) : chat.chatName;
        chat.chatKey = new AesCrypto().generateAesKey();
        chat.isInitialized = false;
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
    private Invitation prepareInvitationToMe(Chat chat, ChatInvitation message) {
        return new Invitation(
                chat, message.senderId, message.receiverId, message.messageId, Invitation.State.PENDING_FOR_REACTION);
    }

    @NonNull
    private Invitation prepareInvitationByMe(Chat chat, ChatInvitation chatInvitation) {
        return new Invitation(chat, chatInvitation.senderId, chatInvitation.receiverId,
                chatInvitation.messageId, Invitation.State.PENDING_FOR_RESPONSE);
    }

    private void broadcastMessage(InternalMessage message) {
        Intent intent = new Intent(IntentKey.BROADCAST_MESSAGE.name());
        intent.putExtra(IntentKey.MESSAGE.name(), message);
        tcpConnectionService.sendBroadcast(intent);
    }

    private void broadcastChatModifiedUpdate(Chat chat) {
        ChatsUpdate chatsUpdate = new ChatsUpdate();
        chatsUpdate.reason = ChatsUpdate.Reason.CHAT_MODIFIED;
        chatsUpdate.chats.add(chat);
        broadcastMessage(chatsUpdate);
    }

    private void broadcastInvitationsRemoveUpdate(List<Invitation> invitations) {
        InvitationsUpdate invitationsUpdate = new InvitationsUpdate();
        invitationsUpdate.reason = InvitationsUpdate.Reason.CHAT_INITIALIZED;
        invitationsUpdate.invitations = invitations;
        broadcastMessage(invitationsUpdate);
    }

    private void broadcastChatRemoveUpdate(Chat chat) {
        ChatsUpdate chatsUpdate = new ChatsUpdate();
        chatsUpdate.reason = ChatsUpdate.Reason.CHAT_REMOVED;
        chatsUpdate.chats.add(chat);
        broadcastMessage(chatsUpdate);
    }

    private void sendNotificationWhenEnabled(Chat chat, ChatMessage message) {
        boolean isNotificationEnabled = appPreferences.getBoolean(AppPreferences.Key.IS_NOTIFICATION_ON);
        if (isNotificationEnabled && !chat.isMuted) {
            Notification notification = notificationBuilder.build(message);
            notificationManager.notify(chat.chatId, notification);
        }
    }
}