package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private static final long CONTADORMILISEGUNDOS  =30000;
    private static final String LISTAPREGUNTAS = "keyQuestionList";
    private static final String PREGUNTASCONTADOR = "keyQuestionCount";
    private static final String SCORE = "score";
    private static final String TIEMPO = "tiempo";
    private static final String RESPONDER = "responder";
    private long tiempoEnMilisegundos;
    private TextView textEnunciado, textPuntos, textContadorPreguntas, textViewTematica, textTemporizador, textUsuario;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button siguiente, volver;
    private List<Preguntas> listaDePreguntas;
    private ColorStateList colors;
    private ColorStateList colorTemporizador;
    private int preguntasContestadas, puntuacion,totalPreguntas,contadorPreguntas;
    private static final int MAX_PREGUNTAS = 10;
    private Preguntas preguntaActual;
    private String comodin, nombreUsuario;
    private boolean respondido;
    private Bundle bolsa;
    private Intent recibe, vueltaAtras, resultados;
    private BaseDatos dbGlobal;
    private CountDownTimer temporizador;
    private MediaPlayer sonidoError;
    private MediaPlayer sonidoCorrecto;
    private MediaPlayer sonidoTiempo;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textUsuario = findViewById(R.id.user_activity_quiz);
        textUsuario.setVisibility(View.INVISIBLE);
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
        textTemporizador=findViewById(R.id.tiempo);
        colors=rb1.getTextColors();
        recibe=getIntent();
        bolsa=recibe.getExtras();
        dbGlobal = MainActivity.getDbGlobal();
        colorTemporizador=textTemporizador.getTextColors();

        sonidoError= MediaPlayer.create(this, R.raw.wrong);
        sonidoCorrecto= MediaPlayer.create(this, R.raw.correcto2);
        sonidoTiempo= MediaPlayer.create(this, R.raw.alarmamongus);

        int tematicaid = recibe.getIntExtra(MainActivity.ID_TEMATICA,1);
        String tematicaName= recibe.getStringExtra(MainActivity.TEMATICA);

        textViewTematica.setText("Temática: " + tematicaName);

        try{
            String usuario = bolsa.getString("nombreUsuario");
            if (dbGlobal.estaRegistrado(usuario)){
                textUsuario.setVisibility(View.VISIBLE);
                textUsuario.setText("¡Adelante, " + usuario + " a por todas!");
            }
            resultados.putExtra("nombreUsuario", usuario);
            vueltaAtras.putExtra("nombreUsuario", usuario);

        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }


        if(savedInstanceState==null){
            if (dbGlobal != null){
                listaDePreguntas=dbGlobal.getAllPreguntas(tematicaid);
                Collections.shuffle(listaDePreguntas);
                siguiente();

            }
        }else{
            listaDePreguntas=savedInstanceState.getParcelableArrayList(LISTAPREGUNTAS);
            totalPreguntas=listaDePreguntas.size();
            contadorPreguntas=savedInstanceState.getInt(PREGUNTASCONTADOR);
            preguntaActual=listaDePreguntas.get(contadorPreguntas-1);
            puntuacion=savedInstanceState.getInt(SCORE);
            tiempoEnMilisegundos=savedInstanceState.getLong(TIEMPO);
            respondido=savedInstanceState.getBoolean(RESPONDER);
            if(!respondido){
                tiempoMaximo();
            }else{
                actualizarTemporizador();
                mostrarOpcionCorrecta();
            }
        }

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!respondido){
                    if(rb1.isChecked()||rb2.isChecked()||rb3.isChecked()||rb4.isChecked()){
                        comprobar();
                        rb1.setEnabled(false);
                        rb2.setEnabled(false);
                        rb3.setEnabled(false);
                        rb4.setEnabled(false);
                    }else{
                        Toast.makeText(QuizActivity.this,"Responde",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    siguiente();
                    rb1.setEnabled(true);
                    rb2.setEnabled(true);
                    rb3.setEnabled(true);
                    rb4.setEnabled(true);
                }
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(vueltaAtras);
                }

        });


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
            tiempoEnMilisegundos=CONTADORMILISEGUNDOS;

            tiempoMaximo();
        }else{
            acabarTest();

        }
    }

    private void tiempoMaximo() {
        temporizador=new CountDownTimer(tiempoEnMilisegundos,1000) {
            int i;
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoEnMilisegundos=millisUntilFinished;

                actualizarTemporizador();
                i++;
                System.out.println(i);
            }

            @Override
            public void onFinish() {
                tiempoEnMilisegundos=0;
                actualizarTemporizador();
                comprobar();

            }

        }.start();
    }

    private void actualizarTemporizador() {
        int minutos=(int)(tiempoEnMilisegundos/1000)/60;
        int segundos=(int)(tiempoEnMilisegundos/1000)%60;
        String formatoTiempo=String.format(Locale.getDefault(),"%02d:%02d", minutos, segundos);
        textTemporizador.setText(formatoTiempo);

        if(tiempoEnMilisegundos<11000){
            textTemporizador.setTextColor(Color.RED);
            sonidoTiempo.start();

        } else {
            textTemporizador.setTextColor(colors);
        }


    }

    private void comprobar(){
        respondido=true;
        temporizador.cancel();
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
        if(comodin!=null){
            if(comodin.equals(preguntaActual.getOpcionCorrecta())){
                puntuacion = puntuacion + 3;
                acertaste();
            } else if(!comodin.equals(preguntaActual.getOpcionCorrecta())){
                    fallaste();

                if (puntuacion > 2){
                    puntuacion = puntuacion - 2;
                }
                else {
                    puntuacion = 0;
                }

            }
        }
        textPuntos.setText("Puntos: "+ puntuacion);
        mostrarOpcionCorrecta();
    }

    private void acertaste() {
        sonidoCorrecto.start();
    }

    private void fallaste() {
        sonidoError.start();
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
            siguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mostrarResultados();
                }
            });
        }
    }

    private void acabarTest() {
        finish();
    }
    private void mostrarResultados(){
        startActivity(resultados);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(temporizador!=null){
            temporizador.cancel();
        }
    }
}
