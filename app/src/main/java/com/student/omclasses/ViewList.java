package com.student.omclasses;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViewList extends AppCompatActivity {

    TextView Title;
    ImageView Back;
    RecyclerView recyclerView;
    ArrayList<String> list = new ArrayList<>();
    DatabaseReference databaseReference;
    MyStudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_view_list );

        bind();

        Title.setText( "Video List" );
        Back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );

        SharedPreferences sharedPreferences = this.getSharedPreferences( "Pref", MODE_PRIVATE );

        try {
            databaseReference = FirebaseDatabase.getInstance().getReference().child( "Video" ).child( sharedPreferences.getString( "std", "" ) );
            databaseReference.addChildEventListener( new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String value = dataSnapshot.getValue( String.class );
                    list.add( value );
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadData();
    }

    private void bind() {
        Title = findViewById( R.id.txtTitle );
        Back = findViewById( R.id.ivBack );
        recyclerView = findViewById( R.id.recyclerView );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        adapter = new MyStudentListAdapter( ViewList.this, list );
        LinearLayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        layoutManager.setStackFromEnd( true );
        layoutManager.setReverseLayout( true );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adapter );
    }
}
