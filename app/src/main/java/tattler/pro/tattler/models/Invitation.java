package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "invitation")
public class Invitation implements Serializable {
    private static final long serialVersionUID = -8968639584253595654L;

    @DatabaseField(generatedId = true, columnName = "invitation_id", unique = true)
    public int invitationId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat", uniqueCombo = true)
    public Chat chat;

    @DatabaseField(columnName = "sender_id", uniqueCombo = true)
    public int senderId;

    @DatabaseField(columnName = "receiver_id", uniqueCombo = true)
    public int receiverId;

    @DatabaseField(columnName = "invitation_message_id")
    public long invitationMessageId;

    @DatabaseField(columnName = "invitation_status")
    public State state;

    @SuppressWarnings("unused")
    public Invitation() {}

    public Invitation(Chat chat, int senderId, int receiverId, long invitationMessageId, State state) {
        this.chat = chat;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.invitationMessageId = invitationMessageId;
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Invitation && invitationId == ((Invitation) obj).invitationId;
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
