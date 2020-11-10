package com.example.trivialix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class GestionUsuarios extends AppCompatActivity implements View.OnClickListener {
    private Button atras_datos, aceptar_datos;
    private EditText passwordIntroducida, nombreIntroducido;
    private String nombreUsuario, password;
    private int opcion;
    private BaseDatos dbGlobal;
    private Bundle bolsa;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.datos_usuario);
        super.onCreate(savedInstanceState);
        dbGlobal = MainActivity.getDbGlobal();
        atras_datos = findViewById(R.id.volver_datos);
        aceptar_datos = findViewById(R.id.aceptar_datos);
        nombreIntroducido = findViewById(R.id.nombreUsuario);
        passwordIntroducida = findViewById(R.id.password);
        Intent recibe = getIntent();
        bolsa = recibe.getExtras();
        opcion = bolsa.getInt("opcion");

        atras_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
        aceptar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (opcion){
                    case 1: crearUsuario();
                    case 2: hacerLogin();
                    case 3: bajaUsuario();
                }
            }
        });


    }
    @Override
    public void onClick(View v) {

    }

    public void crearUsuario(){
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        boolean correcto = dbGlobal.crearUsuario(nombreUsuario, password);
        if (correcto){

            Toast.makeText(this,"Registro correcto",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this,"El usuario ya estaba registrado",Toast.LENGTH_SHORT).show();
        }


    }
    public void hacerLogin(){
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        if (dbGlobal.estaRegistrado(nombreUsuario)){
            if (dbGlobal.comprobarLogin(nombreUsuario, password)){
                Toast.makeText(this,"Bienvenido" + nombreUsuario,Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void bajaUsuario(){
        nombreIntroducido = findViewById(R.id.nombreUsuario);
        passwordIntroducida = findViewById(R.id.password);
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        boolean borrado = dbGlobal.borrarUsuario(nombreUsuario);
        if (borrado){
            Toast.makeText(this,"Usuario eliminado", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this,"Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
    public void volver(){
        Intent i=new Intent(this,OpcionesUsuarios.class);
        startActivity(i);
    }
}
