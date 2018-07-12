package tattler.pro.tattler.common;

import tattler.pro.tattler.messages.Message;

public interface ReceivedMessageCallback {
    void onMessageReceived(Message message);
}
