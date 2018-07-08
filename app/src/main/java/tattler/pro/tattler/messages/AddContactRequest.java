package tattler.pro.tattler.messages;

public class AddContactRequest extends Message {
    public String contactName;
    public int contactNumber;

    public AddContactRequest(String contactName, int contactNumber, int senderId) {
        super(Type.ADD_CONTACT_REQUEST);
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.senderId = senderId;
    }

}
