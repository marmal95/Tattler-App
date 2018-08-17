package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import tattler.pro.tattler.common.DatabaseManager;
import tattler.pro.tattler.main.MainPresenter;
import tattler.pro.tattler.messages.AddContactRequest;
import tattler.pro.tattler.messages.MessageFactory;
import tattler.pro.tattler.messages.RemoveContactsRequest;
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

    public void handleContactsUpdate(List<Contact> contacts) {
        contactsAdapter.clearContacts();
        contactsAdapter.addContacts(contacts);
    }

    public void handleContactAdded(Contact contact) {
        contactsAdapter.addContact(contact);
    }

    @SuppressWarnings("ConstantConditions")
    public void handleContactClick(int position) {
        if (contactsAdapter.isInSelectMode()) {
            contactsAdapter.toggleSelection(position);
        } else {
            startContactActivity(contactsAdapter.getContact(position), position);
        }
    }

    public void handleContactLongClick(int position) {
        contactsAdapter.toggleSelection(position);
    }

    @SuppressWarnings("ConstantConditions")
    public void showContactAlreadyAddedInfo() {
        if (isViewAttached()) {
            getView().showContactAlreadyAddedInfo();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void showContactNotExistInfo() {
        if (isViewAttached()) {
            getView().showContactNotExistInfo();
        }
    }

    public void handleContactsRemoveClick() {
        sendRemoveContactsRequest(contactsAdapter.getSelectedItems());
        List<Integer> contactsIndexes = contactsAdapter.getSelectedPositions();
        removeContacts(contactsIndexes);
        contactsAdapter.clearSelection();
    }

    private void initUserContacts() {
        try {
            List<Contact> contacts = databaseManager.selectContacts();
            contactsAdapter.clearContacts();
            contactsAdapter.addContacts(contacts);
        } catch (SQLException e) {
            e.printStackTrace();
            showContactAddingError();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showContactAddingError() {
        if (isViewAttached()) {
            getView().showContactAddingError();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void startContactActivity(Contact contact, int position) {
        if (isViewAttached()) {
            getView().startContactActivity(contact, position);
        }
    }

    private void sendAddContactRequest(int contactPhoneId) {
        AddContactRequest addContactRequest = messageFactory.createAddContactRequest(contactPhoneId);
        mainPresenter.sendMessage(addContactRequest);
    }

    private void sendRemoveContactsRequest(List<Contact> contacts) {
        RemoveContactsRequest removeContactsRequest = messageFactory.createRemoveContactRequest(contacts);
        mainPresenter.sendMessage(removeContactsRequest);
    }

    private void removeContacts(List<Integer> contactsIndexes) {
        Collections.reverse(contactsIndexes);
        contactsIndexes.forEach(index -> {
            Contact contact = contactsAdapter.getContact(index);
            try {
                contactsAdapter.removeContact(index);
                databaseManager.deleteContact(contact);
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.e("Could not delete contact: " + contact.toString());
            }
        });
    }
}
