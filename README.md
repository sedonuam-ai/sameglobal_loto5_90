# SAMEGLOBAL_LOTO5_90

Application Android (Java) pour le Loto 5/90 avec système réducteur de génération de grilles.

**Package :** `com.sameglobal.loto5_90`
**Langage :** Java uniquement
**Base de données :** SQLite (native, sans Room)

## Fonctionnalités

1. **Génération de grilles** (90 / 180 / 360 grilles de 5 numéros, équilibrées sur 1-90)
2. **Affichage des grilles** générées (liste défilante)
3. **Vérification d'un tirage** (5 cases de saisie avec avancement automatique du curseur,
   comptage des bons numéros, gagnants, paires, triplets)
4. **Statistiques** (fréquence, pair/impair, sommes, numéros extrêmes)
5. **Historique persistant** (tirages et grilles enregistrés en base SQLite)
6. **Génération ciblée par groupes Modulo 9** (combinaison de plusieurs groupes G1 à G9)

## Design

- **Thème sombre** appliqué à tous les écrans (forcé, indépendant du réglage clair/sombre du téléphone).
- **Logo / icône de l'application** : un dé stylisé (motif à 5 points, en référence au Loto 5/90)
  sur fond bleu en losange. Fournie en icône adaptative (Android 8+) et en PNG classique
  pour les versions antérieures — visible sur l'écran d'accueil du téléphone une fois installée.

## Fonctionnement hors ligne

L'application ne déclare **aucune permission Internet** dans son manifest et ne fait
strictement aucun appel réseau : génération des grilles, vérification, statistiques et
historique fonctionnent entièrement en local (calculs en mémoire + base SQLite embarquée).
Elle est donc **100 % utilisable sans connexion**, y compris en mode avion.

## Compilation automatique (GitHub Actions)

Ce dépôt inclut un workflow (`.github/workflows/build.yml`) qui compile automatiquement
un APK debug à chaque `push` sur la branche `main`.

**Pour récupérer l'APK compilé :**
1. Va dans l'onglet **Actions** du dépôt GitHub.
2. Clique sur le dernier run réussi ("Build APK - SAMEGLOBAL_LOTO5_90").
3. En bas de la page, dans la section **Artifacts**, télécharge `SAMEGLOBAL_LOTO5_90-debug-apk`.
4. Décompresse le fichier `.zip` téléchargé pour obtenir `app-debug.apk`.
5. Installe cet APK sur un téléphone Android (active "Sources inconnues" si nécessaire).

Aucune installation d'Android Studio n'est requise pour obtenir l'APK : tout se compile
dans le cloud via GitHub Actions.

## Structure du projet

```
SAMEGLOBAL_LOTO5_90/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/sameglobal/loto5_90/
│       │   ├── MainActivity.java
│       │   ├── GenerateurActivity.java
│       │   ├── GenerateurSysteme.java
│       │   ├── AfficherGrillesActivity.java
│       │   ├── GrillesAdapter.java
│       │   ├── VerificationActivity.java
│       │   ├── StatistiquesActivity.java
│       │   ├── HistoriqueActivity.java
│       │   ├── StringListAdapter.java
│       │   ├── DatabaseHelper.java
│       │   ├── ModuloActivity.java
│       │   ├── ModuloGroupe.java
│       │   └── GrillesRepository.java
│       └── res/
│           ├── layout/
│           └── values/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── .github/workflows/build.yml
```

## Développement local (optionnel)

Si tu veux tout de même ouvrir le projet dans Android Studio :
1. `File > Open` et sélectionne le dossier `SAMEGLOBAL_LOTO5_90`.
2. Laisse Android Studio synchroniser Gradle (il régénère automatiquement les fichiers
   du wrapper Gradle manquants).
3. `Run` sur un émulateur ou un téléphone connecté.

## Licence

Projet personnel — tous droits réservés à l'auteur.
