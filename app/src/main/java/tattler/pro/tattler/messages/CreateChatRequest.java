package tattler.pro.tattler.messages;

import java.io.Serializable;
import java.util.List;

public class CreateChatRequest extends Message implements Serializable {
    private static final long serialVersionUID = 1930918967560003328L;
    public List<Integer> contactsNumbers;
    public String chatName;
    public boolean isGroupChat;

    public CreateChatRequest(int senderId, List<Integer> contactsNumbers, String chatName, boolean isGroupChat) {
        super(Type.CREATE_CHAT_REQUEST);
        this.contactsNumbers = contactsNumbers;
        this.chatName = chatName;
        this.senderId = senderId;
        this.isGroupChat = isGroupChat;
    }

    @Override
    public String toString() {
        return "CreateChatRequest{" +
                "contactsNumbers=" + contactsNumbers +
                ", chatName='" + chatName + '\'' +
                ", isGroupChat=" + isGroupChat +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
