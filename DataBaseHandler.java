package com.project.vactionbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHandler extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = " Camera_imagedb";

    // Contacts table name
    private static final String TABLE_CONTACTS = " Camera_contacts";
    private static final String TABLE_MEMORY_INFO = " Memory_info";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_IMAGE_ID = "image_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " BLOB" + ")";
        String CREATE_MEMORY_INFO = "CREATE TABLE " + TABLE_MEMORY_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT"
                 + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_MEMORY_INFO);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMORY_INFO);
        // Create tables again
        onCreate(db);
    }


    //Adding memory Info
    public int updateMemory(MemoryDTO memoryDTO) {
    Log.i("COnatc Memr Info", memoryDTO.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        // db = dbH.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_ID, memoryDTO._id);
        values.put(KEY_DATE, memoryDTO._memoryDate);
        values.put(KEY_DESCRIPTION, memoryDTO._memoryDescription);
        values.put(KEY_TITLE, memoryDTO._memoryTitle);
        /*int res=db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(memoryDTO._id) });*/
     long id=   db.insert(TABLE_MEMORY_INFO, null, values);
        Log.i("Inserting Memory Info",id+"");
        return (int)id;

    }

    /**
     * All CRUD(Create, Read) Operations
     */

    public// Adding new memoryDTO
    void addContact(MemoryDTO memoryDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, memoryDTO._name); // MemoryDTO Name
        values.put(KEY_IMAGE, memoryDTO._image); // MemoryDTO Phone

        // Inserting Row
        long id=db.insert(TABLE_CONTACTS, null, values);
        Log.i("Inserting memoryDTO",id+"");
        db.close(); // Closing database connection
    }



    public MemoryDTO getMemoryInfo1(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MEMORY_INFO + " WHERE "
                + KEY_IMAGE_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        String title=c.getString(c.getColumnIndex(KEY_TITLE));
        String date=c.getString(c.getColumnIndex(KEY_DATE));
        String description=c.getString(c.getColumnIndex(KEY_DESCRIPTION));
       MemoryDTO memoryDTO =new MemoryDTO(title,description,date);
        Log.i("MemoryDTO from Memry", memoryDTO.toString());
        return memoryDTO;
    }


    // Getting All Contacts
    public List<MemoryDTO> getAllContacts() {
        List<MemoryDTO> memoryDTOList = new ArrayList<MemoryDTO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Camera_contacts";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MemoryDTO memoryDTO = new MemoryDTO();
                memoryDTO.setID(Integer.parseInt(cursor.getString(0)));
                memoryDTO.setName(cursor.getString(1));
                memoryDTO.setImage(cursor.getBlob(2));
                // Adding memoryDTO to list
                if(memoryDTO._id>0){
                MemoryDTO memoryDTOInfo =getMemoryInfo1((int) memoryDTO._id);
                Log.i("COntact memory Info", memoryDTOInfo.toString());
                memoryDTO._memoryDate= memoryDTOInfo._memoryDate;
                memoryDTO._memoryDescription= memoryDTOInfo._memoryDescription;
                memoryDTO._memoryTitle= memoryDTOInfo._memoryTitle;
                Log.i("COntact Fetcheted", memoryDTO.toString());}
                memoryDTOList.add(memoryDTO);

            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return memoryDTO list
        return memoryDTOList;
    }


    public List<MemoryDTO> getAllContacts1() {
        List<MemoryDTO> memoryDTOList = new ArrayList<MemoryDTO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Camera_contacts ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MemoryDTO memoryDTO = new MemoryDTO();
                memoryDTO.setID(Integer.parseInt(cursor.getString(0)));
                memoryDTO.setName(cursor.getString(1));
                memoryDTO.setImage(cursor.getBlob(2));
                // Adding memoryDTO to list
                memoryDTOList.add(memoryDTO);

            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return memoryDTO list
        return memoryDTOList;
    }

    // Getting single memoryDTO
    MemoryDTO getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_IMAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MemoryDTO memoryDTO = new MemoryDTO(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getBlob(1));

        // return memoryDTO
        return memoryDTO;

    }


    MemoryDTO getMemoryInfo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MEMORY_INFO, new String[] { KEY_DATE,
                        KEY_DESCRIPTION, KEY_TITLE }, KEY_IMAGE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MemoryDTO memoryDTO =new MemoryDTO()
                ;
        memoryDTO._memoryTitle=cursor.getString(2);
        memoryDTO._memoryDescription=cursor.getString(1);
        memoryDTO._memoryDate=cursor.getString(0);
      /*  MemoryDTO memoryDTO = new MemoryDTO((cursor.getString(2)),
                cursor.getString(1), cursor.getString(0));
*/
        // return memoryDTO
        return memoryDTO;

    }
}