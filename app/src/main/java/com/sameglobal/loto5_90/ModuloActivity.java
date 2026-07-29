package com.sameglobal.loto5_90;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ModuloActivity extends AppCompatActivity {

    private CheckBox[] checkBoxesGroupes;
    private RadioGroup rgNombreGrillesModulo;
    private Button btnGenererModulo;
    private TextView tvResultatModulo;

    private GenerateurSysteme generateurSysteme;
    private List<ModuloGroupe> groupes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulo);

        checkBoxesGroupes = new CheckBox[]{
                findViewById(R.id.cbG1), findViewById(R.id.cbG2), findViewById(R.id.cbG3),
                findViewById(R.id.cbG4), findViewById(R.id.cbG5), findViewById(R.id.cbG6),
                findViewById(R.id.cbG7), findViewById(R.id.cbG8), findViewById(R.id.cbG9)
        };

        rgNombreGrillesModulo = findViewById(R.id.rgNombreGrillesModulo);
        btnGenererModulo = findViewById(R.id.btnGenererModulo);
        tvResultatModulo = findViewById(R.id.tvResultatModulo);

        generateurSysteme = GrillesRepository.getInstance().getGenerateurSysteme();
        groupes = ModuloGroupe.construireGroupes();

        btnGenererModulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lancerGenerationModulo();
            }
        });
    }

    private void lancerGenerationModulo() {
        List<Integer> pool = new ArrayList<>();
        List<Integer> groupesChoisis = new ArrayList<>();

        for (int i = 0; i < checkBoxesGroupes.length; i++) {
            if (checkBoxesGroupes[i].isChecked()) {
                int numeroGroupe = i + 1;
                groupesChoisis.add(numeroGroupe);
                pool.addAll(groupes.get(i).getNumeros());
            }
        }

        if (pool.size() < GenerateurSysteme.NUMEROS_PAR_GRILLE) {
            Toast.makeText(this,
                    "Sélectionnez au moins un groupe (5 numéros minimum requis)",
                    Toast.LENGTH_LONG).show();
            return;
        }

        int nombreGrilles = getNombreGrillesSelectionne();

        generateurSysteme.genererGrillesAvecPool(pool, nombreGrilles);

        StringBuilder entete = new StringBuilder();
        entete.append("Groupes sélectionnés : ");
        for (int i = 0; i < groupesChoisis.size(); i++) {
            entete.append("G").append(groupesChoisis.get(i));
            if (i < groupesChoisis.size() - 1) entete.append(", ");
        }
        entete.append("\n");
        entete.append("Réserve de numéros disponibles : ").append(pool.size()).append("\n\n");

        // Enregistrement en base de données
        List<String> grillesFormatees = new ArrayList<>();
        for (int[] grille : generateurSysteme.getGrilles()) {
            grillesFormatees.add(generateurSysteme.formaterGrille(grille));
        }
        DatabaseHelper.getInstance(this).insertGrilles(grillesFormatees);

        tvResultatModulo.setText(entete.toString() + generateurSysteme.formaterGrilles());

        Toast.makeText(this,
                nombreGrilles + " grilles générées à partir des groupes sélectionnés",
                Toast.LENGTH_SHORT).show();
    }

    private int getNombreGrillesSelectionne() {
        int selectedId = rgNombreGrillesModulo.getCheckedRadioButtonId();
        if (selectedId == R.id.rbModulo180) {
            return 180;
        } else if (selectedId == R.id.rbModulo360) {
            return 360;
        } else {
            return 90;
        }
    }
}
