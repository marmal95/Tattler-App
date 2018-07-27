package tattler.pro.tattler.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.ContactChat;
import tattler.pro.tattler.models.Invitation;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "tattler.db";
    private static final int DATABASE_VERSION = 16;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Chat.class);
            TableUtils.createTable(connectionSource, ContactChat.class);
            TableUtils.createTable(connectionSource, Invitation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Contact.class, true);
            TableUtils.dropTable(connectionSource, Chat.class, true);
            TableUtils.dropTable(connectionSource, ContactChat.class, true);
            TableUtils.dropTable(connectionSource, Invitation.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> selectContacts() throws SQLException {
        List<Contact> contacts = getContactsDao().queryForAll();
        Logger.d("Selected " + contacts.size() + " contacts.");
        return contacts;
    }

    public List<Chat> selectChats() throws SQLException {
        List<Chat> chats = getChatsDao().queryForAll();
        Logger.d("Selected " + chats.size() + " chats.");
        return chats;
    }

    public List<Invitation> selectInvitations() throws SQLException {
        List<Invitation> invitations = getInvidationsDao().queryForAll();
        Logger.d("Selected " + invitations.size() + " invitations.");
        return invitations;
    }

    public void insertContact(Contact contact) throws SQLException {
        Dao<Contact, Integer> contacts = getContactsDao();
        contacts.create(contact);
        Logger.d("Inserted " + contact.toString());
    }

    public void insertChat(Chat chat) throws SQLException {
        Dao<Chat, Integer> chats = getChatsDao();
        chats.create(chat);
        Logger.d("Inserted " + chat.toString());
    }

    public void insertChat(Chat chat, List<tattler.pro.tattler.messages.models.Contact> contacts) throws SQLException {
        Dao<Chat, Integer> chats = getChatsDao();
        chats.create(chat);
        Logger.d("Inserted " + chat.toString());

        List<Contact> contactsToAdd = new ArrayList<>(contacts.size());
        contacts.forEach(contact -> contactsToAdd.add(new Contact(contact.contactName, contact.contactNumber)));

        for (Contact contact : contactsToAdd) {
            insertChatContact(chat, contact);
        }
    }

    public void insertChatContact(Chat chat, Contact contact) throws SQLException {
        Dao<ContactChat, Integer> contactChats = getContactChatsDao();
        ContactChat contactChat = new ContactChat(chat, contact);
        contactChats.create(contactChat);
        Logger.d("Inserted " + contactChat.toString());
    }

    public void insertInvitation(Invitation invitation) throws SQLException {
        Dao<Invitation, Integer> invitations = getInvidationsDao();
        invitations.create(invitation);
        Logger.d("Inserted " + invitation.toString());
    }

    public Optional<Chat> getIndividualChat(Contact contact) throws SQLException {
        QueryBuilder<ContactChat, Integer> contactChatQueryBuilder = getContactChatsDao().queryBuilder();
        contactChatQueryBuilder.where().eq("contact_number", contact.contactNumber);

        QueryBuilder<Chat, Integer> chatQueryBuilder = getChatsDao().queryBuilder();
        chatQueryBuilder.where().eq("is_group", false);

        chatQueryBuilder.join(contactChatQueryBuilder);
        Logger.i(chatQueryBuilder.prepareStatementString());

        List<Chat> selectedChats = getChatsDao().query(chatQueryBuilder.prepare());
        logSelectedIndividualChat(selectedChats, contact);
        return selectedChats.isEmpty() ? Optional.empty() : Optional.of(selectedChats.get(0));
    }

    public void updateContacts(List<tattler.pro.tattler.messages.models.Contact> contacts) {
        try {
            TableUtils.clearTable(connectionSource, Contact.class);
            for (tattler.pro.tattler.messages.models.Contact contact : contacts) {
                insertContact(new Contact(contact.contactName, contact.contactNumber));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Contact, Integer> getContactsDao() throws SQLException {
        return getDao(Contact.class);
    }

    private Dao<Chat, Integer> getChatsDao() throws SQLException {
        return getDao(Chat.class);
    }

    private Dao<ContactChat, Integer> getContactChatsDao() throws SQLException {
        return getDao(ContactChat.class);
    }

    private Dao<Invitation, Integer> getInvidationsDao() throws SQLException {
        return getDao(Invitation.class);
    }

    private void logSelectedIndividualChat(List<Chat> chats, Contact contact) {
        if (!chats.isEmpty()) {
            Logger.i("Selected individual chat: " + chats.get(0).toString());
        } else {
            Logger.i("Individual chat not found for contact: " + contact.toString());
        }
    }
}
