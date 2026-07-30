package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class VerificationActivity extends AppCompatActivity {

    private EditText[] casesTirage;
    private Button btnVerifierTirage;
    private TextView tvResultatVerification;
    private GenerateurSysteme generateurSysteme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        casesTirage = new EditText[]{
                findViewById(R.id.etCase1), findViewById(R.id.etCase2), findViewById(R.id.etCase3),
                findViewById(R.id.etCase4), findViewById(R.id.etCase5)
        };
        btnVerifierTirage = findViewById(R.id.btnVerifierTirage);
        tvResultatVerification = findViewById(R.id.tvResultatVerification);

        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        configurerAutoAvance();

        btnVerifierTirage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerVerification();
            }
        });
    }

    /**
     * Configure chaque case pour :
     * - passer automatiquement à la case suivante dès que 2 chiffres sont saisis,
     *   ou dès qu'un seul chiffre rend impossible un second chiffre valide (ex: "9"
     *   ne peut pas être suivi d'un chiffre car "9x" dépasserait 90 sauf "90" lui-même).
     * - revenir à la case précédente avec la touche Retour arrière si la case est vide.
     */
    private void configurerAutoAvance() {
        for (int i = 0; i < casesTirage.length; i++) {
            final int index = i;
            final EditText caseActuelle = casesTirage[i];

            caseActuelle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    String valeur = s.toString();
                    if (valeur.isEmpty()) {
                        return;
                    }

                    boolean deuxChiffres = valeur.length() == 2;
                    boolean unSeulChiffreMaisNePeutPasContinuer = false;

                    if (valeur.length() == 1) {
                        int chiffre = Integer.parseInt(valeur);
                        // Si "chiffre" suivi d'un 2e chiffre dépasserait toujours 90
                        // (sauf le cas exact "9" -> "90"), on avance directement.
                        if (chiffre >= 1 && chiffre * 10 > GenerateurSysteme.NUMERO_MAX && chiffre != 9) {
                            unSeulChiffreMaisNePeutPasContinuer = true;
                        }
                    }

                    if ((deuxChiffres || unSeulChiffreMaisNePeutPasContinuer) && index < casesTirage.length - 1) {
                        casesTirage[index + 1].requestFocus();
                    }
                }
            });

            caseActuelle.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && keyCode == KeyEvent.KEYCODE_DEL
                            && caseActuelle.getText().toString().isEmpty()
                            && index > 0) {
                        casesTirage[index - 1].requestFocus();
                        casesTirage[index - 1].setText("");
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void lancerVerification() {
        if (generateurSysteme == null || generateurSysteme.getGrilles().isEmpty()) {
            Toast.makeText(this, "Générez d'abord des grilles", Toast.LENGTH_SHORT).show();
            return;
        }

        int[] tirage = lireTirage();
        if (tirage == null) {
            Toast.makeText(this,
                    "Remplissez les 5 cases avec des numéros distincts entre 1 et 90.",
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
     * Lit les 5 cases de saisie et retourne un tableau de 5 numéros valides,
     * ou null si une case est vide, hors plage 1-90, ou en cas de doublon.
     */
    private int[] lireTirage() {
        int[] resultat = new int[5];

        for (int i = 0; i < 5; i++) {
            String texte = casesTirage[i].getText().toString().trim();
            if (texte.isEmpty()) {
                return null;
            }
            try {
                int n = Integer.parseInt(texte);
                if (n < GenerateurSysteme.NUMERO_MIN || n > GenerateurSysteme.NUMERO_MAX) {
                    return null;
                }
                resultat[i] = n;
            } catch (NumberFormatException e) {
                return null;
            }
        }

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
