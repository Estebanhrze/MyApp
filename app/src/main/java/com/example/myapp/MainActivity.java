package com.example.myapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la barra de herramientas
        setSupportActionBar(binding.toolbar);

        // Referencias de los campos (ViewBinding)
        EditText edtId = binding.edtId;
        EditText edtNombre = binding.edtNombre;
        EditText edtApellido = binding.edtApellido;

        Button btnGuardar = binding.btnGuardar;
        Button btnActualizar = binding.btnActualizar;
        Button btnBuscar = binding.btnBuscar;
        Button btnEliminar = binding.btnEliminar;
        Button btnListar = binding.btnListar;

        // Acción Guardar
        btnGuardar.setOnClickListener(v -> {
            String id = edtId.getText().toString().trim();
            String nombre = edtNombre.getText().toString().trim();
            String apellido = edtApellido.getText().toString().trim();

            String msg = "Guardado:\nID: " + id + "\nNombre: " + nombre + "\nApellido: " + apellido;
            Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_LONG).show();
        });

        // Acción Actualizar
        btnActualizar.setOnClickListener(v -> {
            String id = edtId.getText().toString().trim();
            Snackbar.make(binding.getRoot(), "Actualizando registro con ID: " + id, Snackbar.LENGTH_LONG).show();
        });

        // Acción Buscar
        btnBuscar.setOnClickListener(v -> {
            String id = edtId.getText().toString().trim();
            Snackbar.make(binding.getRoot(), "Buscando registro con ID: " + id, Snackbar.LENGTH_LONG).show();
        });

        // Acción Eliminar
        btnEliminar.setOnClickListener(v -> {
            String id = edtId.getText().toString().trim();
            Snackbar.make(binding.getRoot(), "Eliminando registro con ID: " + id, Snackbar.LENGTH_LONG).show();
        });

        // Acción Listar
        btnListar.setOnClickListener(v ->
                Snackbar.make(binding.getRoot(), "Listando todas las personas registradas...", Snackbar.LENGTH_LONG).show()
        );

        // Acción del FAB
        binding.fab.setOnClickListener(view ->
                Snackbar.make(view, "Nuevo registro", Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.fab)
                        .setAction("OK", null)
                        .show()
        );
    }
}
