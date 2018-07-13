package tattler.pro.tattler.main;

import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.Message;

public class MainMessageHandler implements ReceivedMessageCallback {
    private MainPresenter presenter; // TODO: Make it more generic to use in multiple activities / fragments

    @Override
    public void onMessageReceived(Message message) {
        switch (message.messageType) {
            case Message.Type.ADD_CONTACT_RESPONSE:
                presenter.handleAddContactResponse((AddContactResponse) message);
                break;
        }
    }

    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }
}
