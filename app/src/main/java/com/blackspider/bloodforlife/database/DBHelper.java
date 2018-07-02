package com.blackspider.bloodforlife.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import com.blackspider.bloodforlife.models.Donor;
import com.blackspider.bloodforlife.models.Location;
import com.blackspider.bloodforlife.others.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr blackSpider on 12/22/2016.
 */

public class DBHelper extends SQLiteOpenHelper{
    private Context context;

    private static final String DATABASE_NAME = "bloodforlife.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppConstants.CREATE_TABLE_DONORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_DONORS);
        // Creating tables again
        onCreate(db);
    }

    // Checking table has data or not
    public int getRowCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = -1;
        Cursor cursor  = db.rawQuery("SELECT * FROM " + tableName, null);

        count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Checking table has the given data or not
    public boolean isInserted(String tableName, String index, String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = -1;
        Cursor cursor  = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + index + "=\"" + value.trim()+"\";", null);

        count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    public long saveDonor(String tableName, Donor donor){
        //int curr_row = getRowCount(AppConstants.TABLE_CHAT_LIST);
        boolean isInserted = isInserted(tableName, AppConstants.KEY_EMAIL, donor.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        long res = 0;
        ContentValues values = new ContentValues();
        values.put(AppConstants.KEY_NAME, donor.getName());
        values.put(AppConstants.KEY_EMAIL, donor.getEmail());
        values.put(AppConstants.KEY_PASSWORD, donor.getPassword());
        values.put(AppConstants.KEY_PHONE, donor.getPhone());
        values.put(AppConstants.KEY_BIRTH_DATE, donor.getBirthDate());
        values.put(AppConstants.KEY_WEIGHT, donor.getWeight());
        values.put(AppConstants.KEY_BLOOD_GROUP, donor.getBloodGroup());
        values.put(AppConstants.KEY_IMAGE, donor.getImage());
        values.put(AppConstants.KEY_CITY, donor.getCity());
        values.put(AppConstants.KEY_LAT, donor.getLat());
        values.put(AppConstants.KEY_LNG, donor.getLng());
        values.put(AppConstants.KEY_OCCUPATION, donor.getOccupation());
        values.put(AppConstants.KEY_INSTITUTE, donor.getInstitute());
        values.put(AppConstants.KEY_IS_DONOR, donor.isDonor());
        values.put(AppConstants.KEY_AVAILABLE_HOUR, donor.getAvailableHour());
        values.put(AppConstants.KEY_LAST_DONATION_DATE, donor.getLastDonationDate());
        values.put(AppConstants.KEY_TOTAL_DONATION, donor.getTotalDonation());
        values.put(AppConstants.KEY_IS_DRUG_ADDICTED, donor.isDrugAddicted());
        values.put(AppConstants.KEY_DISEASES, donor.getDiseases());

        if(!isInserted){
            // Inserting Row
            res = db.insert(tableName, null, values);
            Log.d("Donor instered: ",donor.getName());
        }else{
            res = -2;
        }

        // Closing database connection
        db.close();

        return res;
    }

    public int updateColumnValue(String tableName, int id, String column, String newValue){
        int updated = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column, newValue);

        updated = db.update(tableName, values, AppConstants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
        return updated;
    }

    public boolean updateLocation(String tableName, int id, Location location){
        int updated = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstants.KEY_CITY, location.getCity());
        values.put(AppConstants.KEY_LAT, location.getLat());
        values.put(AppConstants.KEY_LNG, location.getLng());

        updated = db.update(tableName, values, AppConstants.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
        return updated > 0;
    }

    public Donor getValidDonor(String tableName, String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isValid = false;
        Donor donor = new Donor();
        String query = "SELECT * FROM " + tableName + " WHERE " + AppConstants.KEY_EMAIL + "=\"" + email.trim()+"\" AND "
                + AppConstants.KEY_PASSWORD + "='" + password +"';";
        Cursor cursor  = db.rawQuery(query, null);

        if(cursor.getCount() > 0){
            if (cursor != null){
                if(cursor.moveToFirst()){
                    isValid = true;
                    donor.setId(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_ID)));
                    donor.setName(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_NAME)));
                    donor.setEmail(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_EMAIL)));
                    donor.setPhone(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_PHONE)));
                    donor.setBirthDate(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_BIRTH_DATE)));
                    donor.setWeight(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_WEIGHT)));
                    donor.setBloodGroup(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_BLOOD_GROUP)));
                    donor.setImage(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IMAGE)));
                    donor.setCity(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY)));
                    donor.setLat(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LAT)));
                    donor.setLng(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LNG)));
                    donor.setOccupation(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_OCCUPATION)));
                    donor.setInstitute(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_INSTITUTE)));
                    donor.setDonor(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IS_DONOR))));
                    donor.setAvailableHour(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AVAILABLE_HOUR)));
                    donor.setLastDonationDate(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LAST_DONATION_DATE)));
                    donor.setTotalDonation(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_TOTAL_DONATION)));
                    donor.setDrugAddicted(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IS_DRUG_ADDICTED))));
                    donor.setDiseases(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_DISEASES)));
                }
            }
        }

        cursor.close();
        db.close();

        if(!isValid) return null;

        return donor;
    }

    // Getting donors
    public List<Donor> getDonors(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Donor> donors = new ArrayList<>();
        Donor donor;

        String query = "SELECT * FROM " + tableName + ";";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null){
            if(cursor.moveToFirst()){
                do {
                    donor = new Donor();
                    donor.setId(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_ID)));
                    donor.setName(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_NAME)));
                    donor.setEmail(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_EMAIL)));
                    donor.setPhone(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_PHONE)));
                    donor.setBirthDate(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_BIRTH_DATE)));
                    donor.setWeight(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_WEIGHT)));
                    donor.setBloodGroup(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_BLOOD_GROUP)));
                    donor.setImage(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IMAGE)));
                    donor.setCity(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_CITY)));
                    donor.setLat(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LAT)));
                    donor.setLng(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LNG)));
                    donor.setOccupation(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_OCCUPATION)));
                    donor.setInstitute(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_INSTITUTE)));
                    donor.setDonor(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IS_DONOR))));
                    donor.setAvailableHour(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_AVAILABLE_HOUR)));
                    donor.setLastDonationDate(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_LAST_DONATION_DATE)));
                    donor.setTotalDonation(cursor.getInt(cursor.getColumnIndex(AppConstants.KEY_TOTAL_DONATION)));
                    donor.setDrugAddicted(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_IS_DRUG_ADDICTED))));
                    donor.setDiseases(cursor.getString(cursor.getColumnIndex(AppConstants.KEY_DISEASES)));

                    Log.d("Chat list retrieving: ",donor.getName());
                    donors.add(donor);

                }while (cursor.moveToNext());

            }
        }

        cursor.close();
        db.close();

        return donors;
    }

    public boolean deleteDonor(String tableName, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        int deleted = db.delete(tableName, AppConstants.KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

        // Closing database connection
        db.close();

        return deleted > 0;
    }

    // Getting master_password
    public String getColValue(String tableName, int id, String column, String newValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY_GET_IMAGE_PATH = "SELECT " + column + " FROM " + tableName +
                " WHERE " + AppConstants.KEY_ID + "=" + id + ";";
        Cursor cursor = db.rawQuery(QUERY_GET_IMAGE_PATH, null);

        if (cursor != null){
            if(cursor.moveToFirst()){
                return cursor.getString(cursor.getColumnIndex(column));
            }
        }

        cursor.close();
        db.close();

        return null;
    }
}
