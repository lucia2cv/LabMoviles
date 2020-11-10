package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textEnunciado, textPuntos, textContadorPreguntas, textViewTematica, textTemporizador;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button siguiente, volver;
    private List<Preguntas> listaDePreguntas;
    private ColorStateList colors;
    private int preguntasContestadas, puntuacion;
    private static final int MAX_PREGUNTAS = 10;
    private Preguntas preguntaActual;
    private String comodin;
    private boolean respondido;
    private Bundle bolsa;
    private Intent recibe, vueltaAtras, resultados;
    private BaseDatos dbGlobal;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        vueltaAtras=new Intent(this, MainActivity.class);
        resultados=new Intent(this, FinDeJuego.class);
        textViewTematica=findViewById(R.id.tematicaElegida);
        textEnunciado=findViewById(R.id.pregunta);
        textPuntos=findViewById(R.id.puntuacion);
        textContadorPreguntas=findViewById(R.id.numeroPregunta);
        textTemporizador=findViewById(R.id.tiempo);
        radioGroup=findViewById(R.id.radioGroup);
        rb1=findViewById(R.id.opciona);
        rb2=findViewById(R.id.opcionb);
        rb3=findViewById(R.id.opcionc);
        rb4=findViewById(R.id.opciond);
        siguiente=findViewById(R.id.siguiente);
        volver = findViewById(R.id.reintentar);
        colors=rb1.getTextColors();
        recibe=getIntent();
        bolsa=recibe.getExtras();
        dbGlobal = MainActivity.getDbGlobal();

        int tematicaid = recibe.getIntExtra(MainActivity.ID_TEMATICA,1);
        String tematicaName= recibe.getStringExtra(MainActivity.TEMATICA);

        textViewTematica.setText("Temática: " + tematicaName);

        if(savedInstanceState==null){
            if (dbGlobal != null){
                listaDePreguntas=dbGlobal.getAllPreguntas(tematicaid);
                Collections.shuffle(listaDePreguntas);
                siguiente();

            }

        }

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!respondido){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        comprobar();
                    }else{
                        Toast.makeText(QuizActivity.this,"Responde",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    siguiente();
                }
            }
        });

        //volver.setOnClickListener(startActivity(vueltaAtras));
    }

    @SuppressLint("SetTextI18n")
    private void siguiente() {
        rb1.setTextColor(colors);
        rb2.setTextColor(colors);
        rb3.setTextColor(colors);
        rb4.setTextColor(colors);
        radioGroup.clearCheck();

        if(preguntasContestadas<MAX_PREGUNTAS){
            preguntaActual=listaDePreguntas.get(preguntasContestadas);
            textEnunciado.setText(preguntaActual.getEnunciado());
            rb1.setText(preguntaActual.getOpcionA());
            rb2.setText(preguntaActual.getOpcionB());
            rb3.setText(preguntaActual.getOpcionC());
            rb4.setText(preguntaActual.getOpcionD());

            preguntasContestadas++;
            textContadorPreguntas.setText("Preguntas: "+preguntasContestadas+"/"+MAX_PREGUNTAS);
            respondido=false;
            siguiente.setText("Comprobar");
            volver.setText("Abandonar");
        }else{
            acabarTest();
        }
    }

    private void comprobar(){
        respondido=true;
        RadioButton seleccionado=findViewById(radioGroup.getCheckedRadioButtonId());
        int respuesta=radioGroup.indexOfChild(seleccionado)+1;
        if(respuesta==1){
            comodin="a";
        }else if(respuesta==2){
            comodin="b";
        }else if(respuesta==3){
            comodin="c";
        }else if(respuesta==4){
            comodin="d";
        }
        if(comodin.equals(preguntaActual.getOpcionCorrecta())){
            puntuacion = puntuacion + 3;
        } else{
            if (puntuacion > 2){
                puntuacion = puntuacion - 2;
            }
            else {
                puntuacion = 0;
            }

        }
        textPuntos.setText("Puntos: "+ puntuacion);
        mostrarOpcionCorrecta();
    }

    private void mostrarOpcionCorrecta(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (preguntaActual.getOpcionCorrecta()){
            case "a":
                rb1.setTextColor(Color.GREEN);
                break;
            case "b":
                rb2.setTextColor(Color.GREEN);
                break;
            case "c":
                rb3.setTextColor(Color.GREEN);
                break;
            case "d":
                rb4.setTextColor(Color.GREEN);
                break;
        }
        if(preguntasContestadas<MAX_PREGUNTAS){
            siguiente.setText("Siguiente");
        }else{
            siguiente.setText("Resultados");
            resultados.putExtra("puntuacion",puntuacion);
            startActivity(resultados);
        }
    }

    private void acabarTest() {


    }
}
