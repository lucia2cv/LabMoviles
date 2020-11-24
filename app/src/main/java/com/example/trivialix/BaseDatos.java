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
import java.util.Collections;
import java.util.List;

public class BaseDatos extends SQLiteOpenHelper {
    private static String DB_PATH = "data/data/com.example.trivialix/databases/";
    private static String DB_NOMBRE = "trivialix.db";
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //Para los cambios en estructura
    }


    public void createDataBase () throws IOException {
        boolean bbddExiste = checkDataBase();
        if (bbddExiste){
            System.out.println("Ya existe la base de datos" + "\n" + "Abriendo base de datos...");
            openDataBase();
        }else{
            this.getWritableDatabase();
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
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
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
        BBDD = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
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

        dataBaseInputStream = context.getAssets().open("trivialix.db");
        while ((length =dataBaseInputStream.read(buffer)) > 0) {
            dataBaseOutputStream.write(buffer);
        }
        dataBaseInputStream.close();
        dataBaseOutputStream.flush();
        dataBaseOutputStream.close();
    }

    //Obtener las temáticas
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
        return temas;
    }

    //Obtener las preguntas
    public List<Preguntas>getAllPreguntas(int id_tematica){
        List<Preguntas>listaDePreguntas=new ArrayList<>();
        String myQuery = "SELECT * FROM  Preguntas";
        BBDD=getReadableDatabase();
        switch (id_tematica){
            case 1 : myQuery = "SELECT * FROM  Preguntas WHERE id_tema = 1";
                    break;
            case 2: myQuery = "SELECT * FROM  Preguntas WHERE id_tema = 2";
                break;
            case 3: myQuery = "SELECT * FROM  Preguntas WHERE id_tema = 3";
                break;
            case 4: myQuery = "SELECT * FROM  Preguntas WHERE id_tema = 4";
                break;
            case 5: myQuery = "SELECT * FROM Preguntas WHERE id_tema = 5";
                break;
            case 6: myQuery = "SELECT * FROM Preguntas WHERE id_tema = 6";
                break;

        }
        Cursor cursor=BBDD.rawQuery(myQuery,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()){
                Preguntas pregunta=new Preguntas(cursor.getInt(cursor.getColumnIndex("id_pregunta")),
                        cursor.getString(cursor.getColumnIndex("enunciado")),
                        cursor.getString(cursor.getColumnIndex("imagen")),
                        cursor.getString(cursor.getColumnIndex("audio")),
                        cursor.getString(cursor.getColumnIndex("OpcionA")),
                        cursor.getString(cursor.getColumnIndex("OpcionB")),
                        cursor.getString(cursor.getColumnIndex("OpcionC")),
                        cursor.getString(cursor.getColumnIndex("OpcionD")),
                        cursor.getString(cursor.getColumnIndex("OpcionCorrecta")),
                        cursor.getInt(cursor.getColumnIndex("TieneImagenAudio")),
                        cursor.getInt(cursor.getColumnIndex("id_tema")));
                listaDePreguntas.add(pregunta);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return listaDePreguntas;
    }

    //Obtener los usuarios
    public List<Usuarios> getAllUsuarios() {
        String myQuery = "select * from Usuarios";
        Cursor cursor;
        List<Usuarios> usuarios = new ArrayList<>();
        cursor = BBDD.rawQuery(myQuery, null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Usuarios usuario = new Usuarios(cursor.getInt(cursor.getColumnIndex("id_usuario")),
                        cursor.getString(cursor.getColumnIndex("nombreUsuario")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getInt(cursor.getColumnIndex("record")));
                usuarios.add(usuario);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return usuarios;

    }

    //Obtener una lista de los nombres de los usuarios
    public ArrayList<String> nombresUsuario(){
        List<Usuarios> usuarios = getAllUsuarios();
        ArrayList<String> nombresUsuarios = new ArrayList<>();
        for (Usuarios u: usuarios){
            nombresUsuarios.add(u.getNombre());

        }
        return nombresUsuarios;
    }


    //Comprobar si un usuario está registrado
    public boolean estaRegistrado(String nombreUser){
        ArrayList<String> nombres = nombresUsuario();
        return  nombres.contains(nombreUser);

    }

    //Comprobar contraseña y usuario

    public boolean comprobarLogin(String nombre, String password){
        if (estaRegistrado(nombre)) {
            String myQuery = "select password from Usuarios where nombreUsuario= '" + nombre + "'";
            Cursor cursor = BBDD.rawQuery(myQuery, null);
            cursor.moveToFirst();
            String passwordBBDD =  cursor.getString(0);
            if (passwordBBDD.equals(password)){
                return true;
            }
            cursor.close();
        }

        return false;

    }

    //Crear un usuario
    public boolean crearUsuario(String nombre, String password){
        List<Usuarios> usuarios = getAllUsuarios();
        int numUsuarios = usuarios.size();
        Usuarios ultimoUsuario = getAllUsuarios().get(numUsuarios-1);
        int id_user = ultimoUsuario.getId_user()+1;
        if (!estaRegistrado(nombre)) {
            String myQuery = "insert into Usuarios (id_usuario, nombreUsuario, password, record) values (" + String.valueOf(id_user) + ", " + "'"+ nombre + "', '" + password + "', 0)";
            BBDD.execSQL(myQuery);
            return true;
        } else{
            return false;
        }

    }
    //Dar de baja un usuario
    public boolean borrarUsuario(String nombreUsuario, String password){
        if(comprobarLogin(nombreUsuario, password)){
            String myQuery = "delete from Usuarios  where nombreUsuario = '" + nombreUsuario +"'";
            BBDD.execSQL(myQuery);
            return true;
        } else{
            return false;
        }

    }

    //Obtener el mejor jugador por ranking
    public Usuarios top1player(){
        List<Usuarios> usuarios = getAllUsuarios();
        Collections.sort(usuarios, new Usuarios.UsuariosComparator());
        Collections.reverse(usuarios);
        return usuarios.get(0);
    }

    //Obtener el segundo mejor jugador por ranking
    public Usuarios top2player(){
        List<Usuarios> usuarios = getAllUsuarios();
        Collections.sort(usuarios, new Usuarios.UsuariosComparator());
        Collections.reverse(usuarios);
        return usuarios.get(1);

    }

    //Obtener el tercer mejor jugador por ranking
    public Usuarios top3player(){
        List<Usuarios> usuarios = getAllUsuarios();
        Collections.sort(usuarios, new Usuarios.UsuariosComparator());
        Collections.reverse(usuarios);
        return usuarios.get(2);

    }

    //Guardar el ranking
    public void guardarRanking(String nombre, int ranking){
        String myQuery = "select record from Usuarios where nombreUsuario= '" + nombre + "'";
        Cursor cursor = BBDD.rawQuery(myQuery, null);
        cursor.moveToFirst();
        int recordBBDD =  cursor.getInt(0);
        if (ranking > recordBBDD) {
            String myQuery2 = "update Usuarios set record='"+ ranking +"' where nombreUsuario= '" + nombre + "'";
            BBDD.execSQL(myQuery2);

        }
        cursor.close();

    }


}
