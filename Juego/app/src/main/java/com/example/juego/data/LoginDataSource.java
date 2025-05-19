package com.example.juego.data;

import com.example.juego.data.model.LoggedInUser;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            Task<AuthResult> task = FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(username, password);

            Tasks.await(task); // Esto bloquea hasta que termine (requiere hilo secundario)
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();
                LoggedInUser loggedInUser = new LoggedInUser(user.getUid(), user.getEmail());
                return new Result.Success<>(loggedInUser);
            } else {
                return new Result.Error(new Exception("Login failed"));
            }
        } catch (Exception e) {
            return new Result.Error(e);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
