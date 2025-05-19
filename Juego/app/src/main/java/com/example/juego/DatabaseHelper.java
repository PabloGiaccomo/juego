package com.example.juego;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "codequest.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla Desafios
    private static final String TABLE_DESAFIOS = "desafios";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_CODIGO = "codigo_incompleto";
    private static final String COLUMN_RESPUESTA = "respuesta";
    private static final String COLUMN_PISTA = "pista";
    private static final String COLUMN_DIFICULTAD = "dificultad";

    // Tabla Progreso
    private static final String TABLE_PROGRESO = "progreso";
    private static final String COLUMN_DESAFIO_ID = "desafio_id";
    private static final String COLUMN_COMPLETADO = "completado";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDesafiosTable = "CREATE TABLE " + TABLE_DESAFIOS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TITULO + " TEXT, "
                + COLUMN_CODIGO + " TEXT, "
                + COLUMN_RESPUESTA + " TEXT, "
                + COLUMN_PISTA + " TEXT, "
                + COLUMN_DIFICULTAD + " INTEGER)";
        db.execSQL(createDesafiosTable);

        String createProgresoTable = "CREATE TABLE " + TABLE_PROGRESO + " ("
                + COLUMN_DESAFIO_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_COMPLETADO + " INTEGER)";
        db.execSQL(createProgresoTable);

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        insertDesafio(db, 1, "Asignación de Variable",
                "x = ___\nprint(x)\n\nSalida esperada: 10",
                "10",
                "Coloca un número entero como valor.",
                1);

        insertDesafio(db, 2, "Condicional If",
                "x = 8\nif x ___ 5:\n    print('Mayor que 5')",
                ">",
                "Usa un operador de comparación (>, <, ==).",
                2);

        insertDesafio(db, 3, "Bucle For",
                "for i in range(___):\n    print(i)\n\nSalida esperada: 0 a 4",
                "5",
                "La función range(5) imprime del 0 al 4.",
                3);

        insertDesafio(db, 4, "Concatenación de Cadenas",
                "nombre = 'Juan'\nprint('Hola ' + ___)\n\nSalida: Hola Juan",
                "nombre",
                "Debes usar la variable que contiene el nombre.",
                4);

        insertDesafio(db, 5, "Definir Función",
                "def ___():\n    print('¡Hola mundo!')",
                "saludo",
                "Solo debes completar con el nombre de la función.",
                5);
    }

    private void insertDesafio(SQLiteDatabase db, int id, String titulo, String codigo,
                               String respuesta, String pista, int dificultad) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_CODIGO, codigo);
        values.put(COLUMN_RESPUESTA, respuesta);
        values.put(COLUMN_PISTA, pista);
        values.put(COLUMN_DIFICULTAD, dificultad);
        db.insert(TABLE_DESAFIOS, null, values);
    }

    public List<Desafio> getAllDesafios() {
        List<Desafio> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DESAFIOS,
                null, null, null, null, null,
                COLUMN_DIFICULTAD + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Desafio desafio = new Desafio(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CODIGO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESPUESTA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PISTA)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIFICULTAD))
                );

                Cursor progresoCursor = db.query(TABLE_PROGRESO,
                        new String[]{COLUMN_COMPLETADO},
                        COLUMN_DESAFIO_ID + "=?",
                        new String[]{String.valueOf(desafio.getId())},
                        null, null, null);

                if (progresoCursor.moveToFirst()) {
                    desafio.setCompletado(progresoCursor.getInt(0) == 1);
                }
                progresoCursor.close();

                lista.add(desafio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void marcarDesafioCompletado(int desafioId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESAFIO_ID, desafioId);
        values.put(COLUMN_COMPLETADO, 1);

        db.insertWithOnConflict(TABLE_PROGRESO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESAFIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESO);
        onCreate(db);
    }
}
