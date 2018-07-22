package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contact_chat")
public class ContactChat {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(foreign = true, columnName = "chat_id", uniqueCombo = true)
    public Chat chat;

    @DatabaseField(foreign = true, columnName = "contact_number", uniqueCombo = true)
    public Contact contact;

    public ContactChat() {

    }

    public ContactChat(Chat chat, Contact contact) {
        this.chat = chat;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "ContactChat{" +
                "id=" + id +
                ", chat=" + chat +
                ", contact=" + contact +
                '}';
    }
}
