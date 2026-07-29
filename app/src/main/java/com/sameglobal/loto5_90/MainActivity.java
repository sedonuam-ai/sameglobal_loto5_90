package com.sameglobal.loto5_90;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGenerer;
    private Button btnAfficherGrilles;
    private Button btnVerifier;
    private Button btnStatistiques;
    private Button btnHistorique;
    private Button btnModulo9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGenerer = findViewById(R.id.btnGenerer);
        btnAfficherGrilles = findViewById(R.id.btnAfficherGrilles);
        btnVerifier = findViewById(R.id.btnVerifier);
        btnStatistiques = findViewById(R.id.btnStatistiques);
        btnHistorique = findViewById(R.id.btnHistorique);
        btnModulo9 = findViewById(R.id.btnModulo9);

        btnGenerer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GenerateurActivity.class));
            }
        });

        btnAfficherGrilles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AfficherGrillesActivity.class));
            }
        });

        btnVerifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VerificationActivity.class));
            }
        });

        btnStatistiques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatistiquesActivity.class));
            }
        });

        btnHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoriqueActivity.class));
            }
        });

        btnModulo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ModuloActivity.class));
            }
        });
    }
}
