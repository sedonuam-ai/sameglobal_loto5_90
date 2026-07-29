package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AfficherGrillesActivity extends AppCompatActivity {

    private RecyclerView rvGrilles;
    private TextView tvTitreListe;
    private GenerateurSysteme generateurSysteme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_grilles);

        rvGrilles = findViewById(R.id.rvGrilles);
        tvTitreListe = findViewById(R.id.tvTitreListe);

        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        List<int[]> grilles = generateurSysteme.getGrilles();

        if (grilles.isEmpty()) {
            Toast.makeText(this, "Aucune grille générée. Générez d'abord un système.",
                    Toast.LENGTH_LONG).show();
            tvTitreListe.setText("Aucune grille disponible");
            return;
        }

        tvTitreListe.setText("Grilles générées (" + grilles.size() + ")");

        rvGrilles.setLayoutManager(new LinearLayoutManager(this));
        rvGrilles.setAdapter(new GrillesAdapter(grilles, generateurSysteme));
    }
}
