package com.htlc.cykf.app.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.htlc.cykf.app.App;
import com.htlc.cykf.app.bean.CityBean;
import com.htlc.cykf.app.util.LogUtil;
import com.htlc.cykf.model.NetworkCityBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/4.
 */
public class ProvinceDao {
    private static final String TableName = "area";

    public ArrayList<CityBean> getProvinces() {
        SQLiteDatabase database = DbManager.getInstance().openDatabase();
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {"1"};
        Cursor cursor = database.query("area", columns, "TYPE=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while (cursor.moveToNext()) {
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        DbManager.getInstance().closeDatabase();
        return beans;
    }

    public ArrayList<CityBean> getCities(int parentId) {
        SQLiteDatabase database = DbManager.getInstance().openDatabase();
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {parentId + ""};
        Cursor cursor = database.query("area", columns, "PARENT_ID=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while (cursor.moveToNext()) {
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        DbManager.getInstance().closeDatabase();
        return beans;
    }

    public ArrayList<CityBean> getCounties(int parentId) {
        SQLiteDatabase database = DbManager.getInstance().openDatabase();
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {parentId + ""};
        Cursor cursor = database.query("area", columns, "PARENT_ID=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while (cursor.moveToNext()) {
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        DbManager.getInstance().closeDatabase();
        return beans;
    }

    public void updateCityListTable(ArrayList<NetworkCityBean> list) {
        SQLiteDatabase database = DbManager.getInstance().openDatabase();
        database.beginTransaction();
        for (int i = 0; i < list.size(); i++) {
            NetworkCityBean bean = list.get(i);
            if ("3".equals(bean.flag)) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("AREA_CODE", bean.id);
                contentValues.put("AREA_NAME", bean.cityname);
                contentValues.put("TYPE", bean.type);
                contentValues.put("PARENT_ID", bean.pid);
                database.update(TableName, contentValues, "AREA_CODE=?", new String[]{bean.id});
            } else if ("2".equals(bean.flag)) {
                database.delete(TableName, "AREA_CODE=?", new String[]{bean.id});
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("AREA_CODE", bean.id);
                contentValues.put("AREA_NAME", bean.cityname);
                contentValues.put("TYPE", bean.type);
                contentValues.put("PARENT_ID", bean.pid);
                database.insert(TableName, null, contentValues);
            }
        }
        database.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
        database.endTransaction(); // 处理完成
        DbManager.getInstance().closeDatabase();
    }
}
