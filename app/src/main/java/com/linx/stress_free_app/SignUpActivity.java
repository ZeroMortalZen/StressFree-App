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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.linx.stress_free_app.StressSystem.HelperClass;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignUpActivity";

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText emailEditText, passwordEditText;
    private Button signUpButton;
    TextView textButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditLoginText);
        passwordEditText = findViewById(R.id.passwordEditLoginText);
        signUpButton = findViewById(R.id.LoginButton);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpWithEmailAndPassword();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        textButton = findViewById(R.id.text_button);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void signUpWithEmailAndPassword() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Validate email and password
        if (isEmailValid(email) && isPasswordValid(password)) {
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.isSuccessful()) {
                                SignInMethodQueryResult result = task.getResult();
                                List<String> signInMethods = result.getSignInMethods();
                                if (signInMethods != null && signInMethods.isEmpty()) {
                                    // Email is not in use, create a new account
                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                                        String userId = mAuth.getCurrentUser().getUid();
                                                        saveUserDataToDatabase(userId);
                                                        finish();

                                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                                                .setTitle("Failed to Sign up")
                                                                .setMessage("Invaild Inputs")
                                                                .show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Email is already in use
                                    new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                            .setTitle("Failed to Sign up")
                                            .setMessage("Email is already used ")
                                            .show();
                                }
                            } else {
                                new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                        .setTitle("Failed to Sign up")
                                        .setMessage("Error checking email ")
                                        .show();
                            }
                        }
                    });
        } else {
            if (!isEmailValid(email)) {
                new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                        .setTitle("Failed to Sign up")
                        .setMessage("Invalid Email ")
                        .show();
            }
            if (!isPasswordValid(password)) {
                new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                        .setTitle("Failed to Sign up")
                        .setMessage("Invalid Password ")
                        .show();
            }
        }
    }


    boolean isEmailValid(String email) {
        return email.contains("@");
    }

    boolean isPasswordValid(String password) {
        return password.length() >= 4 && password.length() <= 20;
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
                            Toast.makeText(SignUpActivity.this, "Sign in with Google successful", Toast.LENGTH_SHORT).show();
                            String userId = mAuth.getCurrentUser().getUid();
                            saveUserDataToDatabase(userId);
                            finish();

                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            new AestheticDialog.Builder(SignUpActivity.this, DialogStyle.EMOTION, DialogType.ERROR)
                                    .setTitle("Failed to Sign up")
                                    .setMessage("Sign in with google failed ")
                                    .show();
                        }
                    }
                });
    }

    private void saveUserDataToDatabase(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        HelperClass helperClass = new HelperClass(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false);
        databaseReference.child(userId).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User data saved.");
                    // Navigate to your main activity or other desired activity
                } else {
                    Log.w(TAG, "User data save failed.");
                }
            }
        });
    }



}
