package tattler.pro.tattler.tcp;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.orhanobut.logger.Logger;

public class TcpServiceConnector implements ServiceConnection {
    private TcpServiceManager serviceManager;

    TcpServiceConnector(TcpServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Logger.d("Service has been connected.");
        if (!serviceManager.isServiceBound()) {
            TcpConnectionService.TcpServiceBinder myBinder = (TcpConnectionService.TcpServiceBinder) service;
            serviceManager.bindService(myBinder);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Logger.d("Service has been disconnected.");
        if (serviceManager.isServiceBound()) {
            serviceManager.unBindService();
        }
    }
}
