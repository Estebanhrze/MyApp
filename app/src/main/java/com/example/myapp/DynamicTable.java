package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class DynamicTable {

    private TableLayout tabla;
    private Context contexto;
    private String[] cabecera;
    private ArrayList<String[]> datos;

    public DynamicTable(Context contexto, TableLayout tabla) {
        this.contexto = contexto;
        this.tabla = tabla;
    }

    public void setCabecera(String[] cabecera) {
        this.cabecera = cabecera;
        agregarCabecera();
    }

    public void setDatos(ArrayList<String[]> datos) {
        this.datos = datos;
        cargarDatos();
    }

    public void nuevaFila(String[] filaDatos) {
        TableRow fila = new TableRow(contexto);
        for (String texto : filaDatos) {
            fila.addView(nuevaCelda(texto));
        }
        tabla.addView(fila);
    }

    private TextView nuevaCelda(String texto) {
        TextView celda = new TextView(contexto);
        celda.setText(texto);
        celda.setPadding(16, 8, 16, 8);
        celda.setGravity(Gravity.CENTER);
        celda.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return celda;
    }

    private void agregarCabecera() {
        if (cabecera == null) return;
        TableRow fila = new TableRow(contexto);
        for (String titulo : cabecera) {
            TextView celda = nuevaCelda(titulo);
            celda.setTypeface(Typeface.DEFAULT_BOLD);
            fila.addView(celda);
        }
        tabla.addView(fila);
    }

    private void cargarDatos() {
        if (datos == null) return;
        for (String[] filaDatos : datos) {
            nuevaFila(filaDatos);
        }
    }

    public void cargarDesdeDB(FeedReaderDbHelper dbHelper) {
        limpiar();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME, null);

        ArrayList<String[]> filas = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO));
            filas.add(new String[]{id, nombre, apellido});
        }
        cursor.close();
        setDatos(filas);
    }

    public void limpiar() {
        if (tabla.getChildCount() > 1) {
            tabla.removeViews(1, tabla.getChildCount() - 1);
        }
    }
}
