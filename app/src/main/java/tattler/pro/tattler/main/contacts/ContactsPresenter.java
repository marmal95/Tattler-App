package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import tattler.pro.tattler.models.Contact;

public class ContactsPresenter extends MvpBasePresenter<ContactsView> {
    private ContactsAdapter contactsAdapter;

    ContactsPresenter(ContactsAdapter contactsAdapter) {
        this.contactsAdapter = contactsAdapter;
    }

    @Override
    public void attachView(ContactsView view) {
        super.attachView(view);
        view.setContactsAdapter(contactsAdapter);
        // TODO: Remove after tests
        contactsAdapter.addContact(new Contact("Testowy Ziomek1", "123456", 21));
        contactsAdapter.addContact(new Contact("Noob", "123456", 22));
        contactsAdapter.addContact(new Contact("Brzydkie kaczątko", "123456", 23));
    }

    @SuppressWarnings("ConstantConditions")
    public void handleAddContactButtonClick() {
        if (isViewAttached()) {
            getView().startAddContactDialog();
        }
    }
}
