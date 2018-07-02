package tattler.pro.tattler.tcp;

public class TcpServiceConnectorFactory {
    public TcpServiceConnector create(TcpServiceManager serviceManager) {
        return new TcpServiceConnector(serviceManager);
    }
}
