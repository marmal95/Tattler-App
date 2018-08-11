package tattler.pro.tattler.common;

import tattler.pro.tattler.internal_messages.InternalMessage;

public interface ReceivedMessageCallback {
    void onMessageReceived(InternalMessage message);
}
