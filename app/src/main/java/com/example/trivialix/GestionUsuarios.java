package com.example.trivialix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class GestionUsuarios extends AppCompatActivity implements View.OnClickListener {
    private Button atras_datos, aceptar_datos;
    private EditText passwordIntroducida, nombreIntroducido;
    private TextView accionUsuario;
    private String nombreUsuario, password;
    private int opcion;
    private BaseDatos dbGlobal;
    private Bundle bolsa;
    private Intent volverIntent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.datos_usuario);
        super.onCreate(savedInstanceState);
        dbGlobal = MainActivity.getDbGlobal();
        atras_datos = findViewById(R.id.volver_datos);
        aceptar_datos = findViewById(R.id.aceptar_datos);
        nombreIntroducido = findViewById(R.id.nombreUsuario);
        passwordIntroducida = findViewById(R.id.password);
        accionUsuario = findViewById(R.id.accion_usuario);
        volverIntent=new Intent(this,OpcionesUsuarios.class);
        Intent recibe = getIntent();
        bolsa = recibe.getExtras();
        opcion = bolsa.getInt("opcion");
        try{
            String usuario = bolsa.getString("nombreUsuario");
            volverIntent.putExtra("nombreUsuario", usuario);
            if ((dbGlobal.estaRegistrado(usuario))&& (opcion == 2)){
                cerrarSesion();
            }

        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }

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
                    case 1:
                        crearUsuario();
                        break;
                    case 2:
                        hacerLogin();
                        break;
                    case 3:
                        bajaUsuario();
                        break;
                }
            }
        });

        switch (opcion){
            case 1:
                accionUsuario.setText("Regístrate");
                break;
            case 2:
                accionUsuario.setText("Accede a tu cuenta");
                break;
            case 3:
                accionUsuario.setText("Borra tu cuenta");
                break;
        }




    }
    @Override
    public void onClick(View v) {
    }

    public void crearUsuario(){
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        if ((!nombreUsuario.equals("")) && (!password.equals(""))){
            boolean correcto = dbGlobal.crearUsuario(nombreUsuario, password);
            if (correcto){

                Toast.makeText(this,"Registro correcto",Toast.LENGTH_SHORT).show();
                nombreIntroducido.setText("Nombre");
                passwordIntroducida.setText("contraseña");
                Intent i= new Intent(this, MainActivity.class);
                i.putExtra("nombreUsuario", nombreUsuario);
                startActivity(i);


            } else {
                Toast.makeText(this,"El usuario ya estaba registrado",Toast.LENGTH_SHORT).show();
                nombreIntroducido.setText("Nombre");
                passwordIntroducida.setText("contraseña");
            }
        } else{
            Toast.makeText(this,"Introduzca el nombre y la contraseña",Toast.LENGTH_SHORT).show();
        }



    }
    public void hacerLogin(){
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        if ((!nombreUsuario.equals("")) && (!password.equals(""))){
            if (dbGlobal.estaRegistrado(nombreUsuario)){
                if (dbGlobal.comprobarLogin(nombreUsuario, password)){
                    Toast.makeText(this,"Bienvenido " + nombreUsuario,Toast.LENGTH_SHORT).show();
                    nombreIntroducido.setText("Nombre");
                    passwordIntroducida.setText("contraseña");
                    Intent i= new Intent(this, MainActivity.class);
                    i.putExtra("nombreUsuario", nombreUsuario);
                    startActivity(i);
                } else{
                    Toast.makeText(this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                    nombreIntroducido.setText("Nombre");
                    passwordIntroducida.setText("contraseña");
                }
            } else{
                Toast.makeText(this,"Usuario no registrado",Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(this,"Introduzca el nombre y la contraseña",Toast.LENGTH_SHORT).show();
        }
    }

    public void bajaUsuario(){
        nombreIntroducido = findViewById(R.id.nombreUsuario);
        passwordIntroducida = findViewById(R.id.password);
        nombreUsuario = nombreIntroducido.getText().toString();
        password = passwordIntroducida.getText().toString();
        if ((!nombreUsuario.equals("")) && (!password.equals(""))){
            boolean borrado = dbGlobal.borrarUsuario(nombreUsuario, password);
            if (borrado){
                Toast.makeText(this,"Usuario eliminado", Toast.LENGTH_SHORT).show();
                nombreIntroducido.setText("Nombre");
                passwordIntroducida.setText("contraseña");
            } else{
                Toast.makeText(this,"Usuario no encontrado", Toast.LENGTH_SHORT).show();
                nombreIntroducido.setText("Nombre");
                passwordIntroducida.setText("contraseña");
            }

        } else{
            Toast.makeText(this,"Introduzca el nombre y la contraseña",Toast.LENGTH_SHORT).show();
        }
    }
    public void cerrarSesion(){
        Intent cerrarSesion =new Intent(this, MainActivity.class);
        startActivity(cerrarSesion);
    }

    public void volver(){
        startActivity(volverIntent);
    }
}
