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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
//    String email;
//    String password;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button signUp = findViewById(R.id.signUpButton);
        final EditText userName = findViewById(R.id.UserEditText);
        final EditText email = findViewById(R.id.emailEditText);
        final EditText password = findViewById(R.id.passwordEditText);
        final EditText age = findViewById(R.id.ageEditText);
        final EditText city = findViewById(R.id.cityEditText);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(registerActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(registerActivity.this, "great success.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
//
////                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                    DatabaseReference myRef = database.getReference("users/"+user.getUid());
//                                    myRef.child("user name").setValue(userName.getText().toString());
//                                    myRef.child("age").setValue(age.getText().toString());
//                                    myRef.child("city").setValue(city.getText().toString());
//                                    //verify all text fields are not empty
//                                    if (userName.getText().toString().matches("") ||
//                                            age.getText().toString().matches("") ||
//                                            city.getText().toString().matches("")){
//                                        Toast.makeText(registerActivity.this, "Some of the fields are empty",
//                                                Toast.LENGTH_LONG).show();
//                                        return;
//                                    }

                                    Intent intent = new Intent(registerActivity.this, PostsActivity.class);
                                    startActivity(intent);
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(registerActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

//        mAuth = FirebaseAuth.getInstance();

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

