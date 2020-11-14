package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pl.droidsonroids.gif.GifImageView;

public class FinDeJuego extends AppCompatActivity implements View.OnClickListener {
    private Button verRanking,atras;
    private TextView mostrarPuntuacion;
    private int puntuacion=0;
    private Intent i,recibe,vueltaAtras;
    private Bundle bolsa;
    private GifImageView gifBuenaPuntacion,gifMalaPuntacion, gifPuntacionNormal;
    private  String nombreUsuario;


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findeljuego);
        atras=findViewById(R.id.volver);
        verRanking= findViewById(R.id.ranking_buttom);
        mostrarPuntuacion=findViewById(R.id.puntuacionFinal);
        gifBuenaPuntacion=findViewById(R.id.buenaPuntuacion);
        gifMalaPuntacion=findViewById(R.id.malaPuntuacion);
        gifPuntacionNormal=findViewById(R.id.puntuacionNormal);
        gifMalaPuntacion.setVisibility(View.INVISIBLE);
        gifBuenaPuntacion.setVisibility(View.INVISIBLE);
        gifPuntacionNormal.setVisibility(View.INVISIBLE);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });

        verRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarRanking();
            }
        });

        try {
        }catch(Exception e){
            Toast.makeText(this,"Error al recibir datos",Toast.LENGTH_SHORT).show();
        }
        recibe=getIntent();
        bolsa=recibe.getExtras();
        assert bolsa != null;
        puntuacion=bolsa.getInt("puntuacion");
        if(puntuacion<=0){
            mostrarPuntuacion.setText("Su puntuación es: 0 puntos" );
        }else{
            mostrarPuntuacion.setText("Su puntuación es: " + puntuacion + " puntos");
        }

        if(puntuacion<=14){
            gifMalaPuntacion.setVisibility(View.VISIBLE);
        } else if(puntuacion <= 24){
            gifPuntacionNormal.setVisibility(View.VISIBLE);
        }else{
            gifBuenaPuntacion.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
    }

    public void volver(){
        vueltaAtras=new Intent(FinDeJuego.this,MainActivity.class);
        startActivity(vueltaAtras);
    }

    public void mostrarRanking(){
        Intent i=new Intent(FinDeJuego.this,RankingActivity.class);
        i.putExtra("puntuacion",puntuacion);
        //i.putExtra("usuario",nombreUsuario);
        startActivity(i);
    }
}
