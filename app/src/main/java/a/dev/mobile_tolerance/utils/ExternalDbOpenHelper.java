package a.dev.mobile_tolerance.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import a.dev.mobile_tolerance.AppConst;


public class ExternalDbOpenHelper extends SQLiteOpenHelper {




    private final String DB_PATH;
    private SQLiteDatabase myDB;
    private final Context context;
    // Database Name
    public static final String DB_NAME = "tolerance.db";


    // Database Version
    private static final int DATABASE_VERSION = 3;

    public ExternalDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;

        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";


        openDataBase();
    }

    //Создаст базу, если она не создана
    void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying myDB!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }


    //Проверка сущестфывфыввования базы данных
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String DATABASE_PATH = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(DATABASE_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //Андроид не любит утечки ресурсов, все должно закрываться
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }
    //Метод копирования базы
    private void copyDataBase() throws IOException {
        // Открываем поток для чтения из уже созданной нами БД
        //источник в assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

        // Путь к уже созданной пустой базе в андроиде
        String outFileName = DB_PATH + DB_NAME;

        // Теперь создадим поток для записи в эту БД побайтно
        OutputStream localDbStream = new FileOutputStream(outFileName);

        // Собственно, копирование
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        // Мы будем хорошими мальчиками (девочками) и закроем потоки
       localDbStream.flush();
        localDbStream.close();
        externalDbStream.close();

    }
    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (myDB == null) {
            createDataBase();
            myDB = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return myDB;
    }
    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {




    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}