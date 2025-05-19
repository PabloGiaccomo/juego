package com.example.juego;

import android.content.Intent; // Importar Intent
import android.os.Bundle;
import android.view.View; // Importar View
import android.widget.Button; // Importar Button
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button buttonIrANiveles; // Declarar el botón

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentBackgroundService = new Intent(this, MyService.class);
        startService(intentBackgroundService);

        // Encontrar el botón por su ID
        buttonIrANiveles = findViewById(R.id.buttonIrANiveles);

        // Configurar el OnClickListener para el botón
        buttonIrANiveles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar NivelesActivity
                Intent intent = new Intent(MainActivity.this, NivelesActivity.class);
                startActivity(intent);
            }
        });

        // Aplicar los márgenes del sistema (barra de estado, barra de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Ajustar el padding para no cubrir las barras
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets; // Aquí no se consume, solo se devuelve el insets modificado
        });
    }
}