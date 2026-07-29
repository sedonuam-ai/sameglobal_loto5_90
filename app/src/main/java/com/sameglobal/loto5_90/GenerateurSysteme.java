package com.sameglobal.loto5_90;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GenerateurSysteme {

    public static final int NUMERO_MIN = 1;
    public static final int NUMERO_MAX = 90;
    public static final int NUMEROS_PAR_GRILLE = 5;

    private List<int[]> grilles;
    private int[] compteurApparitions;
    private Random random;

    public GenerateurSysteme() {
        this.grilles = new ArrayList<>();
        // Index 0 inutilisé, on utilise 1..90
        this.compteurApparitions = new int[NUMERO_MAX + 1];
        this.random = new Random();
    }

    /**
     * Génère "nombreGrilles" grilles de 5 numéros chacune,
     * en équilibrant l'utilisation des numéros de 1 à 90.
     */
    public List<int[]> genererGrilles(int nombreGrilles) {
        grilles.clear();
        for (int i = 1; i <= NUMERO_MAX; i++) {
            compteurApparitions[i] = 0;
        }

        for (int i = 0; i < nombreGrilles; i++) {
            int[] grille = genererUneGrilleEquilibree();
            grilles.add(grille);
        }

        return grilles;
    }

    /**
     * Génère une seule grille de 5 numéros distincts,
     * en privilégiant les numéros les moins utilisés jusqu'ici.
     */
    private int[] genererUneGrilleEquilibree() {
        List<Integer> numerosDisponibles = new ArrayList<>();
        for (int n = 1; n <= NUMERO_MAX; n++) {
            numerosDisponibles.add(n);
        }

        Collections.sort(numerosDisponibles, (a, b) ->
                Integer.compare(compteurApparitions[a], compteurApparitions[b]));

        int taillePoche = Math.min(30, numerosDisponibles.size());
        List<Integer> poche = new ArrayList<>(numerosDisponibles.subList(0, taillePoche));
        Collections.shuffle(poche, random);

        List<Integer> choisis = new ArrayList<>(poche.subList(0, NUMEROS_PAR_GRILLE));
        Collections.sort(choisis);

        int[] grille = new int[NUMEROS_PAR_GRILLE];
        for (int i = 0; i < NUMEROS_PAR_GRILLE; i++) {
            grille[i] = choisis.get(i);
            compteurApparitions[grille[i]]++;
        }

        return grille;
    }

    /**
     * Génère des grilles en piochant uniquement dans un ensemble restreint
     * de numéros (ex: fusion de plusieurs groupes Modulo 9).
     * Le pool doit contenir au moins 5 numéros distincts.
     */
    public List<int[]> genererGrillesAvecPool(List<Integer> pool, int nombreGrilles) {
        grilles.clear();
        for (int i = 1; i <= NUMERO_MAX; i++) {
            compteurApparitions[i] = 0;
        }

        if (pool == null || pool.size() < NUMEROS_PAR_GRILLE) {
            return grilles; // pool insuffisant, retourne une liste vide
        }

        for (int i = 0; i < nombreGrilles; i++) {
            int[] grille = genererUneGrilleEquilibreeAvecPool(pool);
            grilles.add(grille);
        }

        return grilles;
    }

    private int[] genererUneGrilleEquilibreeAvecPool(List<Integer> pool) {
        List<Integer> numerosDisponibles = new ArrayList<>(pool);

        Collections.sort(numerosDisponibles, (a, b) ->
                Integer.compare(compteurApparitions[a], compteurApparitions[b]));

        int taillePoche = Math.min(30, numerosDisponibles.size());
        List<Integer> poche = new ArrayList<>(numerosDisponibles.subList(0, taillePoche));
        Collections.shuffle(poche, random);

        List<Integer> choisis = new ArrayList<>(poche.subList(0, NUMEROS_PAR_GRILLE));
        Collections.sort(choisis);

        int[] grille = new int[NUMEROS_PAR_GRILLE];
        for (int i = 0; i < NUMEROS_PAR_GRILLE; i++) {
            grille[i] = choisis.get(i);
            compteurApparitions[grille[i]]++;
        }

        return grille;
    }

    public List<int[]> getGrilles() {
        return grilles;
    }

    public int[] getCompteurApparitions() {
        return compteurApparitions;
    }

    /**
     * Formate toutes les grilles générées sous forme de texte lisible :
     * Grille 1 :
     * 05 - 17 - 32 - 44 - 78
     */
    public String formaterGrilles() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grilles.size(); i++) {
            sb.append("Grille ").append(i + 1).append(" :\n");
            sb.append(formaterGrille(grilles.get(i)));
            sb.append("\n\n");
        }
        return sb.toString();
    }

    /**
     * Formate une seule grille : "05 - 17 - 32 - 44 - 78"
     */
    public String formaterGrille(int[] grille) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < grille.length; i++) {
            sb.append(String.format("%02d", grille[i]));
            if (i < grille.length - 1) {
                sb.append(" - ");
            }
        }
        return sb.toString();
    }

    /**
     * Retourne un résumé texte du nombre d'apparitions de chaque numéro utilisé.
     */
    public String formaterCompteurApparitions() {
        StringBuilder sb = new StringBuilder();
        for (int n = 1; n <= NUMERO_MAX; n++) {
            if (compteurApparitions[n] > 0) {
                sb.append("Numéro ").append(String.format("%02d", n))
                        .append(" utilisé : ").append(compteurApparitions[n])
                        .append(" fois\n");
            }
        }
        return sb.toString();
    }

    /**
     * Vérifie qu'aucune grille générée ne contient de doublon.
     * Retourne true si tout est correct, false si une anomalie est détectée.
     */
    public boolean verifierAbsenceDoublons() {
        for (int[] grille : grilles) {
            for (int i = 0; i < grille.length; i++) {
                for (int j = i + 1; j < grille.length; j++) {
                    if (grille[i] == grille[j]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
