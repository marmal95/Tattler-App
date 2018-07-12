package tattler.pro.tattler.tcp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.common.IntentKey;
import tattler.pro.tattler.common.ReceivedMessageCallback;
import tattler.pro.tattler.messages.Message;

public class MessageBroadcastReceiver extends BroadcastReceiver {
    private ReceivedMessageCallback receivedMessageCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("Received broadcast message.");
        String intentAction = intent.getAction();
        if (intentAction != null && intentAction.equals(IntentKey.BROADCAST_MESSAGE.name())) {
            Message message = (Message) intent.getSerializableExtra(IntentKey.MESSAGE.name());
            Logger.d("Received message: " + message.toString());
            receivedMessageCallback.onMessageReceived(message);
        }
    }

    public void setReceivedMessageCallback(ReceivedMessageCallback messageCallback) {
        receivedMessageCallback = messageCallback;
    }

    public IntentFilter createBroadcastMessageIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentKey.BROADCAST_MESSAGE.name());
        return intentFilter;
    }
}
