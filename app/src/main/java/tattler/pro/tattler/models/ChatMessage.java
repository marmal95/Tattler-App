package tattler.pro.tattler.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@DatabaseTable(tableName = "message")
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = -7866765286937008977L;

    @DatabaseField(generatedId = true, columnName = "messageId", unique = true)
    public int messageId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat")
    public Chat chat;

    @DatabaseField(columnName = "sender_id", uniqueCombo = true)
    public int senderId;

    @DatabaseField(columnName = "content", dataType = DataType.BYTE_ARRAY)
    public byte[] content;

    @DatabaseField(columnName = "content_type")
    public ContentType contentType;

    @DatabaseField(columnName = "message_time")
    public long messageTime;

    public ChatMessage() {}

    public String getStringTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(messageTime * 1000));
    }

    public enum ContentType {
        TEXT,
        PHOTO,
        ATTACHMENT
    }
}
