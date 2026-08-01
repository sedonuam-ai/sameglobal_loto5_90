package com.sameglobal.loto5_90;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoriqueActivity extends AppCompatActivity {

    private Button btnOngletTirages;
    private Button btnOngletGrilles;
    private Button btnEffacerHistorique;
    private TextView tvSousTitre;
    private RecyclerView rvHistorique;
    private DatabaseHelper databaseHelper;

    private String ongletActuel = "tirages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        btnOngletTirages = findViewById(R.id.btnOngletTirages);
        btnOngletGrilles = findViewById(R.id.btnOngletGrilles);
        btnEffacerHistorique = findViewById(R.id.btnEffacerHistorique);
        tvSousTitre = findViewById(R.id.tvSousTitre);
        rvHistorique = findViewById(R.id.rvHistorique);

        databaseHelper = DatabaseHelper.getInstance(this);
        rvHistorique.setLayoutManager(new LinearLayoutManager(this));

        btnOngletTirages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongletActuel = "tirages";
                afficherTirages();
            }
        });

        btnOngletGrilles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ongletActuel = "grilles";
                afficherGrilles();
            }
        });

        btnEffacerHistorique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmerEffacement();
            }
        });

        afficherTirages();
    }

    private void confirmerEffacement() {
        new AlertDialog.Builder(this)
                .setTitle("Effacer les données")
                .setMessage("Voulez-vous vraiment effacer TOUTES les grilles générées et TOUT l'historique (tirages et grilles enregistrés) ? Cette action est irréversible.")
                .setPositiveButton("Effacer", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        effacerToutesLesDonnees();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void effacerToutesLesDonnees() {
        databaseHelper.viderTirages();
        databaseHelper.viderGrilles();

        GrillesRepository.getInstance().getGenerateurSysteme().genererGrilles(0);

        if ("grilles".equals(ongletActuel)) {
            afficherGrilles();
        } else {
            afficherTirages();
        }

        Toast.makeText(this, "Grilles et historique effacés", Toast.LENGTH_SHORT).show();
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
