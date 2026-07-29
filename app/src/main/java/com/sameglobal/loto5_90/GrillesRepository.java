package com.sameglobal.loto5_90;

/**
 * Repository simple (singleton en mémoire) permettant de partager
 * le GenerateurSysteme (et ses grilles) entre les différents écrans
 * de l'application.
 */
public class GrillesRepository {

    private static GrillesRepository instance;

    private GenerateurSysteme generateurSysteme;

    private GrillesRepository() {
        generateurSysteme = new GenerateurSysteme();
    }

    public static synchronized GrillesRepository getInstance() {
        if (instance == null) {
            instance = new GrillesRepository();
        }
        return instance;
    }

    public GenerateurSysteme getGenerateurSysteme() {
        return generateurSysteme;
    }
}
