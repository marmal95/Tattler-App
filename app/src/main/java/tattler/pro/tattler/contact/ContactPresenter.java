package tattler.pro.tattler.contact;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.tcp.TcpServiceConnector;
import tattler.pro.tattler.tcp.TcpServiceConnectorFactory;
import tattler.pro.tattler.tcp.TcpServiceManager;

public class ContactPresenter extends MvpBasePresenter<ContactView> {
    private TcpServiceManager tcpServiceManager;
    private TcpServiceConnector tcpServiceConnector;

    ContactPresenter(TcpServiceManager tcpManager,
                     TcpServiceConnectorFactory serviceConnectorFactory) {
        this.tcpServiceManager = tcpManager;
        this.tcpServiceConnector = serviceConnectorFactory.create(tcpServiceManager);
    }

    @SuppressWarnings("ConstantConditions")
    public void onCreate(Contact contact) {
        if (isViewAttached()) {
            getView().loadContactData(contact);
        }

        if (!tcpServiceManager.isServiceBound()) {
            bindTcpConnectionService();
        }
    }

    public void onDestroy() {
        if (tcpServiceManager.isServiceBound()) {
            unbindTcpConnectionService();
        }
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
}
