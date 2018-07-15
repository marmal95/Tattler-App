package tattler.pro.tattler.main;

import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;

public class MainMessageHandler implements ReceivedMessageCallback {
    private MainPresenter presenter;

    @Override
    public void onMessageReceived(Message message) {
        switch (message.messageType) {
            case Message.Type.LOGIN_RESPONSE:
                handleLoginResponse((LoginResponse) message);
                break;
            case Message.Type.ADD_CONTACT_RESPONSE:
                handleAddContactResponse((AddContactResponse) message);
                break;
        }
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    private void handleLoginResponse(LoginResponse message) {
        switch (message.status) {
            case LoginResponse.Status.LOGIN_SUCCESSFUL:
                presenter.handleLoginResponse(message);
                break;
        }
    }

    private void handleAddContactResponse(AddContactResponse message) {
        switch (message.status) {
            case AddContactResponse.Status.CONTACT_ADDED:
                presenter.handleAddContactResponse(message);
                break;
            case AddContactResponse.Status.CONTACT_ALREADY_ADDED:
                // TODO: Notify user
                break;
            case AddContactResponse.Status.CONTACT_NOT_EXIST:
                // TODO: Notify user
                break;
        }
    }
}
