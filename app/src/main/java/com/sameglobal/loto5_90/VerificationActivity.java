package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class VerificationActivity extends AppCompatActivity {

    private EditText etTirage;
    private Button btnVerifierTirage;
    private TextView tvResultatVerification;
    private GenerateurSysteme generateurSysteme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        etTirage = findViewById(R.id.etTirage);
        btnVerifierTirage = findViewById(R.id.btnVerifierTirage);
        tvResultatVerification = findViewById(R.id.tvResultatVerification);

        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        btnVerifierTirage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerVerification();
            }
        });
    }

    private void lancerVerification() {
        if (generateurSysteme == null || generateurSysteme.getGrilles().isEmpty()) {
            Toast.makeText(this, "Générez d'abord des grilles", Toast.LENGTH_SHORT).show();
            return;
        }

        int[] tirage = parseTirage(etTirage.getText().toString());
        if (tirage == null) {
            Toast.makeText(this,
                    "Entrée invalide. Entrez 5 numéros distincts entre 1 et 90.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Enregistrement du tirage dans l'historique
        DatabaseHelper.getInstance(this).insertTirage(
                generateurSysteme.formaterGrille(tirage));

        List<int[]> grilles = generateurSysteme.getGrilles();
        StringBuilder sb = new StringBuilder();

        int nombreGagnants = 0; // grilles avec 3+ bons numéros
        int totalPaires = 0;    // grilles avec exactement 2 bons numéros
        int totalTriplets = 0;  // grilles avec exactement 3 bons numéros

        for (int i = 0; i < grilles.size(); i++) {
            int[] grille = grilles.get(i);
            int bons = compterBonsNumeros(grille, tirage);

            if (bons >= 2) {
                sb.append("Grille ").append(i + 1).append(" :\n");
                sb.append(bons).append(" bons numéros\n\n");
            }

            if (bons >= 3) {
                nombreGagnants++;
            }
            if (bons == 2) {
                totalPaires++;
            }
            if (bons == 3) {
                totalTriplets++;
            }
        }

        sb.append("========================\n");
        sb.append("RÉSUMÉ\n");
        sb.append("========================\n");
        sb.append("Grilles gagnantes (3+ bons) : ").append(nombreGagnants).append("\n");
        sb.append("Grilles avec paires (2 bons) : ").append(totalPaires).append("\n");
        sb.append("Grilles avec triplets (3 bons) : ").append(totalTriplets).append("\n");

        tvResultatVerification.setText(sb.toString());
    }

    private int compterBonsNumeros(int[] grille, int[] tirage) {
        int count = 0;
        for (int numGrille : grille) {
            for (int numTirage : tirage) {
                if (numGrille == numTirage) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    /**
     * Parse une chaîne du type "12 25 47 63 89" en tableau de 5 entiers valides.
     * Retourne null si le format est incorrect, s'il y a un doublon,
     * ou si un numéro est hors de la plage 1-90.
     */
    private int[] parseTirage(String texte) {
        String[] parties = texte.trim().split("\\s+");
        if (parties.length != 5) {
            return null;
        }

        int[] resultat = new int[5];
        for (int i = 0; i < 5; i++) {
            try {
                int n = Integer.parseInt(parties[i]);
                if (n < GenerateurSysteme.NUMERO_MIN || n > GenerateurSysteme.NUMERO_MAX) {
                    return null;
                }
                resultat[i] = n;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        // Vérifier l'absence de doublon
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                if (resultat[i] == resultat[j]) {
                    return null;
                }
            }
        }

        return resultat;
    }
}
