package com.example.trivialix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent recibe, inicioIntent;
    private static BaseDatos dbGlobal;
    private Spinner tematica;
    private Button ayuda, login, iniciarJuego;
    public static final int REQUESTCODEQUIZ=1;
    public static final String ID_TEMATICA="IDTematica";
    public static final String TEMATICA="NombreTematica";
    private Bundle bolsa;
    private TextView  user_nombre_main;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tematica=findViewById(R.id.eligeTematica);
        ayuda = findViewById(R.id.ayuda);
        login = findViewById(R.id.login_main);
        iniciarJuego = findViewById(R.id.iniciarJuego);
        user_nombre_main = findViewById(R.id.user_nombre_main);
        user_nombre_main.setVisibility(View.INVISIBLE);
        iniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });
        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirManual();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerLogin();
            }
        });
        dbGlobal = iniciarBBDD();
        cargarTematicas();
        inicioIntent =new Intent(this,QuizActivity.class);
        try{
            recibe=getIntent();
            bolsa=recibe.getExtras();
            String usuario = bolsa.getString("nombreUsuario");
            if (usuario != null){
                user_nombre_main.setVisibility(View.VISIBLE);
                user_nombre_main.setText("Bienvenido, " + usuario);
            }
            inicioIntent.putExtra("nombreUsuario", usuario);
        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }


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
        List<Tematicas> listaTematicas = new ArrayList<>();
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
        Intent i=new Intent(this, MostrarManual.class);
        startActivity(i);

    }
    public void hacerLogin(){
        Intent i=new Intent(this, OpcionesUsuarios.class);
        startActivity(i);
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