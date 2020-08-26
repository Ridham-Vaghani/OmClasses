package com.student.omclasses;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
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

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    TextView txtHeaderName, txtHeaderEmail, txtCall;
    CircleImageView imgHeader;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ViewFlipper viewFlipper;
    int images[] = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};
    Button btnVideo;
    String deviceId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    SharedPreferences.Editor sharedPreferences;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        bind();

        deviceId = Settings.Secure.getString( getContentResolver(), Settings.Secure.ANDROID_ID );
        builder = new AlertDialog.Builder( this );
        checkLogin();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference( "Student" );

        sharedPreferences = this.getSharedPreferences( "Pref", MODE_PRIVATE ).edit();

        saveData();

        for (int images : images) {
            flipImages( images );
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        SetDrawerLayout();
        SetNavigationData();

        txtCall.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent( Intent.ACTION_CALL );
                callIntent.setData( Uri.parse( "tel:9924075179" ) );
                if (ActivityCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity( callIntent );
            }
        } );

        btnVideo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( Home.this, ViewList.class ) );
            }
        } );
    }

    private void checkLogin() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.getKey().equals( firebaseAuth.getCurrentUser().getUid() )) {
                                if (!snapshot.getValue().toString().equals( deviceId )) {
                                    builder.setTitle( "Logging Out" )
                                            .setMessage( "Already Login to another Device" )
                                            .setCancelable( false )
                                            .setPositiveButton( "Logout", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Logout();
                                                }
                                            } );
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void saveData() {
        SharedPreferences sh = this.getSharedPreferences( "Pref", MODE_PRIVATE );
        Query query = databaseReference.orderByChild( "email" ).equalTo( sh.getString( "email", "" ) );
        query.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    sharedPreferences.putString( "std", data.child( "standard" ).getValue().toString() ).apply();
                    sharedPreferences.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void flipImages(int images) {
        ImageView imageView = new ImageView( this );
        imageView.setBackgroundResource( images );
        viewFlipper.addView( imageView );
        viewFlipper.setFlipInterval( 4000 );
        viewFlipper.setAutoStart( true );
        viewFlipper.setInAnimation( this, android.R.anim.slide_out_right );
        viewFlipper.setOutAnimation( this, android.R.anim.slide_out_right );
    }

    private void bind() {
        toolbar = findViewById( R.id.toolbar );
        navigationView = findViewById( R.id.nav_view );
        drawerLayout = findViewById( R.id.drawer_layout );
        viewFlipper = findViewById( R.id.fliper );
        txtCall = findViewById( R.id.txtCall );
        btnVideo = findViewById( R.id.btnVideo );
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.navProfile) {
            startActivity( new Intent( Home.this, Profile.class ) );
        }
        if (id == R.id.navLogout) {
            Logout();
        }

        return false;
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity( new Intent( Home.this, Login.class ) );
        finish();
    }

    private void SetDrawerLayout() {
        drawerLayout = findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
    }

    private void SetNavigationData() {
        navigationView = findViewById( R.id.nav_view );
        navigationView.setItemIconTintList( null );
        navigationView.setNavigationItemSelectedListener( this );
        View navHeaderView = navigationView.inflateHeaderView( R.layout.nav_header_main );
        txtHeaderName = navHeaderView.findViewById( R.id.txtHeaderName );
        imgHeader = navHeaderView.findViewById( R.id.imgHeader );
        txtHeaderEmail = navHeaderView.findViewById( R.id.txtHeaderEmail );

        txtHeaderName.setText( firebaseUser.getDisplayName() );
        txtHeaderEmail.setText( firebaseUser.getEmail() );
        Picasso.get().load( firebaseUser.getPhotoUrl() ).into( imgHeader );
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }
}