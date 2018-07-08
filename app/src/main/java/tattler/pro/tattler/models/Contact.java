package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "contact")
public class Contact implements Serializable {
    private static final long serialVersionUID = -365846270224366014L;

    @DatabaseField(generatedId = true, columnName = "contact_id")
    public int contactId;

    @DatabaseField(columnName = "contact_name")
    public String contactName;

    @DatabaseField(columnName = "contact_number", unique = true)
    public int contactNumber;

    public Contact() {}

    public Contact(String contactName, int contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId + ", contactName='" + contactName + '\'' + ", contactNumber=" + contactNumber +
                '}';
    }
}
