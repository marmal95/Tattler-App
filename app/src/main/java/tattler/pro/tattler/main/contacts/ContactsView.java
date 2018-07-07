package tattler.pro.tattler.main.contacts;

import com.hannesdorfmann.mosby.mvp.MvpView;

interface ContactsView extends MvpView {
    void setContactsAdapter(ContactsAdapter adapter);
    void startAddContactDialog();
}
