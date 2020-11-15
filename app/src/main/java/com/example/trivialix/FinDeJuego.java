package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
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
    private BaseDatos dbGlobal;
    private MediaPlayer sonidoMalo;
    private MediaPlayer sonidoMedio;
    private MediaPlayer sonidoBueno;


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findeljuego);
        dbGlobal = MainActivity.getDbGlobal();
        i=new Intent(FinDeJuego.this,RankingActivity.class);
        vueltaAtras=new Intent(FinDeJuego.this,MainActivity.class);
        atras=findViewById(R.id.volver);
        verRanking= findViewById(R.id.ranking_buttom);
        mostrarPuntuacion=findViewById(R.id.puntuacionFinal);
        gifBuenaPuntacion=findViewById(R.id.buenaPuntuacion);
        gifMalaPuntacion=findViewById(R.id.malaPuntuacion);
        gifPuntacionNormal=findViewById(R.id.puntuacionNormal);
        gifMalaPuntacion.setVisibility(View.INVISIBLE);
        gifBuenaPuntacion.setVisibility(View.INVISIBLE);
        gifPuntacionNormal.setVisibility(View.INVISIBLE);
        sonidoMalo= MediaPlayer.create(this, R.raw.nogodpleaseno);
        sonidoMedio= MediaPlayer.create(this, R.raw.notbad);
        sonidoBueno= MediaPlayer.create(this, R.raw.aplausos3);
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
            recibe=getIntent();
            bolsa=recibe.getExtras();
            assert bolsa != null;
            puntuacion=bolsa.getInt("puntuacion");
            if(puntuacion<=0){
                mostrarPuntuacion.setText("Tu puntuación es: 0 puntos" );
            }else{
                mostrarPuntuacion.setText("Tu puntuación es: " + puntuacion + " puntos");
            }

            if(puntuacion<=14){
                gifMalaPuntacion.setVisibility(View.VISIBLE);
                sonidoMalo.start();

            } else if(puntuacion <= 24){
                gifPuntacionNormal.setVisibility(View.VISIBLE);
                sonidoMedio.start();

            }else{
                gifBuenaPuntacion.setVisibility(View.VISIBLE);
                sonidoBueno.start();

            }
            try{
                String usuario = bolsa.getString("nombreUsuario");
                if (dbGlobal.estaRegistrado(usuario)){
                    dbGlobal.guardarRanking(usuario, puntuacion);
                    System.out.println("Guardado hecho");
                }
                i.putExtra("nombreUsuario", usuario);
                vueltaAtras.putExtra("nombreUsuario", usuario);

            } catch (Exception o){
                System.out.println("No se ha hecho bien el login");
            }
        }catch(Exception e){
            Toast.makeText(this,"Error al recibir datos",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
    }

    public void volver(){
        sonidoMalo.stop();
        sonidoMedio.stop();
        sonidoBueno.stop();
        startActivity(vueltaAtras);
    }

    public void mostrarRanking(){
        sonidoMalo.stop();
        sonidoMedio.stop();
        sonidoBueno.stop();
        i.putExtra("puntuacion",puntuacion);
        startActivity(i);
    }
}
