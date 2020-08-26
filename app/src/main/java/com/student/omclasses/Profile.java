package com.student.omclasses;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView Profile;
    TextView Name, Gender, Std, Email, Number, Number1, Occupation, Address, Title;
    ImageView Back;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_profile );

        bind();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference( "Student" );

        Title.setText( "Profile" );
        Back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
        setData();
    }

    public void setData() {
        ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Loading data.." );
        progressDialog.setCancelable( false );
        progressDialog.show();
        Picasso.get().load( firebaseUser.getPhotoUrl() ).into( Profile );
        Name.setText( "Name: " + firebaseUser.getDisplayName() );
        Email.setText( "Email: " + firebaseUser.getEmail() );

        Query query = databaseReference.orderByChild( "email" ).equalTo( firebaseUser.getEmail() );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Number.setText( "Student: " + data.child( "number" ).getValue().toString() );
                    Number1.setText( "Father: " + data.child( "number1" ).getValue().toString() );
                    Gender.setText( "Gender: " + data.child( "gender" ).getValue().toString() );
                    Occupation.setText( "Occupation: " + data.child( "occupation" ).getValue().toString() );
                    Address.setText( "Address: " + data.child( "address" ).getValue().toString() );
                    Std.setText( "Standard: " + data.child( "standard" ).getValue().toString() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        progressDialog.dismiss();
    }

    private void bind() {
        Profile = findViewById( R.id.imgProfile );
        Name = findViewById( R.id.txtName );
        Gender = findViewById( R.id.txtGender );
        Std = findViewById( R.id.txtStd );
        Email = findViewById( R.id.txtEmail );
        Number = findViewById( R.id.txtNumber );
        Number1 = findViewById( R.id.txtNumber1 );
        Occupation = findViewById( R.id.txtFather );
        Address = findViewById( R.id.txtAddress );
        Title = findViewById( R.id.txtTitle );
        Back = findViewById( R.id.ivBack );
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}