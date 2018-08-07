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
import tattler.pro.tattler.models.Invitation;
import tattler.pro.tattler.models.Participant;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "tattler.db";
    private static final int DATABASE_VERSION = 49;

    private Dao<Chat, Integer> chatsDao;
    private Dao<Contact, Integer> contactsDao;
    private Dao<Invitation, Integer> invitationsDao;
    private Dao<Participant, Integer> participantsDao;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            createTables(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            dropTables(connectionSource);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> selectContacts() throws SQLException {
        List<Contact> contacts = getContactsDao().queryForAll();
        Logger.d("Selected " + contacts.size() + " contactsDao.");
        return contacts;
    }

    public Contact selectContactByPhoneId(int phoneId) throws SQLException {
        return getContactsDao().queryForId(phoneId);
    }

    public List<Chat> selectInitializedChats() throws SQLException {
        List<Chat> chats = getChatsDao().queryBuilder().where().eq("is_initialized", true).query();
        Logger.d("Selected " + chats.size() + " chatsDao.");
        return chats;
    }

    public Chat selectChatById(int chatId) throws SQLException {
        return getChatsDao().queryForId(chatId);
    }

    public List<Invitation> selectInvitations() throws SQLException {
        List<Invitation> invitations = getInvitationsDao().queryForAll();
        Logger.d("Selected " + invitations.size() + " invitationsDao.");
        return invitations;
    }

    public Invitation selectInvitationForChatInvitation(long chatInvitationId) throws SQLException {
        return getInvitationsDao().queryBuilder()
                .where().eq("invitation_message_id", chatInvitationId).queryForFirst();
    }

    public void insertContact(Contact contact) throws SQLException {
        getContactsDao().createOrUpdate(contact);
        Logger.d("Inserted " + contact.toString());
    }

    public void insertChat(Chat chat, List<tattler.pro.tattler.messages.models.Contact> contacts) throws SQLException {
        getChatsDao().createOrUpdate(chat); // TODO: Check if this removes participantsDao, invitationsDao etc... on update
        Logger.d("Inserted " + chat.toString());

        List<Contact> contactsToAdd = new ArrayList<>(contacts.size());
        contacts.forEach(contact -> contactsToAdd.add(new Contact(contact.contactName, contact.contactNumber)));

        for (Contact contact : contactsToAdd) {
            insertChatParticipant(chat, contact);
        }
    }

    public void insertChatParticipant(Chat chat, Contact contact) throws SQLException {
        Participant participant = new Participant(contact.contactNumber, contact.contactName, chat);
        getParticipantsDao().create(participant);
        Logger.d("Inserted " + participant.toString());
    }

    public void insertInvitation(Invitation invitation) throws SQLException {
        getInvitationsDao().createOrUpdate(invitation);
        Logger.d("Inserted " + invitation.toString());
    }

    public Optional<Chat> getIndividualChat(Contact contact) throws SQLException {
        QueryBuilder<Participant, Integer> participantQueryBuilder = getParticipantsDao().queryBuilder();
        participantQueryBuilder.where().eq("contact_number", contact.contactNumber);

        QueryBuilder<Chat, Integer> chatQueryBuilder = getChatsDao().queryBuilder();
        chatQueryBuilder.where().eq("is_group", false);

        chatQueryBuilder.join(participantQueryBuilder);
        Logger.i(chatQueryBuilder.prepareStatementString());

        List<Chat> selectedChats = getChatsDao().query(chatQueryBuilder.prepare());
        logSelectedIndividualChat(selectedChats, contact);
        return selectedChats.isEmpty() ? Optional.empty() : Optional.of(selectedChats.get(0));
    }

    public void updateContacts(List<tattler.pro.tattler.messages.models.Contact> contacts) throws SQLException {
        TableUtils.clearTable(connectionSource, Contact.class);
        for (tattler.pro.tattler.messages.models.Contact contact : contacts) {
            insertContact(new Contact(contact.contactName, contact.contactNumber));
        }
    }

    public void updateChat(Chat chat) {
        try {
            getChatsDao().update(chat);
            Logger.d("Updated: " + chat.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Contact, Integer> getContactsDao() throws SQLException {
        if (contactsDao == null) { contactsDao = getDao(Contact.class);}
        return contactsDao;
    }

    private Dao<Chat, Integer> getChatsDao() throws SQLException {
        if (chatsDao == null) { chatsDao = getDao(Chat.class);}
        return chatsDao;
    }

    private Dao<Invitation, Integer> getInvitationsDao() throws SQLException {
        if (invitationsDao == null) { invitationsDao = getDao(Invitation.class);}
        return invitationsDao;
    }

    private Dao<Participant, Integer> getParticipantsDao() throws SQLException {
        if (participantsDao == null) { participantsDao = getDao(Participant.class);}
        return participantsDao;
    }

    private void logSelectedIndividualChat(List<Chat> chats, Contact contact) {
        if (!chats.isEmpty()) {
            Logger.i("Selected individual chat: " + chats.get(0).toString());
        } else {
            Logger.i("Individual chat not found for contact: " + contact.toString());
        }
    }

    private void createTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, Contact.class);
        TableUtils.createTable(connectionSource, Chat.class);
        TableUtils.createTable(connectionSource, Participant.class);
        TableUtils.createTable(connectionSource, Invitation.class);
    }

    private void dropTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, Contact.class, true);
        TableUtils.dropTable(connectionSource, Chat.class, true);
        TableUtils.dropTable(connectionSource, Participant.class, true);
        TableUtils.dropTable(connectionSource, Invitation.class, true);
    }
}
