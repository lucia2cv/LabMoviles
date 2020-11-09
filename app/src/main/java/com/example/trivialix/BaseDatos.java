package com.example.trivialix;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BaseDatos extends SQLiteOpenHelper {
    private static String DB_PATH = "data/data/packageName/databases";
    private static String DB_NOMBRE = "dbTrivialix";
    private SQLiteDatabase BBDD;
    private final Context context;

    public BaseDatos(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
        this.context = contexto;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Nada
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Para los cambios en esctructura
    }
    public void createDataBase () throws IOException {
        boolean bbddExiste = checkDataBase();
    }

    private boolean checkDataBase() {
        //comprobar si existe la BBDD interna
        SQLiteDatabase checkDB = null;
        try{
            String path = DB_PATH + DB_NOMBRE;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e){
            //Base no creada todavia
        }

        if (checkDB != null){
            checkDB.close();
        }
        return  checkDB !=null ? true:false;
    }

    public void openDataBase() throws SQLException {
        String path = DB_PATH + DB_NOMBRE;
        BBDD = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
    }
    public synchronized void close() {

        if (BBDD != null)
            BBDD.close();
        super.close();
    }

    private void copyDataBase() throws IOException {
        OutputStream dataBaseOutputStream = new FileOutputStream( ""+ DB_PATH + DB_NOMBRE);
        InputStream  dataBaseInputStream;

        byte[] buffer = new byte[1024];
        int length;

        dataBaseInputStream = context.getAssets().open("trivialix2");
        while ((length =dataBaseInputStream.read(buffer)) > 0) {
            dataBaseOutputStream.write(buffer);
        }
        dataBaseInputStream.close();
        dataBaseOutputStream.flush();
        dataBaseOutputStream.close();
    }
}
