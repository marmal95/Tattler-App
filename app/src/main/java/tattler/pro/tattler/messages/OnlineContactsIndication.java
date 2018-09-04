package tattler.pro.tattler.messages;

import java.util.List;

public class OnlineContactsIndication extends Message {
    private static final long serialVersionUID = -5240894162582569537L;
    public List<Integer> contactNumbers;

    public OnlineContactsIndication(int receiverId, List<Integer> contactNumbers) {
        super(Type.ONLINE_CONTACTS_INDICATION);
        this.receiverId = receiverId;
        this.contactNumbers = contactNumbers;
    }

    @Override
    public String toString() {
        return "OnlineContactsIndication{" +
                "contactNumbers=" + contactNumbers +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
