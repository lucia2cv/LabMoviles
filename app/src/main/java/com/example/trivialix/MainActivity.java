package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent recibe, inicioIntent, loginIntent, abrirManualIntent;
    private static BaseDatos dbGlobal;
    private Spinner tematica;
    private Button iniciarJuego;
    public static final int REQUESTCODEQUIZ=1;
    public static final String ID_TEMATICA="IDTematica";
    public static final String TEMATICA="NombreTematica";
    private Bundle bolsa;
    private TextView  user_nombre_main;
    private Toolbar toolbar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.trivialix_toolbar);
        tematica=findViewById(R.id.eligeTematica);
        iniciarJuego = findViewById(R.id.iniciarJuego);
        user_nombre_main = findViewById(R.id.user_nombre_main);
        user_nombre_main.setVisibility(View.INVISIBLE);
        iniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });
        dbGlobal = iniciarBBDD();
        cargarTematicas();
        inicioIntent =new Intent(this,QuizActivity.class);
        loginIntent =new Intent(this, OpcionesUsuarios.class);
        abrirManualIntent = new Intent(this, MostrarManual.class);
        try{
            recibe=getIntent();
            bolsa=recibe.getExtras();
            String usuario = bolsa.getString("nombreUsuario");
            if (dbGlobal.estaRegistrado(usuario)){
                user_nombre_main.setVisibility(View.VISIBLE);
                user_nombre_main.setText("Bienvenido, " + usuario);
            }
            inicioIntent.putExtra("nombreUsuario", usuario);
            loginIntent.putExtra("nombreUsuario", usuario);
            abrirManualIntent.putExtra("nombreUsuario", usuario);
        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem itemManual = menu.findItem(R.id.button_item_manual);
        MenuItem itemLogin = menu.findItem(R.id.button_item_login);
        Button btnManual = itemManual.getActionView().findViewById(R.id.button);
        Button btnLogin = itemLogin.getActionView().findViewById(R.id.button);
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirManual();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hacerLogin();
            }
        });
        return true;
    }

    public BaseDatos iniciarBBDD(){
        //Inicializar BBDD
        BaseDatos db = new BaseDatos(this, "trivialix.db", null, 1);
        try {
            db.createDataBase();

        } catch(IOException e) {

            e.printStackTrace();
        }
        return db;
    }

    public static BaseDatos getDbGlobal() {
        return dbGlobal;
    }

    private void cargarTematicas() {
        List<Tematicas> listaTematicas;
        if (dbGlobal != null){
            listaTematicas =dbGlobal.getAllTematicas();
            ArrayAdapter<Tematicas> adapterTematicas=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,listaTematicas);
            adapterTematicas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tematica.setAdapter(adapterTematicas);
        }
        else {
            System.out.println("Error al cargar las temáticas");
        }


    }
    public void abrirManual(){
        startActivity(abrirManualIntent);

    }
    public void hacerLogin(){
        startActivity(loginIntent);
    }

    private void inicio() {
        Tematicas tematicas=(Tematicas)tematica.getSelectedItem();
        int idTematica=tematicas.getId_tematica();
        String nombreTematica=tematicas.getNombreTematica();


        inicioIntent.putExtra(ID_TEMATICA,idTematica);
        inicioIntent.putExtra(TEMATICA,nombreTematica);
        startActivityForResult(inicioIntent,REQUESTCODEQUIZ);
    }
}