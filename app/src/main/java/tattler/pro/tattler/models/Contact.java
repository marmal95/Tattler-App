package tattler.pro.tattler.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "contact")
public class Contact implements Serializable {
    private static final long serialVersionUID = -365846270224366014L;

    @DatabaseField(generatedId = true, columnName = "contact_id")
    public int contactId;

    @DatabaseField(columnName = "user_name")
    public String userName;

    @DatabaseField(columnName = "phone_number")
    public String phoneNumber;

    @DatabaseField(columnName = "user_number", unique = true)
    public int userNumber;

    public Contact() {}

    public Contact(String userName, int userNumber) {
        this(userName, "", userNumber);
    }

    public Contact(String userName, String phoneNumber, int userNumber) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userNumber = userNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userNumber=" + userNumber +
                '}';
    }
}
