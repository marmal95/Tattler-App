package tattler.pro.tattler.tcp;


import com.orhanobut.logger.Logger;

public class TcpServiceManager {
    private TcpConnectionService tcpConnectionService;
    private boolean isServiceBound;

    public TcpServiceManager() {
        tcpConnectionService = null;
        isServiceBound = false;
    }

    public boolean isServiceBound() {
        return isServiceBound && tcpConnectionService != null;
    }

    public void bindService(TcpConnectionService.TcpServiceBinder serviceBinder) {
        Logger.d("Binding TcpConnectionService.");
        tcpConnectionService = serviceBinder.getService();
        isServiceBound = true;
    }

    public void unBindService() {
        Logger.d("Unbinding TcpConnectionService.");
        tcpConnectionService = null;
        isServiceBound = false;
    }

    public TcpConnectionService getTcpService() {
        return tcpConnectionService;
    }
}
