package com.student.omclasses;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    TextView txtTitle;
    ImageView ivBack;
    EditText etEmail;
    Button btnReset;
    String email;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reset_password );
        bind();

        firebaseAuth = FirebaseAuth.getInstance();

        txtTitle.setText( "Reset Password" );
        ivBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        btnReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString().trim();

                if (TextUtils.isEmpty( email )) {
                    etEmail.setError( "Enter Email" );
                } else if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
                    etEmail.setError( "Enter valid Email" );
                } else {
                    startForget( email );
                }
            }
        } );
    }

    private void startForget(String email) {
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Please wait..." );
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail( email ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText( ResetPassword.this, "Password reset link sent to your email", Toast.LENGTH_SHORT ).show();
                } else {
                    Toast.makeText( ResetPassword.this, task.getException().getMessage(), Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    private void bind() {
        txtTitle = findViewById( R.id.txtTitle );
        ivBack = findViewById( R.id.ivBack );
        etEmail = findViewById( R.id.etEmail );
        btnReset = findViewById( R.id.btnReset );
    }
}
