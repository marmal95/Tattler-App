package tattler.pro.tattler.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orhanobut.logger.Logger;
import tattler.pro.tattler.models.Chat;
import tattler.pro.tattler.models.Contact;
import tattler.pro.tattler.models.ContactChat;

import java.sql.SQLException;
import java.util.List;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "tattler.db";
    private static final int DATABASE_VERSION = 7;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Contact.class);
            TableUtils.createTable(connectionSource, Chat.class);
            TableUtils.createTable(connectionSource, ContactChat.class);
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

    public void insertContact(Contact contact) throws SQLException {
        Dao<Contact, Integer> contacts = getContactsDao();
        contacts.create(contact);
        Logger.d("Inserted " + contact.toString());
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
}
