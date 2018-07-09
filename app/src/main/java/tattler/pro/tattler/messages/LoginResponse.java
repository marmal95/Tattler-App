package tattler.pro.tattler.messages;

import java.io.Serializable;

public class LoginResponse extends MessageResponse implements Serializable {
    private static final long serialVersionUID = 3160773710141271860L;
    public int phoneId;

    public LoginResponse(int status, long acknowledgedMessageId, int phoneId) {
        super(Type.LOGIN_RESPONSE, acknowledgedMessageId, status);
        this.phoneId = phoneId;
        this.receiverId = phoneId;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "phoneId=" + phoneId +
                ", acknowledgedMessageId=" + acknowledgedMessageId +
                ", status=" + status +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }


    public class Status {
        public static final int LOGIN_SUCCESSFUL = 0;
        public static final int INCORRECT_DATA = 1;
    }
}