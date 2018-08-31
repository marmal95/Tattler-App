package tattler.pro.tattler.chat;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.util.List;

import tattler.pro.tattler.common.ChatsManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.common.PickedImageView;
import tattler.pro.tattler.messages.ChatMessage;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Message;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class ChatPresenter extends MvpBasePresenter<ChatView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;

    private ChatMessageHandler messageHandler;
    private MessageBroadcastReceiver broadcastReceiver;

    private Chat chat;
    private MessagesAdapter messagesAdapter;
    private MessageFactory messageFactory;
    private ChatsManager chatsManager;

    ChatPresenter(
            TcpServiceManager tcpManager,
            TcpServiceConnectorFactory serviceConnectorFactory,
            ChatMessageHandler messageHandler,
            MessageBroadcastReceiver broadcastReceiver,
            MessagesAdapter messagesAdapter,
            MessageFactory messageFactory,
            Chat chat,
            DatabaseManager databaseManager,
            ChatsManager chatsManager) {
        this.tcpServiceManager = tcpManager;
        this.tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
        this.messageHandler = messageHandler;
        this.broadcastReceiver = broadcastReceiver;
        this.messagesAdapter = messagesAdapter;
        this.messageFactory = messageFactory;
        this.chat = chat;

        this.messageHandler.setPresenter(this);
        this.broadcastReceiver.setReceivedMessageCallback(messageHandler);
        this.chatsManager = chatsManager;
        this.chatsManager.setDatabaseManager(databaseManager);
    }

    @SuppressWarnings("ConstantConditions")
    public void onCreate() {
        if (isViewAttached()) {
            getView().setMessagesAdapter(messagesAdapter);
            getView().setTitle(chat.chatName);
        }

        if (isChatNotReady() && isViewAttached()) {
            getView().disableChat();
        }

        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }
        registerReceiver();
        messagesAdapter.addMessages(chat.messages);
    }

    void onDestroy() {
        OpenHelperManager.releaseHelper();
        if (tcpServiceManager.isServiceBound()) {
            unbindTcpConnectionService();
        }
        unregisterReceiver();
    }

    public void handleMessagesReceived(List<Message> messages) {
        messagesAdapter.addMessages(messages);
        scrollMessagesBottom();
    }

    public void handleSendMessage(String messageText) {
        if (!messageText.isEmpty()) {
            ChatMessage chatMessage = messageFactory.createChatMessage(chat, messageText.getBytes());
            tcpServiceManager.getTcpService().sendMessage(chatMessage);

            Message dbMessage = new Message(chatMessage, chat);
            messagesAdapter.addMessage(dbMessage);
            insertMessageToDb(dbMessage);
            scrollMessagesBottom();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void handleSendAttachment() {
        if (isViewAttached()) {
            getView().pickImage();
        }
    }

    public void onImagePicked(PickedImageView pickedImageView) {
        ChatMessage chatMessage = messageFactory.createChatMessage(chat, pickedImageView);
        tcpServiceManager.getTcpService().sendMessage(chatMessage);

        Message dbMessage = new Message(chatMessage, chat);
        messagesAdapter.addMessage(dbMessage);
        insertMessageToDb(dbMessage);
        scrollMessagesBottom();
    }

    public void handleMessageClick(int position) {
        Message message = messagesAdapter.getMessage(position);
        if (message.contentType == Message.ContentType.PHOTO) {
            showImagePreview(message);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void bindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Binding TcpConnectionService.");
            getView().bindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void unbindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Unbinding TcpConnectionService.");
            getView().unbindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerReceiver() {
        if (isViewAttached()) {
            Logger.d("Registering BroadcastReceiver.");
            getView().registerReceiver(broadcastReceiver);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void unregisterReceiver() {
        if (isViewAttached()) {
            Logger.d("Unregistering BroadcastReceiver.");
            getView().unregisterReceiver(broadcastReceiver);
        }
    }

    private void insertMessageToDb(Message message) {
        chatsManager.addMessage(chat, message);
    }

    @SuppressWarnings("ConstantConditions")
    private void scrollMessagesBottom() {
        if (isViewAttached()) {
            getView().scrollToPosition(messagesAdapter.getItemCount() - 1);
        }
    }

    private boolean isChatNotReady() {
        return !chat.isInitialized || chat.isBlocked;
    }

    @SuppressWarnings("ConstantConditions")
    private void showImagePreview(Message message) {
        if (isViewAttached()) {
            getView().showImagePreview(message);
        }
    }
}
