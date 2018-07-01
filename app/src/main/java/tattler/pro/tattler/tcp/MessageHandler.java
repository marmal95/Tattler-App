package tattler.pro.tattler.tcp;

import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;

public class MessageHandler {
    private TcpConnectionService tcpConnectionService;

    public MessageHandler(TcpConnectionService tcpConnectionService) {
        this.tcpConnectionService = tcpConnectionService;
    }

    public void handleReceivedMessage(Message message) {
        switch (message.messageType) {
            case Message.Type.LOGIN_RESPONSE:
                handleReceivedLoginResponse((LoginResponse) message);
                break;
        }
    }

    private void handleReceivedLoginResponse(LoginResponse loginResponse) {

    }
}