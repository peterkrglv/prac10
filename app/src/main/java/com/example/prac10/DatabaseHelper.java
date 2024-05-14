package com.example.prac10;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "users";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_AGE = "age";
    private static final String DATABASE_NAME = "Users.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_PHONE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_AGE + " TEXT, " +
                COLUMN_EMAIL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_EMAIL, user.getEmail());
        cv.put(COLUMN_PHONE, Integer.valueOf(user.getPhone()));
        cv.put(COLUMN_AGE, user.getAge());
        cv.put(COLUMN_GENDER, user.getGender());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result != -1;
    }

    public boolean deleteUser(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_PHONE + " = ?",
                new String[]{phone});
        db.close();
        return result > 0;
    }


    public User findUser(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new
                        String[]{COLUMN_PHONE,
                        COLUMN_NAME,
                        COLUMN_GENDER,
                        COLUMN_AGE,
                        COLUMN_EMAIL},
                COLUMN_PHONE + " = ?", new String[]{phone}, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4));
            cursor.close();
            db.close();
            return user;
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, user.getName());
        cv.put(COLUMN_PHONE, user.getPhone());
        cv.put(COLUMN_GENDER, user.getGender());
        cv.put(COLUMN_AGE, user.getAge());
        cv.put(COLUMN_EMAIL, user.getEmail());
        int result = db.update(TABLE_NAME, cv, COLUMN_PHONE + " =?", new String[]{String.valueOf(user.getPhone())});
                db.close();
        return result > 0;
    }

}
