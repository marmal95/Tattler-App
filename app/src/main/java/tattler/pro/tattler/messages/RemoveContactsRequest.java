package tattler.pro.tattler.messages;

import java.util.List;

public class RemoveContactsRequest extends Message {
    private static final long serialVersionUID = -2664689944280273703L;
    public List<Integer> contactNumbers;

    public RemoveContactsRequest(List<Integer> contactNumbers, int senderId) {
        super(Type.REMOVE_CONTACT_REQUEST);
        this.contactNumbers = contactNumbers;
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "RemoveContactsRequest{" +
                "contactNumbers=" + contactNumbers +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
