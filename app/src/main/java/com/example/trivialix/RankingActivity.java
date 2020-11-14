package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button volver;
    private TextView mostrarPuntuacion, nombre1, nombre2, nombre3, ranking1, ranking2, ranking3;
    private String nombreUsuario;
    private int puntuacion;
    private BaseDatos dbGlobal;
    private Bundle bolsa;
    private Intent recibe;


    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        dbGlobal = MainActivity.getDbGlobal();
        volver=findViewById(R.id.volver_ranking);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mostrarPuntuacion=findViewById(R.id.puntuacion_usuario);
        nombre1=findViewById(R.id.nombre1);
        nombre2=findViewById(R.id.nombre2);
        nombre3=findViewById(R.id.nombre3);
        ranking1=findViewById(R.id.ranking1);
        ranking2=findViewById(R.id.ranking2);
        ranking3=findViewById(R.id.ranking3);

        Usuarios user1 = dbGlobal.top1player();
        Usuarios user2 = dbGlobal.top2player();
        Usuarios user3 = dbGlobal.top3player();
        nombre1.setText(user1.getNombre());
        nombre2.setText(user2.getNombre());
        nombre3.setText(user3.getNombre());
        ranking1.setText(Integer.toString(user1.getRecord()));
        ranking2.setText(Integer.toString(user2.getRecord()));
        ranking3.setText(Integer.toString(user3.getRecord()));

        recibe=getIntent();
        bolsa=recibe.getExtras();
        assert bolsa != null;
        puntuacion=bolsa.getInt("puntuacion");
        if(puntuacion<=0){
            mostrarPuntuacion.setText("Su puntuación es: 0 puntos" );
        }else{
            mostrarPuntuacion.setText("Su puntuación es: " + puntuacion + " puntos");
        }
        mostrarPuntuacion.setText("Su puntuación es: " + puntuacion + " puntos");

    }
        @Override
        public void onClick (View v){
            Intent i=new Intent(this,MainActivity.class);
            i.putExtra("usuario",nombreUsuario);
            startActivity(i);
        }
    }
