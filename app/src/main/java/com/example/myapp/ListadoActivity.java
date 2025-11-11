package com.example.myapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ListadoActivity extends AppCompatActivity {

    private FeedReaderDbHelper dbHelper;
    private TextView tvListado;
    private Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        dbHelper = new FeedReaderDbHelper(this);

        tvListado = findViewById(R.id.tvListado);
        btnRegresar = findViewById(R.id.btnRegresar);

        mostrarRegistros();

        btnRegresar.setOnClickListener(v -> finish());
    }

    private void mostrarRegistros() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + FeedReaderContract.FeedEntry.TABLE_NAME, null);

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO));
            builder.append(id).append(": ").append(nombre).append(" ").append(apellido).append("\n");
        }
        cursor.close();

        if (builder.length() == 0) {
            tvListado.setText("No hay registros guardados");
        } else {
            tvListado.setText(builder.toString());
        }
    }
}
