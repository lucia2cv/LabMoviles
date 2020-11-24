package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;


public class RankingActivity extends AppCompatActivity implements View.OnClickListener {
    private Button volver;
    private TableLayout tabla;
    private TextView mostrarPuntuacion, mostrarUsuario, nombre1, nombre2, nombre3, ranking1, ranking2, ranking3, error;
    private int puntuacion;
    private BaseDatos dbGlobal;
    private Bundle bolsa;
    private Intent recibe, i;
    private  MediaPlayer sonidoFin;


    @SuppressLint("SetTextI18n")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);
        i=new Intent(this,MainActivity.class);
        sonidoFin= MediaPlayer.create(this, R.raw.mariobros);
        sonidoFin.start();
        dbGlobal = MainActivity.getDbGlobal();
        volver=findViewById(R.id.volver_ranking);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonidoFin.stop();
                startActivity(i);
            }
        });
        mostrarPuntuacion=findViewById(R.id.puntuacion_usuario);
        mostrarUsuario = findViewById(R.id.user_ranking);
        mostrarUsuario.setVisibility(View.INVISIBLE);
        nombre1=findViewById(R.id.nombre1);
        nombre2=findViewById(R.id.nombre2);
        nombre3=findViewById(R.id.nombre3);
        ranking1=findViewById(R.id.ranking1);
        ranking2=findViewById(R.id.ranking2);
        ranking3=findViewById(R.id.ranking3);
        error = findViewById(R.id.errorRanking);
        error.setVisibility(View.INVISIBLE);


        List<Usuarios> usuarios = dbGlobal.getAllUsuarios();
        if (usuarios.size() >= 3) {
            Usuarios user1 = dbGlobal.top1player();
            Usuarios user2 = dbGlobal.top2player();
            Usuarios user3 = dbGlobal.top3player();
            nombre1.setText(user1.getNombre());
            nombre2.setText(user2.getNombre());
            nombre3.setText(user3.getNombre());
            ranking1.setText(user1.getRecord() + " puntos");
            ranking2.setText(user2.getRecord() + " puntos");
            ranking3.setText(user3.getRecord() + " puntos");
        } else{
            tabla = findViewById(R.id.tableLayout);
            tabla.setVisibility(View.INVISIBLE);
            error.setText("No hay datos suficientes");
            error.setVisibility(View.VISIBLE);

        }


        recibe=getIntent();
        bolsa=recibe.getExtras();
        assert bolsa != null;
        puntuacion=bolsa.getInt("puntuacion");
        if(puntuacion<=0){
            mostrarPuntuacion.setText("Tu puntuación es: 0 puntos" );
        }else{
            mostrarPuntuacion.setText("Tu puntuación es: " + puntuacion + " puntos");
        }
        mostrarPuntuacion.setText("Tu puntuación es: " + puntuacion + " puntos");


        try{
            String usuario = bolsa.getString("nombreUsuario");
            if (dbGlobal.estaRegistrado(usuario)){
                mostrarUsuario.setVisibility(View.VISIBLE);
                mostrarUsuario.setText(usuario + ", estos son los resultados");
            }
            i.putExtra("nombreUsuario", usuario);

        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }


    }
        @Override
        public void onClick (View v){
        }
    }
