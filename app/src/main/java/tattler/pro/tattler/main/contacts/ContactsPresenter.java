package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.models.Contact;

import java.sql.SQLException;
import java.util.List;

public class ContactsPresenter extends MvpBasePresenter<ContactsView> {
    private ContactsAdapter contactsAdapter;
    private DatabaseManager databaseManager;

    ContactsPresenter(ContactsAdapter contactsAdapter, DatabaseManager databaseManager) {
        this.contactsAdapter = contactsAdapter;
        this.databaseManager = databaseManager;
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

    public void handleAddNewContact(String userNumber, String userName) {
        try {
            Contact contact = new Contact(userName, Integer.parseInt(userNumber));
            databaseManager.insertContact(contact);
            contactsAdapter.addContact(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initUserContacts() {
        try {
            List<Contact> contacts = databaseManager.selectContacts();
            contactsAdapter.addContacts(contacts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
