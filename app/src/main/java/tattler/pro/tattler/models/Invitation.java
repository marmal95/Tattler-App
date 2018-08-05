package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "invitation")
public class Invitation implements Serializable {
    private static final long serialVersionUID = -8968639584253595654L;

    @DatabaseField(generatedId = true, columnName = "invitation_id", unique = true)
    public int invitationId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat")
    public Chat chat;

    @DatabaseField(columnName = "sender_id")
    public int senderId;

    @DatabaseField(columnName = "invitation_message_id")
    public long invitationMessageId;

    @DatabaseField(columnName = "invitation_status")
    public State state;

    public Invitation() {}

    public Invitation(Chat chat, int senderId, long invitationMessageId, State state) {
        this.chat = chat;
        this.senderId = senderId;
        this.invitationMessageId = invitationMessageId;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "invitationId=" + invitationId +
                ", chat=" + chat +
                ", senderId=" + senderId +
                ", invitationMessageId=" + invitationMessageId +
                ", state=" + state +
                '}';
    }

    public enum State {
        PENDING_FOR_RESPONSE,
        PENDING_FOR_REACTION
    }
}
