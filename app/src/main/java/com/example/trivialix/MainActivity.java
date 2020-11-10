package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent i;
    public static BaseDatos dbGlobal;
    private Spinner tematica;
    public static final int REQUESTCODEQUIZ=1;
    public static final String ID_TEMATICA="IDTematica";
    public static final String TEMATICA="NombreTematica";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tematica=findViewById(R.id.eligeTematica);
        Button iniciarJuego = findViewById(R.id.iniciarJuego);
        iniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });
        dbGlobal = iniciarBBDD();
        cargarTematicas();

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

    private void inicio() {
        Tematicas tematicas=(Tematicas)tematica.getSelectedItem();
        int idTematica=tematicas.getId_tematica();
        String nombreTematica=tematicas.getNombreTematica();

        Intent i=new Intent(this,QuizActivity.class);
        i.putExtra(ID_TEMATICA,idTematica);
        i.putExtra(TEMATICA,nombreTematica);
        startActivityForResult(i,REQUESTCODEQUIZ);
    }
}