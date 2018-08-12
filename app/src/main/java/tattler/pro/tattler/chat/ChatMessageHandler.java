package tattler.pro.tattler.chat;

import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.internal_messages.InternalMessage;
import tattler.pro.tattler.internal_messages.MessagesUpdate;

public class ChatMessageHandler implements ReceivedMessageCallback {
    private ChatPresenter presenter;

    public void setPresenter(ChatPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onMessageReceived(InternalMessage message) {
        Logger.d("Handling Message: " + message.toString());
        switch (message.type) {
            case MESSAGES_UPDATE:
                handleMessagesUpdate((MessagesUpdate) message);
                break;
        }
    }

    private void handleMessagesUpdate(MessagesUpdate message) {
        switch (message.reason) {
            case NEW_MESSAGE_RECEIVED:
                presenter.handleMessagesReceived(message.messages);
                break;
        }
    }
}
