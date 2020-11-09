package com.example.trivialix;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Pregunta4 extends AppCompatActivity implements View.OnClickListener {
    private Button sig,atras;
    private RadioButton a,b,c,d;
    private int puntuacion=0;
    private Intent i,recibe,vueltaAtras;
    private Bundle bolsa;
    private TextView mostrarPuntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pregunta4);
        i=new Intent(Pregunta4.this,Pregunta5.class);
        vueltaAtras=new Intent(Pregunta4.this,MainActivity.class);
        mostrarPuntuacion=findViewById(R.id.puntuacionFinal);
        a= findViewById(R.id.respuesta1);
        b= findViewById(R.id.respuesta2);
        c= findViewById(R.id.respuesta3);
        d= findViewById(R.id.respuesta4);
        sig= findViewById(R.id.siguientePregunta);
        sig.setVisibility(View.INVISIBLE);
        atras=findViewById(R.id.volver);
        atras.setVisibility(View.INVISIBLE);
        try{

        }catch (Exception e){
            Toast.makeText(this,"Error al recibir datos",Toast.LENGTH_SHORT).show();
        }
        recibe=getIntent();
        bolsa=recibe.getExtras();
        puntuacion=bolsa.getInt("puntuacion");
        if(puntuacion<0){
            puntuacion=0;
        }
        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);
        d.setOnClickListener(this);
        atras.setOnClickListener(this);
        sig.setOnClickListener(this);
        sig.setEnabled(false);
        atras.setEnabled(false);
        mostrarPuntuacion.setText("Su puntuación es: " + puntuacion + " puntos");

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View arg0){
        switch (arg0.getId()){
            case R.id.respuesta1:
                puntuacion= puntuacion-2;
                a.setBackgroundColor(Color.RED);
                d.setBackgroundColor(Color.GREEN);
                a.setEnabled(false);
                b.setEnabled(false);
                c.setEnabled(false);
                d.setEnabled(false);
                sig.setEnabled(true);
                atras.setEnabled(true);
                sig.setVisibility(View.VISIBLE);
                atras.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Respuesta incorrecta -2 puntos",5).show();
                break;
            case R.id.respuesta2:
                puntuacion= puntuacion-2;
                b.setBackgroundColor(Color.RED);
                d.setBackgroundColor(Color.GREEN);
                a.setEnabled(false);
                b.setEnabled(false);
                c.setEnabled(false);
                d.setEnabled(false);
                sig.setEnabled(true);
                atras.setEnabled(true);
                sig.setVisibility(View.VISIBLE);
                atras.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Respuesta incorrecta -2 puntos",5).show();
                break;
            case R.id.respuesta3:
                puntuacion= puntuacion-2;
                c.setBackgroundColor(Color.RED);
                d.setBackgroundColor(Color.GREEN);
                a.setEnabled(false);
                b.setEnabled(false);
                c.setEnabled(false);
                d.setEnabled(false);
                sig.setEnabled(true);
                atras.setEnabled(true);
                sig.setVisibility(View.VISIBLE);
                atras.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Respuesta incorrecta -2 puntos",5).show();
                break;
            case R.id.respuesta4:
                puntuacion= puntuacion+3;
                d.setBackgroundColor(Color.GREEN);
                a.setEnabled(false);
                b.setEnabled(false);
                c.setEnabled(false);
                d.setEnabled(false);
                sig.setEnabled(true);
                sig.setVisibility(View.VISIBLE);
                Toast.makeText(this,"Respuesta correcta +3 puntos",5).show();
                break;
            case R.id.siguientePregunta:
                startActivity(i);
                break;
            case R.id.volver:
                startActivity(vueltaAtras);
                break;
            default:
        }
        i.putExtra("puntuacion",puntuacion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}