package tattler.pro.tattler.tcp;

import com.orhanobut.logger.Logger;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.Message;

public class MessageHandler {
    private TcpConnectionService tcpConnectionService;
    private AppPreferences appPreferences;

    public MessageHandler(TcpConnectionService tcpConnectionService, AppPreferences appPreferences) {
        this.tcpConnectionService = tcpConnectionService;
        this.appPreferences = appPreferences;
    }

    public void handleReceivedMessage(Message message) {
        switch (message.messageType) {
            case Message.Type.LOGIN_RESPONSE:
                handleReceivedLoginResponse((LoginResponse) message);
                break;
        }
    }

    private void handleReceivedLoginResponse(LoginResponse loginResponse) {
        Logger.d("Received Message: LoginResponse. [Status: " + loginResponse.status + "].");
        appPreferences.put(AppPreferences.Key.USER_NUMBER, loginResponse.phoneId);
    }
}