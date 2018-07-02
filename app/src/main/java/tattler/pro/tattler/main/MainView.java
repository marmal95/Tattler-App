package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpView;
import tattler.pro.tattler.tcp.TcpServiceConnector;

interface MainView extends MvpView {
    void startTcpConnectionService(TcpServiceConnector serviceConnector);
    void stopTcpConnectionService(TcpServiceConnector serviceConnector);
}
