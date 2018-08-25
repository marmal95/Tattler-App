package tattler.pro.tattler.chat;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpServiceConnector;

interface ChatView extends MvpView {
    void bindTcpConnectionService(TcpServiceConnector serviceConnector);
    void unbindTcpConnectionService(TcpServiceConnector serviceConnector);
    void registerReceiver(MessageBroadcastReceiver receiver);
    void unregisterReceiver(MessageBroadcastReceiver receiver);
    void setMessagesAdapter(MessagesAdapter adapter);
    void setTitle(String title);
    void scrollToPosition(int position);
    void pickImage();
    void disableChat();
}
