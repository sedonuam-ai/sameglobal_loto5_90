package com.sameglobal.loto5_90;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.List;

public class VerificationActivity extends AppCompatActivity {

    private EditText[] casesTirage;
    private Button btnVerifierTirage;
    private LinearLayout llResultats;
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
        llResultats = findViewById(R.id.llResultats);

        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        configurerAutoAvance();

        btnVerifierTirage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerVerification();
            }
        });
    }

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

        DatabaseHelper.getInstance(this).insertTirage(
                generateurSysteme.formaterGrille(tirage));

        llResultats.removeAllViews();

        List<int[]> grilles = generateurSysteme.getGrilles();

        int nombreGrillesGagnantes = 0;
        int totalPairesGagnees = 0;

        for (int i = 0; i < grilles.size(); i++) {
            int[] grille = grilles.get(i);
            int bons = compterBonsNumeros(grille, tirage);
            int gain = calculerGain(bons);

            if (gain > 0) {
                nombreGrillesGagnantes++;
                totalPairesGagnees += gain;
                llResultats.addView(creerVueGrilleResultat(i + 1, grille, bons, gain));
            }
        }

        llResultats.addView(creerVueResume(nombreGrillesGagnantes, totalPairesGagnees));
    }

    /**
     * Table des gains :
     * 5 bons numéros = 10 paires
     * 4 bons numéros = 6 paires
     * 3 bons numéros = 3 paires
     * 2 bons numéros = 1 paire
     * 1 bon numéro   = 0 paire
     */
    private int calculerGain(int bons) {
        switch (bons) {
            case 5: return 10;
            case 4: return 6;
            case 3: return 3;
            case 2: return 1;
            default: return 0;
        }
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
     * Crée une carte avec un encadré doré pour une grille gagnante.
     */
    private View creerVueGrilleResultat(int numeroGrille, int[] grille, int bons, int gain) {
        LinearLayout conteneur = new LinearLayout(this);
        conteneur.setOrientation(LinearLayout.VERTICAL);
        conteneur.setPadding(dp(12), dp(10), dp(12), dp(10));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(10));
        conteneur.setLayoutParams(params);

        GradientDrawable fond = new GradientDrawable();
        fond.setColor(ContextCompat.getColor(this, R.color.surfaceDarker));
        fond.setStroke(dp(2), ContextCompat.getColor(this, R.color.colorGold));
        fond.setCornerRadius(dp(8));
        conteneur.setBackground(fond);

        TextView tvTitre = new TextView(this);
        tvTitre.setText("🏆 Grille " + numeroGrille + " :");
        tvTitre.setTextColor(ContextCompat.getColor(this, R.color.colorGold));
        tvTitre.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvTitre.setTypeface(tvTitre.getTypeface(), android.graphics.Typeface.BOLD);

        TextView tvNumeros = new TextView(this);
        tvNumeros.setText(generateurSysteme.formaterGrille(grille));
        tvNumeros.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
        tvNumeros.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvNumeros.setTypeface(android.graphics.Typeface.MONOSPACE);
        tvNumeros.setPadding(0, dp(2), 0, dp(4));

        TextView tvGain = new TextView(this);
        tvGain.setText(bons + " bons numéros — Gain : " + gain + (gain > 1 ? " paires" : " paire"));
        tvGain.setTextColor(ContextCompat.getColor(this, R.color.textSecondary));
        tvGain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

        conteneur.addView(tvTitre);
        conteneur.addView(tvNumeros);
        conteneur.addView(tvGain);

        return conteneur;
    }

    private View creerVueResume(int nombreGrillesGagnantes, int totalPairesGagnees) {
        TextView tvResume = new TextView(this);
        tvResume.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
        tvResume.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvResume.setPadding(dp(4), dp(12), dp(4), dp(4));
        tvResume.setGravity(Gravity.START);

        String texte = "========================\n"
                + "RÉSUMÉ\n"
                + "========================\n"
                + "Grilles gagnantes : " + nombreGrillesGagnantes + "\n"
                + "Total des gains : " + totalPairesGagnees + " paires";

        if (nombreGrillesGagnantes == 0) {
            texte = "Aucune grille gagnante pour ce tirage.";
        }

        tvResume.setText(texte);
        return tvResume;
    }

    private int dp(int valeur) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, valeur, getResources().getDisplayMetrics());
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
