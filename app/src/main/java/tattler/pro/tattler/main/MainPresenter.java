package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;

import java.util.List;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.internal_messages.UserInfoUpdate;
import tattler.pro.tattler.main.chats.ChatsPresenter;
import tattler.pro.tattler.main.contacts.ContactsPresenter;
import tattler.pro.tattler.main.invitations.InvitationsPresenter;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.Invitation;
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
        displayUserInfo();
        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }
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

    @SuppressWarnings("ConstantConditions")
    public void handleNavSettingsClick() {
        if (isViewAttached()) {
            getView().startSettingsFragment();
        }
    }

    public void sendMessage(Message message) {
        tcpServiceManager.getTcpService().sendMessage(message);
    }

    public void updateUserInfo(UserInfoUpdate userInfo) {
        displayUserInfo(userInfo.userName, String.valueOf(userInfo.userPhoneId));
    }

    public void handleContactsUpdate(List<Contact> contacts) {
        if (contactsPresenter != null) {
            contactsPresenter.handleContactsUpdate(contacts);
        }
    }

    public void handleContactAdded(Contact contact) {
        if (contactsPresenter != null) {
            contactsPresenter.handleContactAdded(contact);
        }
    }

    public void handleChatModified(Chat chat) {
        if (chatsPresenter != null) {
            chatsPresenter.handleChatModified(chat);
        }
    }

    public void handleChatRemoved(Chat chat) {
        if (chatsPresenter != null) {
            chatsPresenter.handleChatRemoved(chat);
        }

        if (invitationsPresenter != null) {
            invitationsPresenter.handleChatRemoved(chat);
        }
    }

    public void handleInvitationReceived(Invitation invitation) {
        if (invitationsPresenter != null) {
            invitationsPresenter.handleInvitationReceived(invitation);
        }
    }

    public void handleChatInitializedIndication(List<Invitation> invitations) {
        if (invitationsPresenter != null) {
            invitationsPresenter.handleChatInitializedIndication(invitations);
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

    private void displayUserInfo() {
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
        String userNumber = String.valueOf(appPreferences.getInt(AppPreferences.Key.USER_NUMBER));
        displayUserInfo(userName, userNumber);
    }

    @SuppressWarnings("ConstantConditions")
    private void displayUserInfo(String userName, String userNumber) {
        if (isViewAttached()) {
            getView().displayUserData(userName, userNumber);
        }
    }
}
