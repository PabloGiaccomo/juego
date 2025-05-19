package com.example.juego;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class NivelesActivity extends AppCompatActivity implements NivelAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private NivelAdapter adapter;
    private List<Desafio> desafios;
    private FirestoreHelper firestoreHelper;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final String TAG = "NivelesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveles);

        mAuth = FirebaseAuth.getInstance();
        firestoreHelper = new FirestoreHelper();
        desafios = new ArrayList<>();

        progressBar = findViewById(R.id.progressBar);

        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDesafiosDesdeFirebase();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.niveles_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NivelAdapter(desafios, this);
        recyclerView.setAdapter(adapter);
    }

    private void cargarDesafiosDesdeFirebase() {
        progressBar.setVisibility(View.VISIBLE);

        firestoreHelper.getAllDesafios(new FirestoreHelper.OnDesafiosLoadedListener() {
            @Override
            public void onDesafiosLoaded(List<Desafio> loadedDesafios) {
                progressBar.setVisibility(View.GONE);
                desafios.clear();
                desafios.addAll(loadedDesafios);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Desafíos cargados y RecyclerView actualizado. Total: " + desafios.size());
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NivelesActivity.this, "Error al cargar desafíos: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al cargar desafíos: " + errorMessage);
            }
        });
    }

    @Override
    public void onItemClick(Desafio desafio) {
        Intent intent = new Intent(this, DesafioActivity.class);
        intent.putExtra("desafio", desafio);
        startActivity(intent);
    }
}