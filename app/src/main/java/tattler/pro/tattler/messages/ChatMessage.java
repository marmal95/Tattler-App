package tattler.pro.tattler.messages;

import java.util.Arrays;

public class ChatMessage extends Message {
    private static final long serialVersionUID = -6855327870691085198L;
    public int chatId;
    public byte[] content;
    public ContentType contentType;

    public ChatMessage(int senderId, int chatId, byte[] content, ContentType contentType) {
        super(Type.CHAT_MESSAGE);
        this.senderId = senderId;
        this.chatId = chatId;
        this.content = content;
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "chatId=" + chatId +
                ", content=" + Arrays.toString(content) +
                ", contentType=" + contentType +
                ", messageType=" + messageType +
                ", messageTime=" + messageTime +
                ", messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }

    public enum ContentType {
        TEXT,
        IMAGE
    }
}
