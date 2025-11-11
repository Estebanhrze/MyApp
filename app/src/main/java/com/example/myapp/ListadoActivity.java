package com.example.myapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ListadoActivity extends AppCompatActivity {

    private FeedReaderDbHelper dbHelper;
    private TableLayout tabla;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        dbHelper = new FeedReaderDbHelper(this);

        tabla = findViewById(R.id.tabla);
        btnRegresar = findViewById(R.id.btnRegresar);

        mostrarRegistros();

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void mostrarRegistros() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME, null);

        TableRow header = new TableRow(this);
        TextView tvId = crearCelda("ID", true);
        TextView tvNombre = crearCelda("Nombre", true);
        TextView tvApellido = crearCelda("Apellido", true);
        header.addView(tvId);
        header.addView(tvNombre);
        header.addView(tvApellido);
        tabla.addView(header);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO));

            TableRow fila = new TableRow(this);
            fila.addView(crearCelda(String.valueOf(id), false));
            fila.addView(crearCelda(nombre, false));
            fila.addView(crearCelda(apellido, false));
            tabla.addView(fila);
        }

        cursor.close();
    }

    private TextView crearCelda(String texto, boolean esEncabezado) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(16, 8, 16, 8);
        tv.setTextSize(16);
        tv.setTextColor(getResources().getColor(android.R.color.black));

        if (esEncabezado) {
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            tv.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        }
        return tv;
    }
}
