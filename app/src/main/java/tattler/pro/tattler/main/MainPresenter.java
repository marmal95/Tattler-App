package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.main.chats.ChatsPresenter;
import tattler.pro.tattler.main.contacts.ContactsPresenter;
import tattler.pro.tattler.main.invitations.InvitationsPresenter;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.ChatInvitation;
import tattler.pro.tattler.messages.CreateChatResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class MainPresenter extends MvpBasePresenter<MainView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;

    private MainMessageHandler messageHandler;
    private MessageBroadcastReceiver broadcastReceiver;
    private AppPreferences appPreferences;

    private ContactsPresenter contactsPresenter;
    private ChatsPresenter chatsPresenter;
    private InvitationsPresenter invitationsPresenter;

    MainPresenter(TcpServiceManager tcpManager,
            TcpServiceConnectorFactory serviceConnectorFactory,
            MainMessageHandler messageHandler,
            MessageBroadcastReceiver broadcastReceiver,
            AppPreferences appPreferences) {
        this.tcpServiceManager = tcpManager;
        this.tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
        this.appPreferences = appPreferences;
        this.messageHandler = messageHandler;
        this.messageHandler.setPresenter(this);
        this.broadcastReceiver = broadcastReceiver;
        this.broadcastReceiver.setReceivedMessageCallback(messageHandler);
    }

    public void setContactsPresenter(ContactsPresenter contactsPresenter) {
        this.contactsPresenter = contactsPresenter;
    }

    public void setChatsPresenter(ChatsPresenter chatsPresenter) {
        this.chatsPresenter = chatsPresenter;
    }

    public void setInvitationsPresenter(InvitationsPresenter invitationsPresenter) {
        this.invitationsPresenter = invitationsPresenter;
    }

    public void onCreate() {
        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }

        displayUserData();
        registerReceiver();
    }

    public void onDestroy() {
        if (tcpServiceManager.isServiceBound()) {
            unbindTcpConnectionService();
        }

        unregisterReceiver();
    }

    @SuppressWarnings("ConstantConditions")
    public void handleNavContactsClick() {
        if (isViewAttached()) {
            getView().startContactsFragment();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void handleNavChatsClick() {
        if (isViewAttached()) {
            getView().startChatsFragment();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void handleNavInvitationsClick() {
        if (isViewAttached()) {
            getView().startInvitationsFragment();
        }
    }

    public void sendMessage(Message message) {
        tcpServiceManager.getTcpService().sendMessage(message);
    }

    public void handleLoginResponse(LoginResponse loginResponse) {
        displayUserData();
        if (contactsPresenter != null) {
            contactsPresenter.handleLoginResponse(loginResponse);
        }
    }

    public void handleAddContactResponse(AddContactResponse message) {
        if (contactsPresenter != null) {
            contactsPresenter.handleAddContactResponse(message);
        }
    }

    public void handleCreateChatResponse(CreateChatResponse message) {
        if (chatsPresenter != null) {
            chatsPresenter.handleCreateChatResponse(message);
        }
    }

    public void handleChatInvitation(ChatInvitation message) {
        if (invitationsPresenter != null) {
            invitationsPresenter.handleChatInvitation(message);
        }
    }

    public void showContactAlreadyAddedInfo() {
        if (contactsPresenter != null) {
            contactsPresenter.showContactAlreadyAddedInfo();
        }
    }

    public void showContactNotExistInfo() {
        if (contactsPresenter != null) {
            contactsPresenter.showContactNotExistInfo();
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
    private void displayUserData() {
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
        String userNumber = String.valueOf(appPreferences.getInt(AppPreferences.Key.USER_NUMBER));
        if (isViewAttached()) {
            getView().displayUserData(userName, userNumber);
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
}
