package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "participant")
public class Participant implements Serializable {
    private static final long serialVersionUID = -6215170685965904151L;

    @DatabaseField(id = true, columnName = "participant_id", unique = true)
    public int participantId;

    @DatabaseField(columnName = "contact_number", uniqueCombo = true)
    public int contactNumber;

    @DatabaseField(columnName = "contact_name")
    public String contactName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "chat", uniqueCombo = true)
    public Chat chat;

    public Participant() {}

    public Participant(int contactNumber, String contactName, Chat chat) {
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "participantId=" + participantId +
                ", contactNumber=" + contactNumber +
                ", contactName='" + contactName + '\'' +
                ", chat=" + chat +
                '}';
    }
}
