package com.studdburgerindustries.sqlliteapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);

    }

    //gets called when you access a database object
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INTEGER, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";
        db.execSQL(createTableStatement);
    }
    //called whenever version number of application changes. prevents users from breaking app when db design changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS student_table");
        onCreate(db);
    }

    public boolean addOne(CustomerModel customerModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);

        if (insert == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean deleteOne(CustomerModel customerModel){
        //if customer model found in db delete it and return true, else return false
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){ //you are looking at the wrong sample

            return true;
        }
        else{
            return false;
        }

    }

    public List<CustomerModel> getEveryone(){
        List<CustomerModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null); //cursor in SQLite is result set

        if (cursor.moveToFirst()){
            //if true loop through results (cursor) and put into return list
            do{
                int customerId = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1? true:false;

                CustomerModel customerModel;
                customerModel = new CustomerModel(customerId,customerName,customerAge,customerActive);
                returnList.add(customerModel);
            }
            while(cursor.moveToNext());
        }
        else{
            //do nothing
        }
        //close cursor and db when done
        cursor.close();
        db.close();
        return returnList;
    }
}
