package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OpcionesUsuarios extends AppCompatActivity implements View.OnClickListener {
    private Button atras_login, nuevoUsuario, borrarUsuario, acceder;
    private Bundle bolsa;
    private Intent recibe, crearUsuarioIntent, hacerLoginIntent, bajaUsuarioIntent, volverIntent;
    private BaseDatos dbGlobal;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.opciones_usuarios);
        super.onCreate(savedInstanceState);
        dbGlobal = MainActivity.getDbGlobal();
        atras_login = findViewById(R.id.volver_login);
        nuevoUsuario = findViewById(R.id.registro);
        borrarUsuario = findViewById(R.id.borrar);
        acceder = findViewById(R.id.login);
        volverIntent =new Intent(this,MainActivity.class);
        hacerLoginIntent = new Intent(this, GestionUsuarios.class);
        bajaUsuarioIntent = new Intent(this, GestionUsuarios.class);
        crearUsuarioIntent = new Intent(this, GestionUsuarios.class);

        recibe=getIntent();
        bolsa=recibe.getExtras();
        try{
            String usuario = bolsa.getString("nombreUsuario");
            if (dbGlobal.estaRegistrado(usuario)){
                acceder.setText("Cerrar sesión");
            }
            hacerLoginIntent.putExtra("nombreUsuario", usuario);
            volverIntent.putExtra("nombreUsuario", usuario);
            bajaUsuarioIntent.putExtra("nombreUsuario", usuario);
            crearUsuarioIntent.putExtra("nombreUsuario", usuario);


        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }
        nuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });
        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerLogin();
            }
        });
        borrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajaUsuario();
            }
        });
        atras_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }
    @Override
    public void onClick(View v) {

    }

    public void crearUsuario(){
        crearUsuarioIntent.putExtra("opcion",1);
        startActivity(crearUsuarioIntent);


    }
    public void hacerLogin(){
        hacerLoginIntent.putExtra("opcion",2);
        startActivity(hacerLoginIntent);


    }

    public void bajaUsuario(){
        bajaUsuarioIntent.putExtra("opcion",3);
        startActivity(bajaUsuarioIntent);

    }
    public void volver(){
        startActivity(volverIntent);
    }
}
