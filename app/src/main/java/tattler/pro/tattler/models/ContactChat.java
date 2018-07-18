package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contact_chat")
public class ContactChat {

    @DatabaseField(columnName = "id", generatedId = true)
    public int id;

    @DatabaseField(foreign = true, columnName = "chat_id")
    public Chat chat;

    @DatabaseField(foreign = true, columnName = "contact_id")
    public Contact contact;

    public ContactChat() {

    }
}
