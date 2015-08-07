package com.marcos.autodatabases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.marcos.autodatabases.modelUtils.ModelsInfo;
import java.util.List;

/**
 * Created by marcos on 11/13/14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private final ModelsInfo mModelsInfo;


    public SQLiteHelper(Context context, String databaseName, int version) {
        super(context, databaseName, null, version);
        mModelsInfo = ModelsInfo.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> schemas = mModelsInfo.getSchemas();
        for (String schema : schemas) {
            Log.d("DATABASE TRANSACTION", "Schema:" + schema);
            db.execSQL(schema);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //TODO !!!!!

    }




}
