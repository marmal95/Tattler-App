package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.AddContactRequest;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.models.Contact;

public class ContactsPresenter extends MvpBasePresenter<ContactsView> {
    private ContactsAdapter contactsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private MessageFactory messageFactory;

    ContactsPresenter(
            ContactsAdapter contactsAdapter,
            DatabaseManager databaseManager,
            MainPresenter mainPresenter,
            MessageFactory messageFactory) {
        this.contactsAdapter = contactsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.mainPresenter.setContactsPresenter(this);
        this.messageFactory = messageFactory;
    }

    @Override
    public void attachView(ContactsView view) {
        super.attachView(view);
        view.setContactsAdapter(contactsAdapter);
        initUserContacts();
    }

    public void onDestroy() {
        OpenHelperManager.releaseHelper();
    }

    @SuppressWarnings("ConstantConditions")
    public void handleAddContactButtonClick() {
        if (isViewAttached()) {
            getView().startAddContactDialog();
        }
    }

    public void handleAddNewContact(String contactNumber) {
        int contactPhoneId = Integer.parseInt(contactNumber);
        sendAddContactRequest(contactPhoneId);
    }

    public void handleLoginResponse(LoginResponse message) {
        if (!message.contacts.isEmpty()) {
            initUserContacts();
        }
    }

    public void handleAddContactResponse(AddContactResponse message) {
        try {
            Contact contact = databaseManager.selectContactByPhoneId(message.userNumber);
            contactsAdapter.addContact(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void handleContactClick(int position) {
        if (isViewAttached()) {
            getView().startContactActivity(contactsAdapter.getContact(position), position);
        }
    }

    private void initUserContacts() {
        try {
            List<Contact> contacts = databaseManager.selectContacts();
            contactsAdapter.clearContacts();
            contactsAdapter.addContacts(contacts);
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: Inform user about fail
        }
    }

    private void sendAddContactRequest(int contactPhoneId) {
        AddContactRequest addContactRequest = messageFactory.createAddContactRequest(contactPhoneId);
        mainPresenter.sendMessage(addContactRequest);
    }
}
