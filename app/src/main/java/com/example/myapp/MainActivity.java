package com.example.myapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FeedReaderDbHelper dbHelper;
    private EditText edtId, edtNombre, edtApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        dbHelper = new FeedReaderDbHelper(this);

        edtId = binding.edtId;
        edtNombre = binding.edtNombre;
        edtApellido = binding.edtApellido;

        Button btnGuardar = binding.btnGuardar;
        Button btnActualizar = binding.btnActualizar;
        Button btnBuscar = binding.btnBuscar;
        Button btnEliminar = binding.btnEliminar;
        Button btnListar = binding.btnListar;

        btnGuardar.setOnClickListener(v -> guardar());
        btnActualizar.setOnClickListener(v -> actualizar());
        btnBuscar.setOnClickListener(v -> buscar());
        btnEliminar.setOnClickListener(v -> eliminar());
        btnListar.setOnClickListener(v -> listar());

        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Nuevo registro", Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.fab)
                        .setAction("OK", null)
                        .show()
        );
    }

    private void guardar() {
        String nombre = edtNombre.getText().toString().trim();
        String apellido = edtApellido.getText().toString().trim();
        if (nombre.isEmpty() || apellido.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Nombre y Apellido son obligatorios", Snackbar.LENGTH_LONG).show();
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE, nombre);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO, apellido);
        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        Snackbar.make(binding.getRoot(), "Registro guardado con ID: " + newRowId, Snackbar.LENGTH_LONG).show();
    }

    private void actualizar() {
        String idTxt = edtId.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String apellido = edtApellido.getText().toString().trim();
        if (idTxt.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Ingresa un ID para actualizar", Snackbar.LENGTH_LONG).show();
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!nombre.isEmpty()) values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE, nombre);
        if (!apellido.isEmpty()) values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO, apellido);
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idTxt };
        int count = db.update(FeedReaderContract.FeedEntry.TABLE_NAME, values, selection, selectionArgs);
        Snackbar.make(binding.getRoot(), "Registros actualizados: " + count, Snackbar.LENGTH_LONG).show();
    }

    private void buscar() {
        String idTxt = edtId.getText().toString().trim();
        if (idTxt.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Ingresa un ID para buscar", Snackbar.LENGTH_LONG).show();
            return;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO
        };
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idTxt };
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            edtNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NOMBRE)));
            edtApellido.setText(cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_APELLIDO)));
            Snackbar.make(binding.getRoot(), "Registro encontrado", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(binding.getRoot(), "No se encontró el ID", Snackbar.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void eliminar() {
        String idTxt = edtId.getText().toString().trim();
        if (idTxt.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Ingresa un ID para eliminar", Snackbar.LENGTH_LONG).show();
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idTxt };
        int deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        Snackbar.make(binding.getRoot(), "Registros eliminados: " + deletedRows, Snackbar.LENGTH_LONG).show();
    }

    private void listar() {
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
        String msg = builder.length() > 0 ? builder.toString() : "No hay registros";
        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
    }
}
