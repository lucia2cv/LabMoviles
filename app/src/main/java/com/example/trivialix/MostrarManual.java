package com.example.trivialix;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MostrarManual extends AppCompatActivity implements View.OnClickListener {
    private Button aceptar_manual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);
        aceptar_manual = findViewById(R.id.aceptar_manual);
        aceptar_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
    public void volver(){
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}