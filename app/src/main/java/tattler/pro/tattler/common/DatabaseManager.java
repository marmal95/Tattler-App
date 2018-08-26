package tattler.pro.tattler.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
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
import tattler.pro.tattler.models.Message;
import tattler.pro.tattler.models.Participant;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "tattler.db";
    private static final int DATABASE_VERSION = 116;

    private Dao<Chat, Integer> chatsDao;
    private Dao<Contact, Integer> contactsDao;
    private Dao<Invitation, Integer> invitationsDao;
    private Dao<Participant, Integer> participantsDao;
    private Dao<Message, Integer> messagesDao;

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
        Logger.d("Selected " + contacts.size() + " contacts.");
        return contacts;
    }

    public List<Chat> selectInitializedChats() throws SQLException {
        List<Chat> chats = getChatsDao().queryForAll();
        Logger.d("Selected " + chats.size() + " chats.");
        return chats;
    }

    public Optional<Chat> selectChatById(int chatId) throws SQLException {
        Chat chat = getChatsDao().queryForId(chatId);
        return chat != null ? Optional.of(chat) : Optional.empty();
    }

    public List<Invitation> selectInvitations() throws SQLException {
        List<Invitation> invitations = getInvitationsDao().queryForAll();
        Logger.d("Selected " + invitations.size() + " invitations.");
        return invitations;
    }

    public void insertContact(Contact contact) throws SQLException {
        getContactsDao().createOrUpdate(contact);
        Logger.d("Inserted " + contact.toString());
    }

    public void insertChat(Chat chat, List<tattler.pro.tattler.messages.models.Contact> contacts) throws SQLException {
        getChatsDao().assignEmptyForeignCollection(chat, "participants");
        getChatsDao().assignEmptyForeignCollection(chat, "messages");
        getChatsDao().assignEmptyForeignCollection(chat, "invitations");
        contacts.forEach(contact -> chat.participants.add(new Participant(contact.contactNumber, contact.contactName, chat)));

        getChatsDao().createIfNotExists(chat);
        Logger.d("Inserted " + chat.toString());
    }

    public void updateInvitation(Invitation invitation) throws SQLException {
        getInvitationsDao().update(invitation);
        Logger.d("Inserted " + invitation.toString());
    }

    public Optional<Chat> getIndividualChat(Contact contact) throws SQLException {
        QueryBuilder<Participant, Integer> participantQueryBuilder = getParticipantsDao().queryBuilder();
        participantQueryBuilder.where().eq("contact_number", contact.contactNumber);

        QueryBuilder<Chat, Integer> chatQueryBuilder = getChatsDao().queryBuilder();
        chatQueryBuilder.where().eq("is_group", false);

        chatQueryBuilder.join(participantQueryBuilder);
        Logger.i(chatQueryBuilder.prepareStatementString());

        Chat selectedChat = getChatsDao().queryForFirst(chatQueryBuilder.prepare());
        Optional<Chat> optionalChat = selectedChat != null ? Optional.of(selectedChat) : Optional.empty();
        logSelectedIndividualChat(optionalChat, contact);
        return optionalChat;
    }

    public List<Contact> updateContacts(List<tattler.pro.tattler.messages.models.Contact> contacts) throws SQLException {
        TableUtils.clearTable(connectionSource, Contact.class);
        List<Contact> newContacts = new ArrayList<>();

        for (tattler.pro.tattler.messages.models.Contact contact : contacts) {
            Contact newContact = new Contact(contact.contactName, contact.contactNumber);
            insertContact(newContact);
            newContacts.add(newContact);
        }

        return newContacts;
    }

    public void updateChat(Chat chat) throws SQLException {
        getChatsDao().update(chat);
        Logger.d("Updated: " + chat.toString());
    }

    public void deleteContact(Contact contact) throws SQLException {
        getContactsDao().delete(contact);
        Logger.d("Deleted: " + contact.toString());
    }

    public void deleteChat(Chat chat) throws SQLException {
        DeleteBuilder<Invitation, Integer> invitationsDeleteBuilder = getInvitationsDao().deleteBuilder();
        invitationsDeleteBuilder.where().eq("chat", chat.chatId);
        getInvitationsDao().delete(invitationsDeleteBuilder.prepare());

        DeleteBuilder<Participant, Integer> participantsDeleteBuilder = getParticipantsDao().deleteBuilder();
        participantsDeleteBuilder.where().eq("chat", chat.chatId);
        getParticipantsDao().delete(participantsDeleteBuilder.prepare());

        DeleteBuilder<Message, Integer> messagesDeleteBuilder = getMessagesDao().deleteBuilder();
        messagesDeleteBuilder.where().eq("chat", chat.chatId);
        getMessagesDao().delete(messagesDeleteBuilder.prepare());

        getChatsDao().delete(chat);
        Logger.d("Removed: " + chat.toString());
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

    private Dao<Message, Integer> getMessagesDao() throws SQLException {
        if (messagesDao == null) { messagesDao = getDao(Message.class);}
        return messagesDao;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private void logSelectedIndividualChat(Optional<Chat> optionalChat, Contact contact) {
        if (optionalChat.isPresent()) {
            Logger.i("Selected individual chat: " + optionalChat.get().toString());
        } else {
            Logger.i("Individual chat not found for contact: " + contact.toString());
        }
    }

    private void createTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, Contact.class);
        TableUtils.createTable(connectionSource, Chat.class);
        TableUtils.createTable(connectionSource, Participant.class);
        TableUtils.createTable(connectionSource, Invitation.class);
        TableUtils.createTable(connectionSource, Message.class);
    }

    private void dropTables(ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, Contact.class, true);
        TableUtils.dropTable(connectionSource, Chat.class, true);
        TableUtils.dropTable(connectionSource, Participant.class, true);
        TableUtils.dropTable(connectionSource, Invitation.class, true);
        TableUtils.dropTable(connectionSource, Message.class, true);
    }
}
