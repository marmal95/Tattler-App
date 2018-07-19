package tattler.pro.tattler.contact;

import com.hannesdorfmann.mosby.mvp.MvpView;

import tattler.pro.tattler.models.Contact;

interface ContactView extends MvpView {
    void loadContactData(Contact contact);
}
