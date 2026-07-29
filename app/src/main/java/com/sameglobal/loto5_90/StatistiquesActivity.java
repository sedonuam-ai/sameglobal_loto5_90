package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class StatistiquesActivity extends AppCompatActivity {

    private TextView tvStatistiques;
    private GenerateurSysteme generateurSysteme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);

        tvStatistiques = findViewById(R.id.tvStatistiques);
        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        afficherStatistiques();
    }

    private void afficherStatistiques() {
        if (generateurSysteme == null || generateurSysteme.getGrilles().isEmpty()) {
            tvStatistiques.setText("Aucune grille générée. Générez d'abord un système.");
            return;
        }

        List<int[]> grilles = generateurSysteme.getGrilles();
        int[] compteur = generateurSysteme.getCompteurApparitions();

        StringBuilder sb = new StringBuilder();

        // Numéro le plus / moins sorti
        int max = -1, min = Integer.MAX_VALUE;
        int numeroMax = -1, numeroMin = -1;
        for (int n = 1; n <= GenerateurSysteme.NUMERO_MAX; n++) {
            if (compteur[n] > max) {
                max = compteur[n];
                numeroMax = n;
            }
            if (compteur[n] < min) {
                min = compteur[n];
                numeroMin = n;
            }
        }

        sb.append("Nombre de grilles : ").append(grilles.size()).append("\n\n");
        sb.append("Numéro le plus utilisé : ")
                .append(String.format("%02d", numeroMax))
                .append(" (").append(max).append(" fois)\n");
        sb.append("Numéro le moins utilisé : ")
                .append(String.format("%02d", numeroMin))
                .append(" (").append(min).append(" fois)\n\n");

        // Pair / impair et somme moyenne
        int totalPairs = 0, totalImpairs = 0;
        long sommeTotale = 0;

        for (int[] grille : grilles) {
            int sommeGrille = 0;
            for (int n : grille) {
                if (n % 2 == 0) {
                    totalPairs++;
                } else {
                    totalImpairs++;
                }
                sommeGrille += n;
            }
            sommeTotale += sommeGrille;
        }

        double sommeMoyenne = (double) sommeTotale / grilles.size();

        sb.append("Total numéros pairs : ").append(totalPairs).append("\n");
        sb.append("Total numéros impairs : ").append(totalImpairs).append("\n");
        sb.append(String.format("Somme moyenne par grille : %.1f\n\n", sommeMoyenne));

        sb.append("========================\n");
        sb.append("FRÉQUENCE PAR NUMÉRO\n");
        sb.append("========================\n");
        sb.append(generateurSysteme.formaterCompteurApparitions());

        tvStatistiques.setText(sb.toString());
    }
}
