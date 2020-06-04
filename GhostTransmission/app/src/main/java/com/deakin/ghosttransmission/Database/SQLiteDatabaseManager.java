package com.deakin.ghosttransmission.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.deakin.ghosttransmission.Ghosting.Data.IdentityJuggler;

import java.util.Locale;

public class SQLiteDatabaseManager extends SQLiteOpenHelper {

    /**
     * Constants
     */
    // VERSION
    private final int VERSION = 1;

    // DATABASE
    private final String DATABASE_NAME = "GT";
    private final String TABLE_IDENTITIES = "identity";
    private final String TABLE_IDENTITIES_KEY_ADD = "address";
    private final String TABLE_IDENTITIES_VALUE_ID = "id";

    /**
     * Instance Variables
     */
    // CONTEXT
    private Context context = null;

    /**
     * Constructor
     */
    public SQLiteDatabaseManager(Context context) {
        super(context, "GT", null, 1);
    }

    /**
     * Creates a new database instance (if not existent)
     *
     * @param db database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IDENTITY_TABLE = String.format(Locale.getDefault(), "CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT UNIQUE)",
                TABLE_IDENTITIES, TABLE_IDENTITIES_KEY_ADD, TABLE_IDENTITIES_VALUE_ID);
        execSQL(CREATE_IDENTITY_TABLE, db);
    }

    /**
     * Called in the case of a schema (structure) mismatch (i.e. upgrade required)
     *
     * @param db         database instance
     * @param oldVersion old version number
     * @param newVersion new version numbers
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop the identity table if it exists
        String DROP_IDENTITY_TABLE = String.format(Locale.getDefault(), "DROP TABLE IF EXISTS %s", TABLE_IDENTITIES);
        execSQL(DROP_IDENTITY_TABLE, db);

        // recreate the database
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // recreate database
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Execute the given SQL command on the given database instance, with exception handling if
     * the command is unable to be executed
     *
     * @param command command to execute
     * @param db      database instance
     * @return whether the command was executed successfully
     */
    public boolean execSQL(String command, SQLiteDatabase db) {
        // attempt to execute the provided command
        db.beginTransaction();
        try {
            db.execSQL(command);
            db.setTransactionSuccessful();
        } catch (SQLException sqlExc) {
            Toast.makeText(getContext(), "Application error, please restart, and try again", Toast.LENGTH_LONG).show();
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    /**
     * Retrieves and/or creates a new Identity for the given address
     *
     * @param address address to find identity of
     * @return found / created Identity
     */
    public String getIdentity(String address) {
        // query the database for the Identity of the given address
        Cursor c = getReadableDatabase().query(TABLE_IDENTITIES,
                new String[]{TABLE_IDENTITIES_VALUE_ID},
                "address = ?", new String[]{address},
                "", "", "", "");

        String identity = "";

        // attempt to move to the first item in the Cursor, and and the relative identity
        if (c.moveToFirst())
            identity = c.getString(c.getColumnIndex(TABLE_IDENTITIES_VALUE_ID));
        else { // if the cursor has no rows (i.e. the given address has no identity, create a new one with the JuggleDrop algorithm)
            identity = IdentityJuggler.juggleDropIdentity(address);

            // update database with new identity for given address
            SQLiteDatabase db = getWritableDatabase();
            String command = String.format(Locale.getDefault(), "INSERT INTO %s VALUES (\"%s\", \"%s\");", TABLE_IDENTITIES, address, identity);
            execSQL(command, db);
        }

        c.close();

        return identity;
    }

    /**
     * Retrieves the associated Address for a given Identity
     *
     * @param identity identity to find address of
     * @return found address
     */
    public String getAddress(String identity) {
        // query the database for the Identity of the given address
        Cursor c = getReadableDatabase().query(TABLE_IDENTITIES,
                new String[]{TABLE_IDENTITIES_KEY_ADD},
                "id = ?", new String[]{identity},
                "", "", "", "");

        String address = "";

        // attempt to move to the first item in the Cursor, and retrieve the associated address
        if (c.moveToFirst())
            address = c.getString(c.getColumnIndex(TABLE_IDENTITIES_KEY_ADD));

        c.close();

        // modify the address to meet the Content Provider address format (+61)
        address = address.replaceFirst("[0]", "+61");

        return address;
    }

    public String updateIdentity(String address, String newIdentity) {
        ContentValues cv = new ContentValues();
        cv.put(TABLE_IDENTITIES_VALUE_ID, newIdentity);

        String command = String.format(Locale.getDefault(), "UPDATE %s SET %s = '%s' WHERE %s = '%s'",
                TABLE_IDENTITIES, TABLE_IDENTITIES_VALUE_ID, newIdentity, TABLE_IDENTITIES_KEY_ADD, address);
        execSQL(command, getWritableDatabase());

        // retrieve the new value of the identity from the database (ensuring the value was updated)
        String identity = getIdentity(address);
        return identity;
    }

    /**
     * Getters and Setters
     */
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
