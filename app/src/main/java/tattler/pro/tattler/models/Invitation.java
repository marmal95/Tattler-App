package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "invitation")
public class Invitation implements Serializable {
    private static final long serialVersionUID = -8968639584253595654L;

    @DatabaseField(id = true, columnName = "invitation_id", unique = true)
    public int invitationId;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat")
    public Chat chat;

    @DatabaseField(columnName = "sender_id")
    public int senderId;

    @DatabaseField(columnName = "receiver_id")
    public int receiverId;

    public Invitation() {

    }

    @Override
    public String toString() {
        return "Invitation{" +
                "invitationId=" + invitationId +
                ", chat=" + chat +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
