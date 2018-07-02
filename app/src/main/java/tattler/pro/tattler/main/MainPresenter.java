package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
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

    public void onCreate() {
        if (!tcpServiceManager.isServiceBound()) {
            startTcpConnectionService();
        }
    }

    public void onDestroy() {
        if (tcpServiceManager.isServiceBound()) {
            stopTcpConnectionService();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void startTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Starting TcpConnectionService.");
            getView().startTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void stopTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Stopping TcpConnectionService.");
            getView().stopTcpConnectionService(tcpServiceConnector);
        }
    }

}
