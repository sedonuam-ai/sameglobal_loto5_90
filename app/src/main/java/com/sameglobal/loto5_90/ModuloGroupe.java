package com.sameglobal.loto5_90;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un groupe de numéros associés au système Modulo 9.
 * G1 à G9 : chaque groupe contient les numéros de 1 à 90
 * dont (numero % 9) correspond au groupe.
 * Convention : un reste de 0 est associé au groupe G9.
 */
public class ModuloGroupe {

    private int numeroGroupe; // 1 à 9
    private List<Integer> numeros;

    public ModuloGroupe(int numeroGroupe) {
        this.numeroGroupe = numeroGroupe;
        this.numeros = new ArrayList<>();
    }

    public int getNumeroGroupe() {
        return numeroGroupe;
    }

    public List<Integer> getNumeros() {
        return numeros;
    }

    public void ajouterNumero(int n) {
        numeros.add(n);
    }

    /**
     * Construit les 9 groupes (G1 à G9) à partir des numéros 1 à 90,
     * répartis selon leur reste modulo 9 (reste 0 -> G9).
     */
    public static List<ModuloGroupe> construireGroupes() {
        List<ModuloGroupe> groupes = new ArrayList<>();
        for (int g = 1; g <= 9; g++) {
            groupes.add(new ModuloGroupe(g));
        }

        for (int n = GenerateurSysteme.NUMERO_MIN; n <= GenerateurSysteme.NUMERO_MAX; n++) {
            int reste = n % 9;
            int indexGroupe = (reste == 0) ? 9 : reste; // reste 0 -> G9
            groupes.get(indexGroupe - 1).ajouterNumero(n);
        }

        return groupes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("G").append(numeroGroupe).append(" : ");
        for (int i = 0; i < numeros.size(); i++) {
            sb.append(String.format("%02d", numeros.get(i)));
            if (i < numeros.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
