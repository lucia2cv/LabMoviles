package com.example.trivialix;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Comparator;

public class Usuarios {
    private int id_user, record;
    private String nombre, password;

    public Usuarios(int id_user, String nombre, String password, int record){
        this.id_user = id_user;
        this.record = record;
        this.nombre = nombre;
        this.password = password;

    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class UsuariosComparator implements Comparator<Usuarios> {

       public UsuariosComparator() {
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int compare(Usuarios o1, Usuarios o2) {
            return Integer.compare(o1.getRecord(), o2.getRecord());
        }
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "id_user=" + id_user +
                ", record=" + record +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
