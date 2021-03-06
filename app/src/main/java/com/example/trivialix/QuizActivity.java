package com.example.trivialix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
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
    private TextView textEnunciado;
    private TextView textPuntos;
    private TextView textContadorPreguntas;
    private TextView textTemporizador;
    private TextView textUsuario;
    private RadioGroup radioGroup;
    private RadioButton rb1, rb2, rb3, rb4;
    private Button siguiente, volver;
    private List<Preguntas> listaDePreguntas;
    private ColorStateList colors;
    private int preguntasContestadas;
    private int puntuacion;
    private int contadorPreguntas;
    private static final int MAX_PREGUNTAS = 10;
    private Preguntas preguntaActual;
    private String comodin;
    private boolean respondido;
    private Bundle bolsa;
    private Intent recibe, vueltaAtras, resultados;
    private BaseDatos dbGlobal;
    private CountDownTimer temporizador;
    private SoundPool sonidoError;
    private SoundPool sonidoCorrecto;
    private MediaPlayer sonidoTiempo;
    private int correcto;
    private int error;

    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        textUsuario = findViewById(R.id.user_activity_quiz);
        textUsuario.setVisibility(View.INVISIBLE);
        vueltaAtras=new Intent(this, MainActivity.class);
        resultados=new Intent(this, FinDeJuego.class);
        TextView textViewTematica = findViewById(R.id.tematicaElegida);
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
        ColorStateList colorTemporizador = textTemporizador.getTextColors();

        sonidoError= new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sonidoCorrecto= new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sonidoTiempo= MediaPlayer.create(this, R.raw.sawlarga);
        correcto=sonidoCorrecto.load(this,R.raw.correcto2,1);
        error=sonidoError.load(this,R.raw.wrong,1);



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
            int totalPreguntas = listaDePreguntas.size();
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
                        try {
                            comprobar();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                if(sonidoTiempo.isPlaying()){
                    sonidoTiempo.stop();
                }
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

            @Override
            public void onTick(long millisUntilFinished) {
                tiempoEnMilisegundos=millisUntilFinished;

                actualizarTemporizador();

            }

            @Override
            public void onFinish() {
                tiempoEnMilisegundos=0;
                actualizarTemporizador();
                try {
                    comprobar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    @SuppressLint("SetTextI18n")
    private void comprobar() throws IOException {
        respondido=true;
        temporizador.cancel();
        if(sonidoTiempo.isPlaying()){
            sonidoTiempo.stop();
            sonidoTiempo.prepare();
        }
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
        } else{
            comodin = null;
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
        } else{
            fallaste();

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

    private void acertaste() {
        sonidoCorrecto.play(correcto,1,1,1,0,0);
    }

    private void fallaste() {
        sonidoError.play(error,1,1,1,0,0);
    }




    @SuppressLint("SetTextI18n")
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
