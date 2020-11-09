package com.example.trivialix;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {
    private static String DB_PATH = "data/data/com.example.trivialix/databases/";
    private static String DB_NOMBRE = "prueba.db";
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
        if (bbddExiste){
            System.out.println("Ya existe la base de datos" + "\n" + "Abriendo base de datos...");
            openDataBase();
        }else{
            this.getReadableDatabase();
            try{
                copyDataBase();
                openDataBase();
            }catch (IOException e){
                throw new Error("Error copiando database");
            }
        }
        //return this;
    }

    private boolean checkDataBase() {
        //comprobar si existe la BBDD interna
        SQLiteDatabase checkDB = null;
        try{
            String path = DB_PATH + DB_NOMBRE;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e){
            System.out.println("Base de datos no creada todavía");
        }

        if (checkDB != null){
            checkDB.close();
        }
        return  checkDB !=null;
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

        dataBaseInputStream = context.getAssets().open("prueba.db");
        while ((length =dataBaseInputStream.read(buffer)) > 0) {
            dataBaseOutputStream.write(buffer);
        }
        dataBaseInputStream.close();
        dataBaseOutputStream.flush();
        dataBaseOutputStream.close();
    }

    //obtener las tematicas
    public List<Tematicas> getAllTematicas(){
        String myQuery = "select * from Tematicas";
        Cursor cursor;
        List<Tematicas>temas = new ArrayList<>();
        cursor = BBDD.rawQuery(myQuery, null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()){
                Tematicas tematicas=new Tematicas(cursor.getInt(cursor.getColumnIndex("id_tematica")),
                        cursor.getString(cursor.getColumnIndex("nombreTematica")));
                temas.add(tematicas);
                cursor.moveToNext();
            }
        }
        cursor.close();
        BBDD.close();
        return temas;
    }
}
