package tattler.pro.tattler.messages;

import java.util.List;

import tattler.pro.tattler.messages.models.Contact;

public class CreateChatResponse extends MessageResponse {
    private static final long serialVersionUID = 7689710211630600805L;
    public int chatId;
    public boolean isGroupChat;
    public String chatName;
    public List<Contact> contacts;

    public CreateChatResponse(int status, long acknowledgedMessageId, int receiverId) {
        super(Type.CREATE_CHAT_RESPONSE, acknowledgedMessageId, status);
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "CreateChatResponse{" +
                "chatId=" + chatId +
                ", isGroupChat=" + isGroupChat +
                ", chatName='" + chatName + '\'' +
                ", contacts=" + contacts +
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
        public static final int CHAT_CREATED = 0;
        public static final int CHAT_ALREADY_EXISTS = 1;
        public static final int CHAT_NO_CONTACTS_ENOUGH = 2;
    }
}
