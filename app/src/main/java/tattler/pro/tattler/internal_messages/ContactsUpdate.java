package tattler.pro.tattler.internal_messages;

import java.util.ArrayList;
import java.util.List;

import tattler.pro.tattler.models.Contact;

public class ContactsUpdate extends InternalMessage {
    private static final long serialVersionUID = -483507476122864221L;
    public List<Contact> contacts;
    public Reason reason;

    public ContactsUpdate() {
        super(Type.CONTACTS_UPDATE);
        contacts = new ArrayList<>();
    }

    public enum Reason {
        ALL_CONTACTS_UPDATE,
        NEW_CONTACT_ADDED,
        CONTACT_ALREADY_ADDED,
        CONTACT_NOT_EXIST
    }
}
