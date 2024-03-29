package tattler.pro.tattler.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;

@DatabaseTable(tableName = "chat")
public class Chat implements Serializable {
    private static final long serialVersionUID = -320051287339286027L;

    @DatabaseField(id = true, columnName = "chat_id", unique = true)
    public int chatId;

    @DatabaseField(columnName = "chat_name")
    public String chatName;

    @DatabaseField(columnName = "is_group")
    public boolean isGroupChat;

    @DatabaseField(columnName = "is_initialized")
    public boolean isInitialized;

    @DatabaseField(columnName = "is_muted")
    public boolean isMuted;

    @DatabaseField(columnName = "is_blocked")
    public boolean isBlocked;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] publicKey;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] privateKey;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] chatKey;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<Invitation> invitations;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<Participant> participants;

    @ForeignCollectionField(eager = true)
    public ForeignCollection<Message> messages;

    public Chat() {}

    public Chat(int chatId, boolean isGroupChat, String chatName, boolean isInitialized) {
        this.chatId = chatId;
        this.isGroupChat = isGroupChat;
        this.chatName = chatName;
        this.isInitialized = isInitialized;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Chat && chatId == ((Chat) obj).chatId;
    }

    @Override
    public String toString() {
        int invitationsSize = invitations == null ? 0 : invitations.size();
        int participantsSize = participants == null ? 0 : participants.size();
        int messagesSize = messages == null ? 0 : messages.size();

        return "Chat{" +
                "chatId=" + chatId +
                ", chatName='" + chatName + '\'' +
                ", isGroupChat=" + isGroupChat +
                ", isInitialized=" + isInitialized +
                ", isMuted=" + isMuted +
                ", isBlocked=" + isBlocked +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", privateKey=" + Arrays.toString(privateKey) +
                ", chatKey=" + Arrays.toString(chatKey) +
                ", invitations=" + "[" + invitationsSize + "]" +
                ", participants=" + "[" + participantsSize + "]" +
                ", messages=" + "[" + messagesSize + "]" +
                '}';
    }
}
