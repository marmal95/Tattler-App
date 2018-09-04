package tattler.pro.tattler.main;

import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.internal_messages.ChatsUpdate;
import tattler.pro.tattler.internal_messages.ContactsUpdate;
import tattler.pro.tattler.internal_messages.InternalMessage;
import tattler.pro.tattler.internal_messages.InvitationsUpdate;
import tattler.pro.tattler.internal_messages.UserInfoUpdate;

public class MainMessageHandler implements ReceivedMessageCallback {
    private MainPresenter presenter;

    @Override
    public void onMessageReceived(InternalMessage message) {
        Logger.d("Handling Message: " + message.toString());
        switch (message.type) {
            case USER_INFO_UPDATE:
                handleUserInfoUpdate((UserInfoUpdate) message);
                break;
            case CONTACTS_UPDATE:
                handleContactsUpdate((ContactsUpdate) message);
                break;
            case CHATS_UPDATE:
                handleChatsUpdate((ChatsUpdate) message);
                break;
            case INVITATIONS_UPDATE:
                handleInvitationsUpdate((InvitationsUpdate) message);
                break;
        }
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    private void handleUserInfoUpdate(UserInfoUpdate message) {
        switch (message.reason) {
            case LOGIN_SUCCESSFUL:
                presenter.updateUserInfo(message);
                break;
        }
    }

    private void handleContactsUpdate(ContactsUpdate message) {
        switch (message.reason) {
            case ALL_CONTACTS_UPDATE:
                presenter.handleContactsUpdate(message.contacts);
                break;
            case NEW_CONTACT_ADDED:
                presenter.handleContactAdded(message.contacts.iterator().next());
                break;
            case CONTACT_ALREADY_ADDED:
                presenter.handleContactAdded(message.contacts.iterator().next());
                presenter.showContactAlreadyAddedInfo();
                break;
            case CONTACT_NOT_EXIST:
                presenter.showContactNotExistInfo();
                break;
            case ONLINE_STATUS_UPDATE:
                presenter.handleOnlineStatusUpdate(message.contacts);
                break;
        }
    }

    private void handleChatsUpdate(ChatsUpdate message) {
        switch (message.reason) {
            case CHAT_MODIFIED:
                presenter.handleChatModified(message.chats.iterator().next());
                break;
            case CHAT_REMOVED:
                presenter.handleChatRemoved(message.chats.iterator().next());
                break;
        }
    }

    private void handleInvitationsUpdate(InvitationsUpdate message) {
        switch (message.reason) {
            case NEW_INVITATION:
                presenter.handleInvitationReceived(message.invitations.iterator().next());
                break;
            case CHAT_INITIALIZED:
                presenter.handleChatInitializedIndication(message.invitations);
                break;
        }
    }
}
