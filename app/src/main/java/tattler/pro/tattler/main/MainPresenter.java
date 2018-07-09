package tattler.pro.tattler.main;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.messages.Message;
import tattler.pro.tattler.tcp.MessageBroadcastReceiver;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class MainPresenter extends MvpBasePresenter<MainView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;
    private AppPreferences appPreferences;
    private MessageBroadcastReceiver broadcastReceiver;

    MainPresenter(TcpServiceManager tcpManager,
                  TcpServiceConnectorFactory serviceConnectorFactory,
                  AppPreferences appPreferences,
                  MessageBroadcastReceiver broadcastReceiver) {
        this.tcpServiceManager = tcpManager;
        this.tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
        this.appPreferences = appPreferences;
        this.broadcastReceiver = broadcastReceiver;
    }

    public void onCreate() {
        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }

        displayUserData();
        registerReceiver();
    }

    public void onDestroy() {
        if (tcpServiceManager.isServiceBound()) {
            unbindTcpConnectionService();
        }

        unregisterReceiver();
    }

    @SuppressWarnings("ConstantConditions")
    public void handleNavContactsClick() {
        if (isViewAttached()) {
            getView().startContactsFragment();
        }
    }

    public void sendMessage(Message message) {
        tcpServiceManager.getTcpService().sendMessage(message);
    }

    @SuppressWarnings("ConstantConditions")
    private void bindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Binding TcpConnectionService.");
            getView().bindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void unbindTcpConnectionService() {
        if (isViewAttached()) {
            Logger.d("Unbinding TcpConnectionService.");
            getView().unbindTcpConnectionService(tcpServiceConnector);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void displayUserData() {
        String userName = appPreferences.getString(AppPreferences.Key.USER_NAME);
        String userNumber = String.valueOf(appPreferences.getInt(AppPreferences.Key.USER_NUMBER));
        if (isViewAttached()) {
            getView().displayUserData(userName, userNumber);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void registerReceiver() {
        if (isViewAttached()) {
            Logger.d("Registering BroadcastReceiver.");
            getView().registerReceiver(broadcastReceiver);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void unregisterReceiver() {
        if (isViewAttached()) {
            Logger.d("Unregistering BroadcastReceiver.");
            getView().unregisterReceiver(broadcastReceiver);
        }
    }
}
