package tattler.pro.tattler.tcp;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class TcpServiceConnector implements ServiceConnection {
    private TcpServiceManager serviceManager;

    TcpServiceConnector(TcpServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (!serviceManager.isServiceBound()) {
            TcpConnectionService.TcpServiceBinder myBinder = (TcpConnectionService.TcpServiceBinder) service;
            serviceManager.bindService(myBinder);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (serviceManager.isServiceBound()) {
            serviceManager.unBindService();
        }
    }
}
