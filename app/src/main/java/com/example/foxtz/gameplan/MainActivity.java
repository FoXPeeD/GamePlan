package com.example.foxtz.gameplan;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email;
    String password;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button signin = findViewById(R.id.signinID);
        Button signup = findViewById(R.id.signupID);
        final EditText emailField = findViewById(R.id.emailID);
        final EditText passwordField = findViewById(R.id.passwordID);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
//                                    Toast.makeText(MainActivity.this, "Authentication is failed.",
//                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //get id
//                                    user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
//                                        @Override
//                                        public void onSuccess(GetTokenResult result) {
//                                            String idToken = result.getToken();
//                                            //Do whatever
//                                            Log.d(TAG, "GetTokenResult result = " + idToken);
////                                            Toast.makeText(MainActivity.this, "id is " + idToken,
////                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
                                    Toast.makeText(MainActivity.this, "id is " + user.getUid(),
                                                    Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(MainActivity.this, PostsActivity.class);
                                    intent.putExtra("key", "value");
                                    startActivity(intent);

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(MainActivity.this, "great success.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
//        if (user != null) {
//            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            findViewById(R.id.emailID).setVisibility(View.GONE);
//            findViewById(R.id.passwordID).setVisibility(View.GONE);
//            findViewById(R.id.signedInButtons).setVisibility(View.VISIBLE);
//
//            findViewById(R.id.verifyEmailButton).setEnabled(!user.isEmailVerified());
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
//
//            findViewById(R.id.emailPasswordButtons).setVisibility(View.VISIBLE);
//            findViewById(R.id.emailPasswordFields).setVisibility(View.VISIBLE);
//            findViewById(R.id.signedInButtons).setVisibility(View.GONE);
//        }
    }

}
