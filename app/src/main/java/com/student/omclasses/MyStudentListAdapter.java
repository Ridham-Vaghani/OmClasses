package com.student.omclasses;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyStudentListAdapter extends RecyclerView.Adapter<MyStudentListAdapter.ViewHolder> {

    Context context;
    ArrayList<String> list = new ArrayList<>();

    public MyStudentListAdapter(@NonNull Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            this.imageView = itemView.findViewById( R.id.imageView );
        }
    }


    @NonNull
    @Override
    public MyStudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from( parent.getContext() );
        View view = layoutInflater.inflate( R.layout.my_custom_list, parent, false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyStudentListAdapter.ViewHolder holder, final int position) {
        try {
            final String videoId = list.get( position ).split( "v=" )[1];
            String thumbnailHq = "http://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
            Picasso.get().load( thumbnailHq ).into( holder.imageView );
            Log.d( "videokey", videoId );

            holder.itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( context, PlayVideo.class );
                    intent.putExtra( "videoKey", videoId );
                    context.startActivity( intent );
                }
            } );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}