package com.example.att_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TRegister extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mclassNo;
    Button mTRegisterBtn;
    TextView mTLoginBtn;
    TextView mCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String tuserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tregister);


        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mclassNo = findViewById(R.id.classNo);
        mTRegisterBtn = findViewById(R.id.TRegisterBtn);
        mTLoginBtn = findViewById(R.id.TLoginBtn);
        mCreateBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser () != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mTRegisterBtn.setOnClickListener((v) ->  {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String fullName = mFullName.getText().toString();
            String classNo = mclassNo.getText().toString();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required.");
                return;
            }

            if (password.length()<6) {
                mPassword.setError("Password requires minimum 6 characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //register T-user in firebase

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(TRegister.this, "User Created.",Toast.LENGTH_SHORT).show();
                        tuserID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("tusers").document(tuserID);
                        Map<String,Object> tuser = new HashMap<>();
                        tuser.put("fullName",fullName);
                        tuser.put("email",email);
                        tuser.put("classNo",classNo);
                        documentReference.set(tuser).addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                            Log.d(TAG,"onSuccess: user profile is created for" + tuserID);
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onFailure:" + e.toString());
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),TMainActivity.class));
                    }
                    else  {
                        Toast.makeText(TRegister.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        });

        mTLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TLogin.class));

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));

            }
        });
    }
}