package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpView;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpServiceConnector;

interface MainView extends MvpView {
    void bindTcpConnectionService(TcpServiceConnector serviceConnector);
    void unbindTcpConnectionService(TcpServiceConnector serviceConnector);
    void startContactsFragment();
    void startChatsFragment();
    void startInvitationsFragment();
    void displayUserData(String userName, String userNumber);
    void registerReceiver(MessageBroadcastReceiver receiver);
    void unregisterReceiver(MessageBroadcastReceiver receiver);
}
