package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class MainPresenter extends MvpBasePresenter<MainView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;

    MainPresenter(TcpServiceManager tcpManager, TcpServiceConnectorFactory serviceConnectorFactory) {
        tcpServiceManager = tcpManager;
        tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
    }

    public void onStart() {
        startTcpConnectionService();
    }

    public void onStop() {
        stopTcpConnectionService();
    }

    @SuppressWarnings("ConstantConditions")
    private void startTcpConnectionService() {
        if (isViewAttached()) {
            getView().startTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void stopTcpConnectionService() {
        if (isViewAttached()) {
            getView().stopTcpConnectionService(tcpServiceConnector);
        }
    }

}
