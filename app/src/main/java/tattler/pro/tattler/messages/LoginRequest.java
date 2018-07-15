package tattler.pro.tattler.messages;

public class LoginRequest extends Message {
    private static final long serialVersionUID = -4094636369413588997L;
    public String phoneNumber;
    public String userName;
    public boolean isContactsRetrievalRequested;

    public LoginRequest(String phoneNumber, String userName, boolean contactsRetrievalRequested) {
        super(Type.LOGIN_REQUEST);
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.isContactsRetrievalRequested = contactsRetrievalRequested;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", isContactsRetrievalRequested=" + isContactsRetrievalRequested +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
