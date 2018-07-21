package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "chat")
public class Chat implements Serializable {
    private static final long serialVersionUID = -320051287339286027L;

    @DatabaseField(columnName = "chat_id", unique = true, id = true)
    public int chatId;

    @DatabaseField(columnName = "is_group")
    public boolean isGroupChat;

    public Chat() {}

    public Chat(int chatId, boolean isGroupChat) {
        this.chatId = chatId;
        this.isGroupChat = isGroupChat;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", isGroupChat=" + isGroupChat +
                '}';
    }
}
