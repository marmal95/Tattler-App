package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import tattler.pro.tattler.common.AppPreferences;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.AddContactRequest;
import tattler.pro.tattler.messages.AddContactResponse;
import tattler.pro.tattler.messages.LoginResponse;
import tattler.pro.tattler.models.Contact;

public class ContactsPresenter extends MvpBasePresenter<ContactsView> {
    private ContactsAdapter contactsAdapter;
    private DatabaseManager databaseManager;
    private MainPresenter mainPresenter;
    private AppPreferences appPreferences;

    ContactsPresenter(ContactsAdapter contactsAdapter, DatabaseManager databaseManager, MainPresenter mainPresenter, AppPreferences appPreferences) {
        this.contactsAdapter = contactsAdapter;
        this.databaseManager = databaseManager;
        this.mainPresenter = mainPresenter;
        this.appPreferences = appPreferences;

        this.mainPresenter.setContactsPresenter(this);
    }

    @Override
    public void attachView(ContactsView view) {
        super.attachView(view);
        view.setContactsAdapter(contactsAdapter);
        initUserContacts();

        // TODO: Remove and uncomment after tests`
        contactsAdapter.addContact(new Contact("Andrzej", 1));
        contactsAdapter.addContact(new Contact("Stefan", 2));
        contactsAdapter.addContact(new Contact("Ziomek", 2));
        contactsAdapter.addContact(new Contact("Yolo", 2));
        contactsAdapter.addContact(new Contact("Typek", 2));
    }

    public void onDestroy() {
        OpenHelperManager.releaseHelper();
    }

    @SuppressWarnings("ConstantConditions")
    public void handleAddContactButtonClick() {
        // TODO: Remove and uncomment after tests`
        contactsAdapter.addContact(new Contact("Andrzej", 1));
//        if (isViewAttached()) {
//            getView().startAddContactDialog();
//        }
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
        Contact contact = new Contact(message.userName, message.userNumber);
        contactsAdapter.addContact(contact);
    }

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
        int userPhoneId = appPreferences.getInt(AppPreferences.Key.USER_NUMBER);
        AddContactRequest addContactRequest = new AddContactRequest(contactPhoneId, userPhoneId);
        mainPresenter.sendMessage(addContactRequest);
    }
}
