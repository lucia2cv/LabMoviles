package com.example.trivialix;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MostrarManual extends AppCompatActivity implements View.OnClickListener {
    private Button aceptar_manual;
    private Bundle bolsa;
    private Intent recibe, i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual);
        i=new Intent(this, MainActivity.class);
        try{
            recibe=getIntent();
            bolsa=recibe.getExtras();
            String usuario = bolsa.getString("nombreUsuario");
            i.putExtra("nombreUsuario", usuario);
        } catch (Exception o){
            System.out.println("No se ha hecho bien el login");
        }
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

        startActivity(i);
    }
}