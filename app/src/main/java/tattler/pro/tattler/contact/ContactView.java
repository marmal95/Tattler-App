package tattler.pro.tattler.contact;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.tcp.TcpServiceConnector;

interface ContactView extends MvpView {
    void loadContactData(Contact contact);
    void bindTcpConnectionService(TcpServiceConnector serviceConnector);
    void unbindTcpConnectionService(TcpServiceConnector serviceConnector);
    void startChatActivity(Chat chat);
}
