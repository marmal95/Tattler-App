package tattler.pro.tattler.messages;

public class AddContactRequest extends Message {
    private static final long serialVersionUID = 6709921986039195846L;
    public int contactNumber;

    public AddContactRequest(int contactNumber, int senderId) {
        super(Type.ADD_CONTACT_REQUEST);
        this.contactNumber = contactNumber;
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "AddContactRequest{"
                + "contactNumber=" + contactNumber
                + ", messageType=" + messageType
                + ", messageTime=" + messageTime
                + ", messageId=" + messageId
                + ", senderId=" + senderId
                + ", receiverId=" + receiverId + '}';
    }
}
