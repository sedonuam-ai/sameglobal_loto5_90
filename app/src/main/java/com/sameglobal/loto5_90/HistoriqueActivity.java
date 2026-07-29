package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    private Button btnOngletTirages;
    private Button btnOngletGrilles;
    private TextView tvSousTitre;
    private RecyclerView rvHistorique;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        btnOngletTirages = findViewById(R.id.btnOngletTirages);
        btnOngletGrilles = findViewById(R.id.btnOngletGrilles);
        tvSousTitre = findViewById(R.id.tvSousTitre);
        rvHistorique = findViewById(R.id.rvHistorique);

        databaseHelper = DatabaseHelper.getInstance(this);
        rvHistorique.setLayoutManager(new LinearLayoutManager(this));

        btnOngletTirages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherTirages();
            }
        });

        btnOngletGrilles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherGrilles();
            }
        });

        // Affichage par défaut : tirages
        afficherTirages();
    }

    private void afficherTirages() {
        List<String> tirages = databaseHelper.getAllTirages();
        tvSousTitre.setText("Tirages enregistrés (" + tirages.size() + ")");
        rvHistorique.setAdapter(new StringListAdapter(tirages));
    }

    private void afficherGrilles() {
        List<String> grilles = databaseHelper.getAllGrilles();
        tvSousTitre.setText("Grilles enregistrées (" + grilles.size() + ")");
        rvHistorique.setAdapter(new StringListAdapter(grilles));
    }
}
