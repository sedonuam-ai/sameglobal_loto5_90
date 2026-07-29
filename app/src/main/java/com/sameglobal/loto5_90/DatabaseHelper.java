package com.sameglobal.loto5_90;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "loto590.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TIRAGES = "TIRAGES";
    private static final String TABLE_GRILLES = "GRILLES";

    private static final String COL_ID = "id";
    private static final String COL_NUMEROS = "numeros";
    private static final String COL_DATE = "date_ajout";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TIRAGES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUMEROS + " TEXT NOT NULL, " +
                COL_DATE + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_GRILLES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUMEROS + " TEXT NOT NULL, " +
                COL_DATE + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIRAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRILLES);
        onCreate(db);
    }

    private String maintenant() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        return sdf.format(new java.util.Date());
    }

    // ---------- TIRAGES ----------

    public void insertTirage(String numerosFormates) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NUMEROS, numerosFormates);
        values.put(COL_DATE, maintenant());
        db.insert(TABLE_TIRAGES, null, values);
    }

    public List<String> getAllTirages() {
        List<String> resultats = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TIRAGES, null, null, null, null, null,
                COL_ID + " DESC");

        while (cursor.moveToNext()) {
            String numeros = cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMEROS));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            resultats.add(date + "\n" + numeros);
        }
        cursor.close();
        return resultats;
    }

    // ---------- GRILLES ----------

    /**
     * Enregistre un lot de grilles générées, toutes avec la même date/heure de session.
     * Utilise une transaction pour rester rapide même avec 360 grilles.
     */
    public void insertGrilles(List<String> grillesFormatees) {
        SQLiteDatabase db = getWritableDatabase();
        String dateSession = maintenant();

        db.beginTransaction();
        try {
            for (String grille : grillesFormatees) {
                ContentValues values = new ContentValues();
                values.put(COL_NUMEROS, grille);
                values.put(COL_DATE, dateSession);
                db.insert(TABLE_GRILLES, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<String> getAllGrilles() {
        List<String> resultats = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_GRILLES, null, null, null, null, null,
                COL_ID + " DESC");

        while (cursor.moveToNext()) {
            String numeros = cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMEROS));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
            resultats.add(date + "\n" + numeros);
        }
        cursor.close();
        return resultats;
    }

    public void viderTirages() {
        getWritableDatabase().delete(TABLE_TIRAGES, null, null);
    }

    public void viderGrilles() {
        getWritableDatabase().delete(TABLE_GRILLES, null, null);
    }
}
