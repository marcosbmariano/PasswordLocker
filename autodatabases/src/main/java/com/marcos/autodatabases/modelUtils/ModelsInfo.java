package com.marcos.autodatabases.modelUtils;


import com.marcos.autodatabases.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcos on 11/13/14.
 */
public class ModelsInfo {

    private static ModelsInfo instance = null;
    private static List<ModelInfo> mModelsInfo;
    private static List<Class<? extends Model>> classList;
    private final HashMap<String, Class<? extends Model>> mAllRelationalTables;

    private ModelsInfo() {
        classList = new ArrayList<>();
        mModelsInfo = new ArrayList<>();
        mAllRelationalTables = new HashMap<>();
    }

    public static ModelsInfo getInstance() {

        if (instance == null) {
            instance = new ModelsInfo();
        }
        return instance;
    }

    public void addModel(Class<? extends Model> aClass) {
        classList.add(aClass);
        ModelInfo info = new ModelInfo(aClass);
        mModelsInfo.add(info);
        mAllRelationalTables.putAll(info.getRelationalTables());
    }

    public String getTableFromClass(Class<? extends Model> aClass) {
        String tableName = "";
        for (ModelInfo info : mModelsInfo) {
            if (info.getClassName().equals(aClass.getName())) {
                tableName = info.getTableName();
            }
        }
        return tableName;
    }

    public List<ModelInfo> getModelsInfo() {
        return mModelsInfo;
    }


    public String getRelationalTableFromClass (Class<? extends Model> aClass){
        String relativeTable = "";

        if (mAllRelationalTables.containsValue(aClass)){
            for ( String key : mAllRelationalTables.keySet()){
                if (mAllRelationalTables.get(key).equals(aClass)){
                    relativeTable = key;
                }
            }
        }
        return relativeTable;
    }

    public Class<? extends Model> getClassFromTable(String tableName) {

        for (ModelInfo info : mModelsInfo) {
            if (info.getTableName().equals(tableName)) {
                return info.getModelClass();
            }
        }
        return null;
    }

    public List<String> getSchemas() {
        List<String> list = new ArrayList<>();
        List<ModelInfo> modelsList = getModelsInfo();
        for (ModelInfo models : modelsList) {
            list.addAll(models.getSchemas());
        }
        return list;
    }

    public ModelInfo getModelInfo(Class<? extends Model> aClass) {
        String className = aClass.getName();

        for (ModelInfo model : mModelsInfo) {
            if (className.equals(model.getClassName())) {
                return model;
            }
        }
        return null;
    }
}
