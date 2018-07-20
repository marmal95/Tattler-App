package tattler.pro.tattler.messages;

import java.io.Serializable;
import java.util.List;

public class CreateChatRequest extends Message implements Serializable {
    private static final long serialVersionUID = 1930918967560003328L;
    public List<Integer> contactsNumbers;
    public String chatName;

    public CreateChatRequest(int senderId, List<Integer> contactsNumbers, String chatName) {
        super(Type.CREATE_CHAT_REQUEST);
        this.contactsNumbers = contactsNumbers;
        this.chatName = chatName;
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "CreateChatRequest{" +
                "contactsNumbers=" + contactsNumbers +
                ", chatName='" + chatName + '\'' +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId + '}';
    }
}
