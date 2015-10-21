package com.marcos.autodatabases.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.marcos.autodatabases.SQLiteHelper;
import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.models.Model;


/**
 * Created by marcos on 11/17/14.
 */
public class DatabaseHelper {
    private final String TAG_DB_NAME = "AA_DB_NAME";
    private final String TAG_DB_VERSION = "AA_DB_VERSION";
    private static Context mContext;
    private static DatabaseHelper mInstance = null;
    private static SQLiteHelper mHelper; //todo why static?
    private SQLiteDatabase mDb;
    private static String DATABASE_NAME = "my_database.db";
    private int DATABASE_VERSION = 1;
    private boolean hasNoModels = true;

    private DatabaseHelper() {
        if ( null == mContext ){
            throw new NullPointerException(
                    "DatabaseHelper must have a Context reference before being used!");
        }
        setDatabaseNameAndVersionFromManifest();
        mHelper = new SQLiteHelper(mContext, DATABASE_NAME, DATABASE_VERSION);
    }

    public static void setupContext(Context context){
        mContext = context;
    }

    public void createDatabase() {
        if ( hasNoModels){
            throw new IllegalStateException(
                    "At least one Model subclass must be added to the database before creation!");
        }
        SQLiteDatabase db = getWritable(); //getWritable creates a database
        db.close();
    }

    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper();
        }
        return mInstance;
    }

    public String getDatabaseName() {
        return mHelper.getDatabaseName();
    }

    @SafeVarargs
    public final void addModel(Class<? extends Model>... aClasses) {
        if ( aClasses.length > 0){
            hasNoModels = false;
        }

        ModelsInfo mModelsInfo = ModelsInfo.getInstance();
        for (Class<? extends Model> model : aClasses) {
            mModelsInfo.addModel(model);
        }
    }

    public void executeSQL(String command) {
        mDb = mHelper.getWritableDatabase();
        mDb.execSQL(command);
        closeDatabase();
    }

    public Cursor query(String query) {
        mDb = getReadable();
        return mDb.rawQuery(query, null);
    }

    public void closeDatabase() {
        if (mDb != null && mDb.isOpen()) {
            mDb.close();
        }
    }

    private SQLiteDatabase getReadable() { //TODO package local?
        return mHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritable() {
        return mHelper.getWritableDatabase();
    }

    private void setDatabaseNameAndVersionFromManifest(){
        Bundle bundle = null;
        try {
            Log.e("PACKAGE NAME", mContext.getPackageName());
            ApplicationInfo appInfo = mContext.getPackageManager().
                    getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);

            bundle = appInfo.metaData;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        if (bundle != null) {
            if  (bundle.containsKey(TAG_DB_NAME)){
                DATABASE_NAME = bundle.getString(TAG_DB_NAME);
            }
            if  (bundle.containsKey(TAG_DB_VERSION)){
                DATABASE_VERSION = bundle.getInt(TAG_DB_VERSION);
            }
        }
        Log.d("Database Name ",DATABASE_NAME );
        Log.d("Database Version"," Version " + DATABASE_VERSION );
    }


}
