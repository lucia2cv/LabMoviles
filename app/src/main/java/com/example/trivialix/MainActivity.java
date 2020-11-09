package com.example.trivialix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Intent i;
    public BaseDatos dbGlobal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i=new Intent(MainActivity.this,Pregunta1.class);
        Button iniciarJuego = findViewById(R.id.iniciarJuego);
        iniciarJuego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        dbGlobal = iniciarBBDD();

    }

    public BaseDatos iniciarBBDD(){
        //Inicializar BBDD
        BaseDatos db = new BaseDatos(this, "dbTrivialix", null, 1);
        try {
            db.createDataBase();
            db.openDataBase();
        } catch(IOException e) {

            e.printStackTrace();
        }
        return db;
    }


    public void inicio(View view){
        final Intent i=new Intent(MainActivity.this,Pregunta1.class);
        startActivity(i);
        dbGlobal.getReadableDatabase();
        System.out.println("hola");
        System.out.println(databaseList());

    }
}