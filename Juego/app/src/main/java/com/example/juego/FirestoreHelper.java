package com.example.juego;

import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreHelper {

    private static final String TAG = "FirestoreHelper";
    private FirebaseFirestore db;
    private CollectionReference desafiosRef;
    private CollectionReference usersRef;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        desafiosRef = db.collection("challenges");
        usersRef = db.collection("users");
    }

    public void addDesafio(Desafio desafio) {
        desafiosRef.document(String.valueOf(desafio.getId())).set(desafio)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Desafío agregado con ID: " + desafio.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error al agregar desafío", e));
    }

    public void populateInitialChallenges() {
        desafiosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    Log.d(TAG, "Poblando base de datos con desafíos iniciales...");
                    addDesafio(new Desafio(1, "Asignación de Variables", "¿Qué valor falta en la siguiente asignación?\n\nx = ___\nprint(x)\n\nSalida esperada: 10", "10", "Debes asignar un número entero a la variable x.", 1));
                    addDesafio(new Desafio(2, "Condicional If", "Completa la condición para que se imprima 'Mayor que 5' si x es mayor a 5:\n\nx = 8\nif x ___ 5:\n    print('Mayor que 5')", ">", "Utiliza el operador de comparación adecuado para valores mayores.", 2));
                    addDesafio(new Desafio(3, "Bucle For", "Completa el rango para que se impriman los números del 0 al 4:\n\nfor i in range(___):\n    print(i)", "5", "La función range(n) genera valores desde 0 hasta n-1.", 3));
                    addDesafio(new Desafio(4, "Concatenación de Cadenas", "Completa el siguiente código para que la salida sea 'Hola Juan':\n\nnombre = 'Juan'\nprint('Hola ' + ___)", "nombre", "Estás concatenando una cadena literal con una variable.", 4));
                    addDesafio(new Desafio(5, "Función Definida por el Usuario", "Completa la línea para definir una función llamada saludo:\n\ndef ___():\n    print('¡Hola!')", "saludo", "Solo necesitas escribir el nombre de la función entre paréntesis.", 5));
                } else {
                    Log.d(TAG, "La colección challenges ya tiene datos. No se poblará.");
                }
            } else {
                Log.w(TAG, "Error al verificar si existen desafíos", task.getException());
            }
        });
    }

    public void getAllDesafios(OnDesafiosLoadedListener listener) {
        List<Desafio> listaDesafios = new ArrayList<>();
        desafiosRef.orderBy("dificultad", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Desafio desafio = document.toObject(Desafio.class);
                        desafio.setCompletado(false);
                        listaDesafios.add(desafio);
                    }

                    String currentUserId = "USUARIO_DE_EJEMPLO"; // <<< REEMPLAZAR CON LA UID DEL USUARIO AUTENTICADO >>>
                    // Ejemplo:
                    // FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    // String currentUserId = (currentUser != null) ? currentUser.getUid() : null;


                    if (currentUserId == null || currentUserId.isEmpty()) {
                        Log.w(TAG, "Usuario no autenticado. No se cargará el progreso.");
                        listener.onDesafiosLoaded(listaDesafios);
                        return;
                    }

                    usersRef.document(currentUserId).collection("progress").get()
                            .addOnSuccessListener(progressSnapshots -> {
                                Map<Integer, Boolean> progresoMap = new HashMap<>();
                                for (QueryDocumentSnapshot progressDoc : progressSnapshots) {
                                    try {
                                        int desafioId = Integer.parseInt(progressDoc.getId());
                                        progresoMap.put(desafioId, true);
                                    } catch (NumberFormatException e) {
                                        Log.w(TAG, "Error al parsear ID de desafío de progreso: " + progressDoc.getId(), e);
                                    }
                                }

                                for (Desafio desafio : listaDesafios) {
                                    if (progresoMap.containsKey(desafio.getId())) {
                                        desafio.setCompletado(true);
                                    }
                                }
                                listener.onDesafiosLoaded(listaDesafios);
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error al cargar progreso del usuario", e);
                                listener.onDesafiosLoaded(listaDesafios);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error al obtener desafíos", e);
                    listener.onError(e.getMessage());
                });
    }

    public interface OnDesafiosLoadedListener {
        void onDesafiosLoaded(List<Desafio> desafios);
        void onError(String errorMessage);
    }

    public void marcarDesafioCompletado(int desafioId, String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.w(TAG, "Usuario no autenticado. No se puede marcar el desafío como completado.");
            return;
        }

        DocumentReference progresoDocRef = usersRef.document(userId).collection("progress").document(String.valueOf(desafioId));
        Map<String, Object> data = new HashMap<>();
        data.put("completado", true);

        progresoDocRef.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Desafío " + desafioId + " marcado como completado para el usuario " + userId))
                .addOnFailureListener(e -> Log.w(TAG, "Error al marcar desafío " + desafioId + " como completado para el usuario " + userId, e));
    }

    public void updateDesafio(Desafio desafio) {
        desafiosRef.document(String.valueOf(desafio.getId())).set(desafio)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Desafío actualizado con ID: " + desafio.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error al actualizar desafío", e));
    }

    public void deleteDesafio(int desafioId) {
        desafiosRef.document(String.valueOf(desafioId)).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Desafío eliminado con ID: " + desafioId))
                .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar desafío", e));
    }

    public void deleteProgresoDesafio(int desafioId, String userId) {
        if (userId == null || userId.isEmpty()) {
            Log.w(TAG, "Usuario no autenticado. No se puede eliminar el progreso.");
            return;
        }
        usersRef.document(userId).collection("progress").document(String.valueOf(desafioId)).delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Progreso del desafío " + desafioId + " eliminado para el usuario " + userId))
                .addOnFailureListener(e -> Log.w(TAG, "Error al eliminar progreso del desafío " + desafioId + " para el usuario " + userId, e));
    }
}