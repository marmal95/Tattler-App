package tattler.pro.tattler.tcp;

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
        tcpConnectionService = serviceBinder.getService();
        isServiceBound = true;
    }

    public void unBindService() {
        tcpConnectionService = null;
        isServiceBound = false;
    }

    public TcpConnectionService getTcpService() {
        return tcpConnectionService;
    }
}
