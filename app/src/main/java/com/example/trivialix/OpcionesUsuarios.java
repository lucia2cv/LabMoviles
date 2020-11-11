package com.example.trivialix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OpcionesUsuarios extends AppCompatActivity implements View.OnClickListener {
    private Button atras_login, nuevoUsuario, borrarUsuario, acceder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.opciones_usuarios);
        super.onCreate(savedInstanceState);
        atras_login = findViewById(R.id.volver_login);
        nuevoUsuario = findViewById(R.id.registro);
        borrarUsuario = findViewById(R.id.borrar);
        acceder = findViewById(R.id.login);
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
        Intent i = new Intent(this, GestionUsuarios.class);
        i.putExtra("opcion",1);
        startActivity(i);


    }
    public void hacerLogin(){
        Intent i = new Intent(this, GestionUsuarios.class);
        i.putExtra("opcion",2);
        startActivity(i);


    }

    public void bajaUsuario(){
        Intent i = new Intent(this, GestionUsuarios.class);
        i.putExtra("opcion",3);
        startActivity(i);

    }
    public void volver(){
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
