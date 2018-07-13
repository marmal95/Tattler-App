package tattler.pro.tattler.messages;

public class AddContactResponse extends MessageResponse {
    private static final long serialVersionUID = -182716305931807836L;
    public String userName;
    public int userNumber;

    public AddContactResponse(int status, long acknowledgedMessageId, int receiverId) {
        super(Type.ADD_CONTACT_RESPONSE, acknowledgedMessageId, status);
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "AddContactResponse{"
                + "userName='" + userName
                + ", userNumber=" + userNumber
                + ", acknowledgedMessageId=" + acknowledgedMessageId
                + ", status=" + status
                + ", messageType=" + messageType
                + ", messageTime=" + messageTime
                + ", messageId=" + messageId
                + ", senderId=" + senderId
                + ", receiverId=" + receiverId + '}';
    }

    public class Status {
        public static final int CONTACT_ADDED = 0;
        public static final int CONTACT_NOT_EXIST = 1;
        public static final int CONTACT_ALREADY_ADDED = 2;
    }
}
