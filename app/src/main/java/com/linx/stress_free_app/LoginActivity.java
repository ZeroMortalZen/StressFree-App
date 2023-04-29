package com.linx.stress_free_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.linx.stress_free_app.MainMenu.MainMenuActivity;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        emailEditText = findViewById(R.id.emailEditLoginText);
        passwordEditText = findViewById(R.id.passwordEditLoginText);
        loginButton = findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void loginUser(String email, String password) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                startActivity(intent);

                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                new AestheticDialog.Builder(LoginActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                        .setTitle("Failed Login")
                                        .setMessage("Invaild Email or Password")
                                        .show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        }
    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                            // Navigate to your main activity or other desired activity
                            Intent intent = new Intent(LoginActivity.this, GetStarted.class);
                            startActivity(intent);

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            new AestheticDialog.Builder(LoginActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("Failed Login")
                                    .setMessage("Invaild Email or Password")
                                    .show();
                        }
                    }
                });
    }



    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}
