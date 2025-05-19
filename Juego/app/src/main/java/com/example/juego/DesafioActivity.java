package com.example.juego;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.juego.R;
import com.example.juego.Desafio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DesafioActivity extends AppCompatActivity {
    private Desafio desafio;
    private TextView codigoTextView;
    private EditText respuestaEditText;
    private Button verificarButton;
    private Button pistaButton;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desafio);

        mAuth = FirebaseAuth.getInstance();
        firestoreHelper = new FirestoreHelper();

        desafio = (Desafio) getIntent().getSerializableExtra("desafio");

        codigoTextView = findViewById(R.id.codigo_text);
        respuestaEditText = findViewById(R.id.respuesta_edit);
        verificarButton = findViewById(R.id.verificar_button);
        pistaButton = findViewById(R.id.pista_button);

        codigoTextView.setText(desafio.getCodigoIncompleto());

        verificarButton.setOnClickListener(v -> verificarRespuesta());
        pistaButton.setOnClickListener(v -> mostrarPista());
    }

    private void verificarRespuesta() {
        String respuestaUsuario = respuestaEditText.getText().toString().trim();
        if (respuestaUsuario.equalsIgnoreCase(desafio.getRespuesta().trim())) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                firestoreHelper.marcarDesafioCompletado(desafio.getId(), currentUser.getUid());
            } else {
                Toast.makeText(this, "Error: Usuario no autenticado. No se pudo guardar el progreso.", Toast.LENGTH_LONG).show();
            }

            desafio.setCompletado(true);
            finish();
        } else {
            Toast.makeText(this, "Incorrecto, intenta de nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarPista() {
        Toast.makeText(this, "Pista: " + desafio.getPista(), Toast.LENGTH_LONG).show();
    }
}