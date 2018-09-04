package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "contact")
public class Contact implements Serializable {
    private static final long serialVersionUID = -365846270224366014L;

    @DatabaseField(id = true, columnName = "contact_number", unique = true)
    public int contactNumber;

    @DatabaseField(columnName = "contact_name")
    public String contactName;

    public boolean isOnline;

    public Contact() {}

    public Contact(String contactName, int contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactNumber=" + contactNumber +
                ", contactName='" + contactName + '\'' +
                '}';
    }
}
