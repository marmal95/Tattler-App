package tattler.pro.tattler.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import tattler.pro.tattler.messages.ChatMessage;

@DatabaseTable(tableName = "message")
public class Message implements Serializable {
    private static final long serialVersionUID = -7866765286937008977L;

    @DatabaseField(generatedId = true, columnName = "messageId", unique = true)
    public long messageId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat")
    public Chat chat;

    @DatabaseField(columnName = "sender_id")
    public int senderId;

    @DatabaseField(columnName = "content", dataType = DataType.BYTE_ARRAY)
    public byte[] content;

    @DatabaseField(columnName = "content_type")
    public ContentType contentType;

    @DatabaseField(columnName = "message_time")
    public long messageTime;

    public Message() {}

    public Message(ChatMessage chatMessage, Chat chat) {
        this.messageId = chatMessage.messageId;
        this.chat = chat;
        this.senderId = chatMessage.senderId;
        this.content = chatMessage.content;
        this.contentType = convertToMessageContentType(chatMessage.contentType);
        this.messageTime = chatMessage.messageTime;
    }

    public String getStringTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(messageTime));
    }

    private ContentType convertToMessageContentType(ChatMessage.ContentType contentType) {
        switch (contentType) {
            case TEXT:
                return ContentType.TEXT;
            case IMAGE:
                return ContentType.PHOTO;
        }
        return null;
    }

    public enum ContentType {
        TEXT,
        PHOTO
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Message && messageId == ((Message) obj).messageId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", chat=" + chat.chatId +
                ", senderId=" + senderId +
                ", content=" + Arrays.toString(content) +
                ", contentType=" + contentType +
                ", messageTime=" + messageTime +
                '}';
    }
}
