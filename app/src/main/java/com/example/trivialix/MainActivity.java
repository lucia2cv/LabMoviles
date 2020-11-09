package com.example.trivialix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Intent i;
    private BaseDatos dbGlobal;
    private TextView tematica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tematica = findViewById(R.id.tematicas);
        i = new Intent(MainActivity.this, Pregunta1.class);
        Button iniciarJuego = findViewById(R.id.iniciarJuego);
        iniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        dbGlobal = iniciarBBDD();
        cargarTematicas();

    }

    public BaseDatos iniciarBBDD(){
        //Inicializar BBDD
        BaseDatos db = new BaseDatos(this, "prueba.db", null, 1);
        try {
            db.createDataBase();

        } catch(IOException e) {

            e.printStackTrace();
        }
        return db;
    }

    private void cargarTematicas() {
        List<Tematicas> listaTematicas = new ArrayList<>();
        Iterator it;
        String texto= "";
        int x = 0;
        if (dbGlobal != null){
            listaTematicas =dbGlobal.getAllTematicas();
            it = listaTematicas.iterator();
            for (Tematicas l: listaTematicas){
                texto= texto + l.getNombreTematica() + "\n";
            }
        }
        else {
            System.out.println("ERROR AL CARGAR LAS TEMATICAS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            texto = "No hay temáticas";
        }

        tematica.setText(texto);

    }
}