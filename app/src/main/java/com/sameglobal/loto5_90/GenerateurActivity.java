package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class GenerateurActivity extends AppCompatActivity {

    private RadioGroup rgNombreGrilles;
    private Button btnLancerGeneration;
    private Button btnVoirRepartition;
    private TextView tvResultat;
    private GenerateurSysteme generateurSysteme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateur);

        rgNombreGrilles = findViewById(R.id.rgNombreGrilles);
        btnLancerGeneration = findViewById(R.id.btnLancerGeneration);
        btnVoirRepartition = findViewById(R.id.btnVoirRepartition);
        tvResultat = findViewById(R.id.tvResultat);
        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();

        btnLancerGeneration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nombreGrilles = getNombreGrillesSelectionne();

                generateurSysteme.genererGrilles(nombreGrilles);

                boolean ok = generateurSysteme.verifierAbsenceDoublons();
                if (!ok) {
                    Toast.makeText(GenerateurActivity.this,
                            "ANOMALIE : doublon détecté dans une grille !",
                            Toast.LENGTH_LONG).show();
                }

                // Enregistrement des grilles générées dans la base de données
                List<String> grillesFormatees = new ArrayList<>();
                for (int[] grille : generateurSysteme.getGrilles()) {
                    grillesFormatees.add(generateurSysteme.formaterGrille(grille));
                }
                DatabaseHelper.getInstance(GenerateurActivity.this)
                        .insertGrilles(grillesFormatees);

                String resultat = generateurSysteme.formaterGrilles();
                tvResultat.setText(resultat);

                Toast.makeText(GenerateurActivity.this,
                        nombreGrilles + " grilles générées et enregistrées",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnVoirRepartition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (generateurSysteme.getGrilles().isEmpty()) {
                    Toast.makeText(GenerateurActivity.this,
                            "Générez d'abord des grilles",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String repartition = generateurSysteme.formaterCompteurApparitions();
                tvResultat.setText(repartition);
            }
        });
    }

    private int getNombreGrillesSelectionne() {
        int selectedId = rgNombreGrilles.getCheckedRadioButtonId();
        if (selectedId == R.id.rb180) {
            return 180;
        } else if (selectedId == R.id.rb360) {
            return 360;
        } else {
            return 90;
        }
    }
}
