package com.baozi.baby.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.baozi.baby.app.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * sp存储 by baozi
 */
public class PreferencesUtil {

    private String mObjectCachePath =  MyApplication.getInstance().getApplicationContext().getExternalFilesDir("ObjectCache").getAbsolutePath();

    public static <T> void putPreferences(String key, T value) {
        SharedPreferences.Editor editor = MyApplication.userPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.apply();
    }

    public static <T> T getPreferences(String key, T value) {
        Object o = null;
        if (value instanceof String) {
            o = MyApplication.userPreferences.getString(key, value.toString());
        } else if (value instanceof Boolean) {
            o = MyApplication.userPreferences.getBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            o = MyApplication.userPreferences.getInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            o = MyApplication.userPreferences.getFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            o = MyApplication.userPreferences.getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }

    /**
     * 用户退出清除用户信息
     */
    public static void clear(){
        SharedPreferences.Editor editor = MyApplication.userPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 通过文件保存对象数据，对象必须可序列化。通过清理app数据即可清理掉数据
     * 目录:SDCard/Android/data/应用包名/data/files/ObjectCache
     */
    public void putObject(String key, Object value) {
        File objectFile = new File(mObjectCachePath + "/" + key);
        if (objectFile.exists()) {
            objectFile.delete();
        }
        try {
            objectFile.createNewFile();
            ObjectOutputStream obs = new ObjectOutputStream(new FileOutputStream(objectFile));
            obs.writeObject(value);
            obs.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("bz------------>", "文件流打开失败。");
        }
    }

    //-----------------------实体类存储-------------------------
    public Object getObject(String key) {
        File objectFile = new File(mObjectCachePath + "/" + key);
        if (!objectFile.exists()) {
            Log.i("bz------------>", "该对象没有被缓存");
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objectFile));
            Object result = ois.readObject();
            ois.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("bz------------>", "对象缓存读取失败");
        }
        return null;
    }

    public void clearCacheObject() {
        File cacheDir = new File(mObjectCachePath);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            deleteDir(cacheDir);
        }
    }

    protected void deleteDir(File dir) {
        if (dir.isDirectory() && dir.listFiles().length > 0) {
            for (File file : dir.listFiles()) {
                deleteDir(file);
            }
        } else {
            dir.delete();
        }

    }
}
