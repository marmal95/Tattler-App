package tattler.pro.tattler.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "chat")
public class Chat implements Serializable {
    private static final long serialVersionUID = -320051287339286027L;

    @DatabaseField(columnName = "chat_id", unique = true, id = true)
    public int chatId;

    @DatabaseField(columnName = "chat_name")
    public String chatName;

    @DatabaseField(columnName = "is_group")
    public boolean isGroupChat;

    @DatabaseField(columnName = "is_initialized")
    public boolean isInitialized;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] publicKey;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] privateKey;

    @ForeignCollectionField
    public ForeignCollection<Invitation> invitations;

    @ForeignCollectionField
    public ForeignCollection<Participant> participants;

    public Chat() {}

    public Chat(int chatId, boolean isGroupChat, String chatName, boolean isInitialized) {
        this.chatId = chatId;
        this.isGroupChat = isGroupChat;
        this.chatName = chatName;
        this.isInitialized = isInitialized;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", chatName='" + chatName + '\'' +
                ", isGroupChat=" + isGroupChat +
                ", isInitialized=" + isInitialized +
                ", invitations=" + invitations +
                ", participants=" + participants +
                '}';
    }

}
