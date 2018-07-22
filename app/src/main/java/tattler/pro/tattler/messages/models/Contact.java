package tattler.pro.tattler.messages.models;

import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = 6268695091000379611L;
    public int contactNumber;
    public String contactName;

    public Contact(int contactNumber, String contactName) {
        this.contactNumber = contactNumber;
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "contactNumber=" + contactNumber +
                ", contactName='" + contactName + '\'' +
                '}';
    }
}
